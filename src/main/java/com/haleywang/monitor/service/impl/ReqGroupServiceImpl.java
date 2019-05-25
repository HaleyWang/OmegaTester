package com.haleywang.monitor.service.impl;

import com.google.common.collect.ImmutableMap;
import com.haleywang.monitor.common.Msg;
import com.haleywang.monitor.common.req.HttpMethod;
import com.haleywang.monitor.dao.ReqAccountRepository;
import com.haleywang.monitor.dao.ReqGroupRepository;
import com.haleywang.monitor.dao.ReqRelationRepository;
import com.haleywang.monitor.dto.ConfigDto;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqGroup;
import com.haleywang.monitor.entity.ReqInfo;
import com.haleywang.monitor.entity.ReqRelation;
import com.haleywang.monitor.entity.ReqSetting;
import com.haleywang.monitor.service.ReqGroupService;
import com.haleywang.monitor.service.ReqInfoService;
import com.haleywang.monitor.utils.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReqGroupServiceImpl extends BaseServiceImpl<ReqGroup> implements ReqGroupService {
	
	private ReqGroupRepository reqGroupRepository;
	private ReqAccountRepository reqAccountRepository;
	private ReqRelationRepository reqRelationRepository;

	public ReqGroupServiceImpl() {
		super();
		setRepository();
	}


	public void setRepository() {
		this.reqAccountRepository = getMapper(ReqAccountRepository.class);
		this.reqRelationRepository = getMapper(ReqRelationRepository.class);
		this.reqGroupRepository = getMapper(ReqGroupRepository.class);
		this.mapper = reqGroupRepository;
	}

	public void initExampleData(ReqAccount currentAccount) {

		ReqGroup reqGroup = add(ReqGroup.builder().createdBy(currentAccount).name("Example").build());
		ConfigDto configDto =new ReqSettingServiceImpl().parseConfigDto();

		ReqInfo exampleReqInfo = ReqInfo.builder()
				.method(HttpMethod.GET)
				.name("example 1")
				.url("http://localhost:8000/v1/req/version")
				.reqGroup(reqGroup)
				.createdOn(new Date())
				.sort(0)
				.meta(ImmutableMap.of("request", configDto.getReqExamples().get(0).getValue()))
				.build();
		new ReqInfoServiceImpl().add(exampleReqInfo, currentAccount);

		ReqSetting setting = ReqSetting.builder()
				.type(ReqSetting.SettingType.ENV)
				.onwer(currentAccount.getAccountId())
				.current(1).name("DEV")
				.content("{\"host\":\"http://localhost:8000\"}").build();
		new ReqSettingServiceImpl().saveSetting(setting, currentAccount);
	}

    @Override
    public List<ReqGroup> listByAccount(ReqAccount currentAccount) {

		List<ReqGroup> data = findGroupData(currentAccount);
		if(CollectionUtils.isEmpty(data)) {
			initExampleData(currentAccount);
			data = findGroupData(currentAccount);
		}
		return data;
    }

	public List<ReqGroup> findGroupData(ReqAccount currentAccount) {

		Example reqRelationExample = new Example(ReqRelation.class);
		reqRelationExample.createCriteria().andEqualTo("accountId", currentAccount.getAccountId())
				.andEqualTo("type", "ReqGroup");
		List<ReqRelation> groups = reqRelationRepository.selectByExample(reqRelationExample);

		List<Long> groupIds = groups.stream().map(ReqRelation::getObjectId).collect(Collectors.toList());


		Example reqGroupExample = new Example(ReqGroup.class);
		reqGroupExample.createCriteria().andIn("groupId", groupIds);
		return reqGroupRepository.selectByExample(reqGroupExample);
	}

	@Override
	public ReqGroup add(ReqGroup g) {

		ReqAccount c = g.getCreatedBy();
		c = reqAccountRepository.selectOne(c);
		g.setCreatedBy(c);
		reqGroupRepository.insert(g);

		ReqRelation re = new ReqRelation(g.getGroupId(), "ReqGroup", "CMD", c);
        re.setAccountId(c.getAccountId());
		reqRelationRepository.insert(re);

		return g;
	}

	public ResultStatus<ReqGroup> groupDelete(Long id, ReqAccount currentAccount) {
		ResultStatus<ReqGroup> res = new ResultStatus<>();

		ReqInfoService requestInfoService = new ReqInfoServiceImpl();

		ReqGroup rg = findOne(id);

		List<ReqInfo> reqs = requestInfoService.listRequestInfoByReqGroup(rg);
		if (!reqs.isEmpty()) {
			return res.of(Msg.NOT_ALLOWED);
		}
		delete(rg);

		Example reqRelationExample = new Example(ReqRelation.class);
		reqRelationExample.createCriteria().andEqualTo("accountId", currentAccount.getAccountId())
				.andEqualTo("type", "ReqGroup").andEqualTo("objectId", id);
		List<ReqRelation> groups = reqRelationRepository.selectByExample(reqRelationExample);

		return res.ofData(rg);
	}

}
