package com.haleywang.monitor.service.impl;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import com.fasterxml.jackson.core.type.TypeReference;
import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.dao.ReqGroupRepository;
import com.haleywang.monitor.dao.ReqInfoRepository;
import com.haleywang.monitor.dao.ReqMetaRepository;
import com.haleywang.monitor.dao.ReqRelationRepository;
import com.haleywang.monitor.dao.ReqTaskHistoryMetaRepository;
import com.haleywang.monitor.dao.ReqTaskHistoryRepository;
import com.haleywang.monitor.dto.UnirestRes;
import com.haleywang.monitor.model.ReqAccount;
import com.haleywang.monitor.model.ReqGroup;
import com.haleywang.monitor.model.ReqInfo;
import com.haleywang.monitor.model.ReqMeta;
import com.haleywang.monitor.model.ReqMeta.DataType;
import com.haleywang.monitor.model.ReqRelation;
import com.haleywang.monitor.model.ReqSetting;
import com.haleywang.monitor.model.ReqTaskHistory;
import com.haleywang.monitor.model.ReqTaskHistory.HisType;
import com.haleywang.monitor.model.ReqTaskHistoryMeta;
import com.haleywang.monitor.service.ReqGroupService;
import com.haleywang.monitor.service.ReqInfoService;
import com.haleywang.monitor.service.ReqSettingService;
import com.haleywang.monitor.utils.CollectionUtils;
import com.haleywang.monitor.utils.JavaExecScript;
import com.haleywang.monitor.utils.JsonUtils;
import com.haleywang.monitor.utils.http.HttpRequestItem;
import com.haleywang.monitor.utils.http.HttpUtils;
import com.haleywang.monitor.utils.http.ReqInfoHelper;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.mybatis.mapper.entity.Example;

