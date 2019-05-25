package com.haleywang.monitor.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Preconditions;
import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.common.req.HttpMethod;
import com.haleywang.monitor.common.req.HttpRequestItem;
import com.haleywang.monitor.common.req.HttpUtils;
import com.haleywang.monitor.common.req.ReqInfoHelper;
import com.haleywang.monitor.dao.ReqGroupRepository;
import com.haleywang.monitor.dao.ReqInfoRepository;
import com.haleywang.monitor.dao.ReqMetaRepository;
import com.haleywang.monitor.dao.ReqRelationRepository;
import com.haleywang.monitor.dao.ReqTaskHistoryMetaRepository;
import com.haleywang.monitor.dao.ReqTaskHistoryRepository;
import com.haleywang.monitor.dto.UnirestRes;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqGroup;
import com.haleywang.monitor.entity.ReqInfo;
import com.haleywang.monitor.entity.ReqMeta;
import com.haleywang.monitor.entity.ReqMeta.DataType;
import com.haleywang.monitor.entity.ReqRelation;
import com.haleywang.monitor.entity.ReqSetting;
import com.haleywang.monitor.entity.ReqTaskHistory;
import com.haleywang.monitor.entity.ReqTaskHistory.HisType;
import com.haleywang.monitor.entity.ReqTaskHistoryMeta;
import com.haleywang.monitor.service.ReqGroupService;
import com.haleywang.monitor.service.ReqInfoService;
import com.haleywang.monitor.service.ReqSettingService;
import com.haleywang.monitor.utils.CollectionUtils;
import com.haleywang.monitor.utils.JavaExecScript;
import com.haleywang.monitor.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.json.JSONObject;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ReqInfoServiceImpl extends BaseServiceImpl<ReqInfo> implements
        ReqInfoService {

    public static final String GROUP_ID = "groupId";
    public static final String NAME_DESC = " name desc ";
    public static final String REQ_ID = "req_id";
    public static final String TASK_HISTORY_ID = "taskHistoryId";

    private ReqInfoRepository requestInfoRepository;

    private ReqRelationRepository reqRelationRepository;
    private ReqTaskHistoryRepository reqTaskHistoryRepository;
    private ReqGroupRepository reqGroupRepository;
    private ReqSettingService reqSettingService;
    private ReqGroupService reqGroupService;
    private ReqMetaRepository reqMetaRepository;
    private ReqTaskHistoryMetaRepository reqTaskHistoryMetaRepository;

    public ReqInfoServiceImpl() {
        setRepository();
    }

    private void setRepository() {
        this.requestInfoRepository = getMapper(ReqInfoRepository.class);
        this.reqRelationRepository = getMapper(ReqRelationRepository.class);
        this.reqTaskHistoryRepository = getMapper(ReqTaskHistoryRepository.class);
        this.reqGroupRepository = getMapper(ReqGroupRepository.class);

        this.reqMetaRepository = getMapper(ReqMetaRepository.class);
        this.reqTaskHistoryMetaRepository = getMapper(ReqTaskHistoryMetaRepository.class);
        this.mapper = (requestInfoRepository);

        this.reqSettingService = new ReqSettingServiceImpl();
        this.reqGroupService = new ReqGroupServiceImpl();
    }

    public List<ReqGroup> listRequestInfoByAccount(ReqAccount acc) {


        if (acc == null) {
            return new ArrayList<>();
        }
        Example reqRelationExample = new Example(ReqRelation.class);
        reqRelationExample.createCriteria().andEqualTo("accountId", acc.getAccountId())
                .andEqualTo("type", "ReqGroup");

        List<ReqRelation> groups = reqRelationRepository
                .selectByExample(reqRelationExample);
        if (groups.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> groupIds = groups.stream().map(ReqRelation::getObjectId)
                .collect(Collectors.toList());


        Example reqGroupExample = new Example(ReqGroup.class);
        reqGroupExample.createCriteria().andIn(GROUP_ID, groupIds);
        List<ReqGroup> gs = reqGroupRepository.selectByExample(reqGroupExample);

        Example reqInfoExample = new Example(ReqInfo.class);
        reqInfoExample.createCriteria().andIn(GROUP_ID, groupIds);
        reqInfoExample.setOrderByClause(NAME_DESC);
        List<ReqInfo> requestInfos = requestInfoRepository.selectByExample(reqInfoExample);


        Map<Long, ReqGroup> groupMap = gs.stream().collect(Collectors.toMap(ReqGroup::getGroupId, it -> it, (r, s) -> r));

        for (ReqInfo ri : requestInfos) {
            ReqGroup group = groupMap.get(ri.getGroupId());
            ri.setReqGroup(group);
        }

        Map<ReqGroup, List<ReqInfo>> requestInfosMap = requestInfos.stream()
                .collect(Collectors.groupingBy(ReqInfo::getReqGroup
                ));

        List<ReqGroup> res = new ArrayList<>();
        for (ReqGroup reqGroup : gs) {
            List<ReqInfo> riList = requestInfosMap.get(reqGroup);
            ReqGroup e = new ReqGroup(reqGroup);
            e.setChildren(new ArrayList<>());
            if (riList != null) {
                e.addItem(riList.toArray(new ReqInfo[0]));
            }
            res.add(e);
        }

        return res;

    }

    @Override
    public ReqInfo add(ReqInfo ri, ReqAccount by) {

        if (ri.getId() == null) {
            ri.setCreatedOn(new Date());
        }
        if (ri.getReqGroup() != null) {
            ReqGroup g = null;
            if (ri.getReqGroup().getGroupId() != null) {
                g = reqGroupRepository.selectOne(ri.getReqGroup());
            } else {
                String gName = ri.getReqGroup().getName();
                g = new ReqGroup();
                g.setName(gName);
                g.setCreatedBy(by);
                reqGroupService.add(g);
            }

            if (g != null && g.getGroupId() != null) {
                ri.setReqGroup(g);
            }
        }
        ri = super.save(ri);
        Set<String> keySet = ri.getMeta().keySet();
        for (String key : keySet) {
            ReqMeta rm = new ReqMeta();
            rm.setReq(ri);
            rm.setDataType(DataType.valueOf(key.toUpperCase()));
            rm.setData(ri.getMeta().get(key));
            reqMetaRepository.insert(rm);
        }

        return ri;
    }

    @Override
    public ReqInfo update(ReqInfo ri, ReqAccount by) {

        if (ri.getId() != null) {
            ReqInfo ri1 = findOne(ri.getId());
            if (ri1 != null) {
                ri.setCreatedOn(ri1.getCreatedOn());
            }
        }
        ri.setUpdatedOn(new Date());

        if (ri.getId() == null) {
            ri.setCreatedOn(new Date());
        } else {
            ri.setUpdatedOn(new Date());
        }

        String requestJsonData = parseRequestData(ri);

        Map<String, String> metaMap = ri.getMeta();

        JSONObject reqJsonObject = new JSONObject(requestJsonData);

        String method = JsonUtils.val(reqJsonObject, "method", "GET");
        String reqUrl = reqJsonObject.get("url") + "";

        ri.setName(reqJsonObject.get("name") + "");

        ri.setMethod(HttpMethod.valueOf(StringUtils.upperCase(method)));
        ri.setUrl(reqUrl);


        if (ri.getId() == null) {
            ri = super.save(ri);
        } else {
            ri = super.update(ri);
        }


        Example reqMetaExample = new Example(ReqMeta.class);
        reqMetaExample.createCriteria().andEqualTo("reqId", ri.getId());
        List<ReqMeta> reqMetalist = reqMetaRepository.selectByExample(reqMetaExample);

        for (ReqMeta reqMeta : reqMetalist) {
            reqMetaRepository.delete(reqMeta);
        }

        for (Map.Entry<String, String> entey : metaMap.entrySet()) {
            ReqMeta rm = new ReqMeta();
            rm.setReq(ri);
            rm.setDataType(DataType.valueOf(entey.getKey().toUpperCase()));
            rm.setData(entey.getValue());
            reqMetaRepository.insert(rm);
        }

        return ri;
    }

    @Override
    public ReqInfo detail(Long id, ReqAccount acc) {
        ReqInfo ri1 = new ReqInfo();
        ri1.setId(id);
        ReqInfo ri = this.findOne(ri1.getId());


        Example reqMetaExample = new Example(ReqMeta.class);
        reqMetaExample.createCriteria().andEqualTo("reqId", ri.getId());
        List<ReqMeta> rms = reqMetaRepository.selectByExample(reqMetaExample);
        if (rms == null) {
            return ri;
        }
        for (ReqMeta rm : rms) {
            if (rm.getDataType() == null) {
                continue;
            }
            ri.getMeta().put(rm.getDataType().toString().toLowerCase(), rm.getData());
        }
        return ri;
    }


    public void delete(Long id) {

        ReqInfo req = findOne(id);

        Example reqMetaExample = new Example(ReqMeta.class);
        reqMetaExample.createCriteria().andEqualTo(REQ_ID, req.getId());
        List<ReqMeta> list = reqMetaRepository.selectByExample(reqMetaExample);

        for (ReqMeta rm : list) {
            reqMetaRepository.delete(rm);
        }

        Example reqTaskHistoryMetaExample = new Example(ReqTaskHistoryMeta.class);
        reqTaskHistoryMetaExample.createCriteria().andEqualTo(REQ_ID, id);
        reqTaskHistoryMetaRepository.deleteByExample(reqTaskHistoryMetaExample);

        Example reqTaskHistoryExample = new Example(ReqTaskHistory.class);
        reqTaskHistoryExample.createCriteria().andEqualTo(REQ_ID, id);
        reqTaskHistoryRepository.deleteByExample(reqTaskHistoryExample);

        super.delete(req);
    }

    @Override
    public UnirestRes send(ReqInfo ri, ReqAccount currentAccout)
            throws IOException {

        return send(ri, currentAccout, null, null);
    }

    @Override
    public UnirestRes send(ReqInfo ri, ReqAccount currentAccout,
                           Long batchHistoryId, ReqSetting envString) throws IOException {

        Preconditions.checkNotNull(currentAccout);
        if (envString == null) {
            envString = reqSettingService.findByTypeAndOnwerAndCurrent(ReqSetting.SettingType.ENV, currentAccout.getAccountId(), 1);
        }

        String preReqResultStr = runPreRequestScript(ri, envString);

        String requestMeta = parseRequestData(ri);
        ri.getMeta().put("requestJson", requestMeta);
        String casesData = parseCasesData(ri);
        ri.getMeta().put("casesJson", casesData);


        HashMap map = JsonUtils.fromJson(requestMeta, HashMap.class);
        ri.setUrl(map.getOrDefault("url", "").toString());
        Preconditions.checkArgument(StringUtils.isNotBlank(ri.getUrl()), "Url should not be empty");

        String jsonRes = null;
        UnirestRes result = null;
        try {
            HttpRequestItem reqItem = ReqInfoHelper.parse(ri, envString, preReqResultStr);

            String reqData = JsonUtils.toJson(reqItem);
            log.info("send req: {}", reqData);
            long begin = new Date().getTime();
            result = HttpUtils.send(reqItem);
            long end = new Date().getTime();
            result.setBegin(begin);
            result.setEnd(end);
            jsonRes = JsonUtils.toJson(result);
            log.info("req response: {}", jsonRes);
        } catch (MalformedURLException e) {
            result = new UnirestRes();
        }


        String testResults = runTestRespone(ri, jsonRes, preReqResultStr);
        result.setTestResult(testResults);
        result.setTestSuccess(isTestResultSuccess(testResults));

        ReqTaskHistory reqTaskHistory = new ReqTaskHistory();
        reqTaskHistory.setCreatedOn(new Date());
        reqTaskHistory.setCreatedById(currentAccout.getAccountId());
        reqTaskHistory.setReqId(ri.getId());

        reqTaskHistory.setBatchHistoryId(batchHistoryId);
        HisType hisType = batchHistoryId != null ? HisType.BATCH : HisType.MANUAL;
        reqTaskHistory.setHisType(hisType);

        reqTaskHistory.setStatutCode("" + result.getStatus());
        reqTaskHistory.setTestSuccess("" + result.getTestSuccess());
        reqTaskHistoryRepository.insert(reqTaskHistory);

        ReqTaskHistoryMeta hm = new ReqTaskHistoryMeta();
        hm.setContent(jsonRes);
        hm.setReqTaskHistory(reqTaskHistory);
        hm.setTestReport(testResults);
        reqTaskHistoryMetaRepository.insert(hm);

        removeOldHistory(currentAccout, hisType);

        return result;
    }

    private String parseCasesData(ReqInfo ri) {
        String requestMeta = ri.getMeta().get("cases");
        requestMeta = StringUtils.defaultIfBlank(requestMeta, "{}").trim();
        if (requestMeta.indexOf("var") == 0) {
            requestMeta = JsonUtils.toJson(JavaExecScript.returnJson(requestMeta, "cases"));
        }
        return requestMeta;
    }

    private String parseRequestData(ReqInfo ri) {
        String requestMeta = ri.getMeta().get("request");
        requestMeta = StringUtils.defaultIfBlank(requestMeta, "{}").trim();
        if (requestMeta.indexOf("var") == 0) {
            requestMeta = JsonUtils.toJson(JavaExecScript.returnJson(requestMeta, "req"));
        }
        return requestMeta;
    }

    private void removeOldHistory(ReqAccount currentAccout, HisType hisType) {
        int max = 100;

        Example reqTaskHistoryExample = new Example(ReqTaskHistory.class);
        reqTaskHistoryExample.createCriteria().andEqualTo("createdById", currentAccout.getAccountId())
                .andEqualTo("hisType", hisType);
        reqTaskHistoryExample.orderBy(TASK_HISTORY_ID).asc();
        List<ReqTaskHistory> hList = reqTaskHistoryRepository.selectByExample(reqTaskHistoryExample);


        int neesDeleteSize = CollectionUtils.size(hList) - max;


        for (ReqTaskHistory rt : hList) {
            if (neesDeleteSize <= 0) {
                break;
            }
            neesDeleteSize--;


            Example reqTaskHistoryMetaExample = new Example(ReqTaskHistoryMeta.class);
            reqTaskHistoryMetaExample.createCriteria().andEqualTo(TASK_HISTORY_ID, rt.getTaskHistoryId());

            List<ReqTaskHistoryMeta> hms = reqTaskHistoryMetaRepository.selectByExample(reqTaskHistoryMetaExample);
            try {
                for (ReqTaskHistoryMeta rthm : hms) {
                    reqTaskHistoryMetaRepository.deleteByPrimaryKey(rthm.getId());
                }
                reqTaskHistoryRepository.deleteByPrimaryKey(rt.getTaskHistoryId());

            } catch (Exception e) {
                throw new ReqException(e.getMessage(), e);

            }
        }

    }

    private String runPreRequestScript(ReqInfo ri, ReqSetting envString) {
        Map<String, String> riMeta = ri.getMeta();
        String key = DataType.PRE_REQUEST_SCRIPT.name();
        String preRequestScriptCode = riMeta.getOrDefault(key.toLowerCase(), riMeta.get(key));

        if (preRequestScriptCode == null) {
            return null;
        }

        String envContent = "{}";
        if (envString != null && envString.getContent() != null) {
            envContent = envString.getContent();
        }

        return JavaExecScript.jsRunPreRequestScriptCode(preRequestScriptCode, envContent);
    }

    private String runTestRespone(ReqInfo ri, String response, String preReqResultStr) {

        Map<String, String> riMeta = ri.getMeta();
        String key = DataType.TEST_SCRIPT.name();
        String code = riMeta.getOrDefault(key.toLowerCase(), riMeta.get(key));

        return JavaExecScript.jsRunTestCode(code, response, preReqResultStr);

    }

    private Boolean isTestResultSuccess(String testResult) {
        if (StringUtils.isEmpty(testResult)) {
            return true;
        }

        TypeReference<HashMap<String, String>> t = new TypeReference<HashMap<String, String>>() {
        };
        Map<String, String> testResultMap = JsonUtils.fromJson(testResult, t);

        return !testResultMap.entrySet().stream().anyMatch(o -> !"true".equals(o.getValue()));
    }


    @Override
    public List<ReqTaskHistory> findReqTaskHistory(ReqAccount currentAccout, HisType hisType) {

        Example example1 = new Example(ReqTaskHistory.class);
        example1.orderBy(TASK_HISTORY_ID).desc();

        example1.createCriteria().andEqualTo("createdById", currentAccout.getAccountId())
                .andEqualTo("hisType", hisType.name().toUpperCase());

        RowBounds rowBounds = new RowBounds(0, 1000);

        List<ReqTaskHistory> list = reqTaskHistoryRepository.selectByExampleAndRowBounds(example1, rowBounds);

        getReqInfoForHistory(currentAccout, hisType, list);

        return list;

    }

    private void getReqInfoForHistory(ReqAccount currentAccout, HisType hisType, List<ReqTaskHistory> list) {
        List<ReqInfo> reqInfoList = requestInfoRepository.findHistoryReqInfo(currentAccout.getAccountId(), hisType.name().toUpperCase());

        Map<Long, ReqInfo> idReqInfoMap = reqInfoList.stream().collect(Collectors.toMap(ReqInfo::getId, ri -> ri, (r, s) -> r));

        list.stream().forEach(reqTaskHistory -> reqTaskHistory.setReq(idReqInfoMap.get(reqTaskHistory.getReqId())));
    }

    @Override
    public ReqTaskHistory findHistoryDetail(ReqAccount currentAccout, Long id) {
        ReqTaskHistory h = reqTaskHistoryRepository.selectByPrimaryKey(id);
        Example e = new Example(ReqTaskHistoryMeta.class);
        e.createCriteria().andEqualTo(TASK_HISTORY_ID, h.getTaskHistoryId());
        List<ReqTaskHistoryMeta> hms = reqTaskHistoryMetaRepository.selectByExample(e);
        if (!CollectionUtils.isEmpty(hms)) {
            h.setReqTaskHistoryMeta(hms.get(0));
        }
        return h;
    }

    @Override
    public List<ReqInfo> listRequestInfoBySwaggerId(ReqAccount currentAccout, String swaggerId) {

        Example reqInfoExample = new Example(ReqInfo.class);
        reqInfoExample.createCriteria().andEqualTo("swaggerId", swaggerId);
        reqInfoExample.setOrderByClause(NAME_DESC);

        return requestInfoRepository.selectByExample(reqInfoExample);
    }


    public List<ReqInfo> listRequestInfoByReqGroup(Long reqGroupId) {
        return listRequestInfoByReqGroup(reqGroupService.findOne(reqGroupId));
    }

    @Override
    public List<ReqInfo> listRequestInfoByReqGroup(ReqGroup reqGroup) {

        Example reqInfoExample = new Example(ReqInfo.class);
        reqInfoExample.createCriteria().andEqualTo(GROUP_ID, reqGroup.getGroupId());
        reqInfoExample.setOrderByClause(NAME_DESC);


        return requestInfoRepository.selectByExample(reqInfoExample);
    }

    @Override
    public List<ReqTaskHistory> findReqTaskHistory(ReqAccount currentAccout, Long batchHistoryId) {
        Example e = new Example(ReqTaskHistory.class);
        e.createCriteria().andEqualTo("batchHistoryId", batchHistoryId);

        List<ReqTaskHistory> list = reqTaskHistoryRepository.selectByExample(e);


        getReqInfoForHistory(currentAccout, HisType.BATCH, list);

        return list;

    }


}