public class ReqInfoServiceImpl extends BaseServiceImpl<ReqInfo> implements
		ReqInfoService {

	private static final Logger LOG = LoggerFactory.getLogger(ReqInfoServiceImpl.class);
	
	static int HISTORY_LIMIT_SIZE = 10;

	private ReqInfoRepository requestInfoRepository;

	@Resource
	private ReqRelationRepository reqRelationRepository;

	@Resource
	private ReqTaskHistoryRepository reqTaskHistoryRepository;

	@Resource
	private ReqGroupRepository reqGroupRepository;

	@Resource
	private ReqSettingService reqSettingService;

	@Resource
	private ReqGroupService reqGroupService;

	@Resource
	private ReqMetaRepository reqMetaRepository;
	@Resource
	private ReqTaskHistoryMetaRepository reqTaskHistoryMetaRepository;

	public ReqInfoServiceImpl() {
		setRepository();
	}

	@Resource
	public void setRepository() {
		ReqInfoRepository requestInfoRepository = getMapper(ReqInfoRepository.class);
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
			return null;
		}
        Example reqRelationExample = new Example(ReqRelation.class);
        reqRelationExample.createCriteria().andEqualTo("accountId", acc.getAccountId())
        .andEqualTo("type", "ReqGroup");

		List<ReqRelation> groups = reqRelationRepository
				.selectByExample(reqRelationExample);
		if (groups.size() == 0) {
			return null;
		}

		List<Long> groupIds = groups.stream().map(ReqRelation::getObjectId)
				.collect(Collectors.toList());


        Example reqGroupExample = new Example(ReqGroup.class);
        reqGroupExample.createCriteria().andIn("groupId", groupIds);
        List<ReqGroup> gs = reqGroupRepository.selectByExample(reqGroupExample);

        Example reqInfoExample = new Example(ReqInfo.class);
        reqInfoExample.createCriteria().andIn("groupId", groupIds);
        reqInfoExample.setOrderByClause(" name desc ");
		List<ReqInfo> requestInfos = requestInfoRepository.selectByExample(reqInfoExample);


        Map<Long, ReqGroup> groupMap = gs.stream().collect(Collectors.toMap(ReqGroup::getGroupId, it -> it, (r, s) -> r));

        for(ReqInfo ri: requestInfos) {
            ReqGroup group = groupMap.get(ri.getGroupId());
            ri.setReqGroup(group);
        }

		Map<ReqGroup, List<ReqInfo>> requestInfosMap = requestInfos.stream()
				.collect(Collectors.groupingBy(requestInfo -> {
					ReqGroup g = requestInfo.getReqGroup();
					return g;
				}));

		List<ReqGroup> res = new ArrayList<>();
		for (ReqGroup reqGroup : gs) {
			List<ReqInfo> riList = requestInfosMap.get(reqGroup);
			ReqGroup e = reqGroup.clone();
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

		if (ri.getId() == null) {
			ri.setCreatedOn(new Date());
		} else {
			ri.setUpdatedOn(new Date());
		}
		Map<String, String> metaMap = ri.getMeta();
        if(ri.getId() == null) {
            ri = super.save(ri);
        }else {
            ri = super.update(ri);
        }


        Example reqMetaExample = new Example(ReqMeta.class);
        reqMetaExample.createCriteria().andEqualTo("reqId", ri.getId());
		List<ReqMeta> reqMetalist = reqMetaRepository.selectByExample(reqMetaExample);

		for(ReqMeta reqMeta : reqMetalist) {
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
            if(rm.getDataType() == null) {
                continue;
            }
			ri.getMeta().put(rm.getDataType().toString().toLowerCase(), rm.getData());
		}
		return ri;
	}


	public void delete(Long id) {

		ReqInfo req = findOne(id);

        Example reqMetaExample = new Example(ReqMeta.class);
        reqMetaExample.createCriteria().andEqualTo("req_id", req.getId());
        List<ReqMeta> list = reqMetaRepository.selectByExample(reqMetaExample);

        for(ReqMeta rm : list) {
            reqMetaRepository.delete(rm);
        }

        Example reqTaskHistoryMetaExample = new Example(ReqTaskHistoryMeta.class);
        reqTaskHistoryMetaExample.createCriteria().andEqualTo("req_id", id);
		reqTaskHistoryMetaRepository.deleteByExample(reqTaskHistoryMetaExample);

        Example reqTaskHistoryExample = new Example(ReqTaskHistory.class);
        reqTaskHistoryExample.createCriteria().andEqualTo("req_id", id);
		reqTaskHistoryRepository.deleteByExample(reqTaskHistoryExample);

		super.delete(req);
	}

	@Override
	public UnirestRes<String> send(ReqInfo ri, ReqAccount currentAccout)
			throws UnirestException, MalformedURLException {

		return send(ri, currentAccout, null, null);
	}

	@Override
	public UnirestRes<String> send(ReqInfo ri, ReqAccount currentAccout,
								   Long batchHistoryId, ReqSetting envString) throws UnirestException, MalformedURLException {

		if(envString == null ) {
			if( currentAccout != null) {
				envString = reqSettingService.findByTypeAndOnwerAndCurrent(ReqSetting.SettingType.ENV, currentAccout.getAccountId(), 1);
			}
		}

		String preReqResultStr =  runPreRequestScript(ri, envString);

		HttpRequestItem reqItem = ReqInfoHelper.parse(ri, envString, preReqResultStr);

		LOG.info("send req: " + JsonUtils.toJson(reqItem));
		UnirestRes<String> result = new UnirestRes<String>(
				HttpUtils.send(reqItem));
		String jsonRes = JsonUtils.toJson(result);
		LOG.info("req response: " + jsonRes);
		
		
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

		reqTaskHistory.setStatutCode(""+result.getStatus());
		reqTaskHistory.setTestSuccess(""+result.getTestSuccess());
		reqTaskHistoryRepository.insert(reqTaskHistory);

		ReqTaskHistoryMeta hm = new ReqTaskHistoryMeta();
		hm.setContent(jsonRes);
		hm.setReqTaskHistory(reqTaskHistory);
		hm.setTestReport(testResults);
		reqTaskHistoryMetaRepository.insert(hm);


        removeOldHistory(ri, currentAccout, hisType);

		return result;
	}

    private void removeOldHistory(ReqInfo ri, ReqAccount currentAccout, HisType hisType) {
        int max = 100;

		Example reqTaskHistoryExample = new Example(ReqTaskHistory.class);
		reqTaskHistoryExample.createCriteria().andEqualTo("createdById", currentAccout.getAccountId())
		.andEqualTo("hisType", hisType);
		//reqTaskHistoryExample.setOrderByClause("task_history_id asc");
		reqTaskHistoryExample.orderBy("taskHistoryId").asc();
		List<ReqTaskHistory> hList = reqTaskHistoryRepository.selectByExample(reqTaskHistoryExample);


        int neesDeleteSize = CollectionUtils.size(hList) - max;


        for(ReqTaskHistory rt : hList) {
            if(neesDeleteSize <=0 ) {
                break;
            }
            neesDeleteSize--;


            Example reqTaskHistoryMetaExample = new Example(ReqTaskHistoryMeta.class);
            reqTaskHistoryMetaExample.createCriteria().andEqualTo("taskHistoryId", rt.getTaskHistoryId());

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
		String preRequestScriptCode = riMeta.get(DataType.PRE_REQUEST_SCRIPT.toString());

        if(preRequestScriptCode == null) {
            return null;
        }

        String envContent = "{}";
        if(envString != null && envString.getContent() != null) {
            envContent = envString.getContent();
        }

		String res = JavaExecScript.jsRunPreRequestScriptCode(preRequestScriptCode, envContent);

		return res;
	}

	private String runTestRespone(ReqInfo ri, String response, String preReqResultStr) {
		//ReqMeta testScriptRM = reqMetaRepository.findByDataTypeAndReqIn(DataType.testScript, ri);

		Map<String, String> riMeta = ri.getMeta();
		String code = riMeta.get(DataType.TEST_SCRIPT.name());
		System.out.println(code);

		String res = JavaExecScript.jsRunTestCode(code, response, preReqResultStr);

		return res;
	}
	
	private Boolean isTestResultSuccess(String testResult) {
		//ReqMeta testScriptRM = reqMetaRepository.findByDataTypeAndReqIn(DataType.testScript, ri);
		if(StringUtils.isEmpty(testResult)) {
			return true;
		}

		TypeReference<HashMap<String, String>> t = new TypeReference<HashMap<String, String>>() {
		};
		Map<String, String> testResultMap = JsonUtils.fromJson(testResult, t);
		if(testResultMap.isEmpty()) {
			return true;
		}

		return !testResultMap.entrySet().stream().anyMatch(o -> !"true".equals(o.getValue()));
	}
	

	@Override
	public List<ReqTaskHistory> findReqTaskHistory(ReqAccount currentAccout, HisType hisType)  {
		


		Example example1 = new Example(ReqTaskHistory.class);
		example1.orderBy("taskHistoryId").desc();

		example1.createCriteria().andEqualTo("createdById", currentAccout.getAccountId())
				.andEqualTo("hisType", hisType.name().toUpperCase());

		RowBounds rowBounds = new RowBounds(0, 1000);

		List<ReqTaskHistory> list = reqTaskHistoryRepository.selectByExampleAndRowBounds(example1, rowBounds);

		List<ReqInfo> reqInfoList = requestInfoRepository.findHistoryReqInfo(currentAccout.getAccountId(), hisType.name().toUpperCase());

		Map<Long, ReqInfo> idReqInfoMap = reqInfoList.stream().collect(Collectors.toMap(ri -> ri.getId(), ri -> ri, (r, s) -> r));

		list.stream().forEach(reqTaskHistory -> reqTaskHistory.setReq(idReqInfoMap.get(reqTaskHistory.getReqId())));

		return list;

		//return reqTaskHistoryRepository.findByCreatedByIdAndHisType(currentAccout.getAccountId(), hisType);
		
	}
	
	@Override
	public ReqTaskHistory findHistoryDetail(ReqAccount currentAccout, Long id)  {
		ReqTaskHistory h = reqTaskHistoryRepository.selectByPrimaryKey(id);
		// todo
		Example e = new Example(ReqTaskHistoryMeta.class);
		e.createCriteria().andEqualTo("taskHistoryId", h.getTaskHistoryId());
		List<ReqTaskHistoryMeta> hms = reqTaskHistoryMetaRepository.selectByExample(e);
		if(!CollectionUtils.isEmpty(hms)) {
			h.setReqTaskHistoryMeta(hms.get(0));
		}
		return h;
	}

	@Override
	public List<ReqInfo> listRequestInfoBySwaggerId(ReqAccount currentAccout, String swaggerId) {

        Example reqInfoExample = new Example(ReqInfo.class);
        reqInfoExample.createCriteria().andEqualTo("swaggerId", swaggerId);
        reqInfoExample.setOrderByClause(" name desc ");

        return requestInfoRepository.selectByExample(reqInfoExample);
	}


	public List<ReqInfo> listRequestInfoByReqGroup(Long reqGroupId) {
		return listRequestInfoByReqGroup(reqGroupService.findOne(reqGroupId));
	}

		@Override
	public List<ReqInfo> listRequestInfoByReqGroup(ReqGroup reqGroup) {

        Example reqInfoExample = new Example(ReqInfo.class);
        reqInfoExample.createCriteria().andEqualTo("groupId", reqGroup.getGroupId());
        reqInfoExample.setOrderByClause(" name desc ");


        return  requestInfoRepository.selectByExample(reqInfoExample);
	}

	@Override
	public List<ReqTaskHistory> findReqTaskHistory(ReqAccount currentAccout, Long batchHistoryId) {
		//Sort sort =  Sort.of( "historyId", false);
		//Pageable pageable = new PageRequest(0, HISTORY_LIMIT_SIZE, sort);
			//reqTaskHistoryRepository.findByCreatedByIdAndHisType(accountId, hisType);
		Example e = new Example(ReqTaskHistory.class);
		e.createCriteria().andEqualTo("batchHistoryId", batchHistoryId);

		return reqTaskHistoryRepository.selectByExample(e);
	}
	
	
}
