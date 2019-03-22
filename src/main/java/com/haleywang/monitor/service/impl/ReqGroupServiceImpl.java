package com.haleywang.monitor.service.impl;

import com.haleywang.monitor.dao.ReqAccountRepository;
import com.haleywang.monitor.dao.ReqGroupRepository;
import com.haleywang.monitor.dao.ReqRelationRepository;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqGroup;
import com.haleywang.monitor.entity.ReqRelation;
import com.haleywang.monitor.service.ReqGroupService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

public class ReqGroupServiceImpl extends BaseServiceImpl<ReqGroup> implements ReqGroupService {
	
	private ReqGroupRepository reqGroupRepository;
	
	@Resource
	private ReqAccountRepository reqAccountRepository;
	
	@Resource
	ReqRelationRepository reqRelationRepository;

	public ReqGroupServiceImpl() {
		super();
		setRepository();
	}


	@Resource
	public void setRepository() {
		ReqGroupRepository reqGroupRepository = getMapper(ReqGroupRepository.class);
		this.reqAccountRepository = getMapper(ReqAccountRepository.class);
		this.reqRelationRepository = getMapper(ReqRelationRepository.class);
		this.reqGroupRepository = reqGroupRepository;

		this.mapper = reqGroupRepository;
	}

    @Override
    public List<ReqGroup> listByAccount(ReqAccount currentAccout) {

        Example reqRelationExample = new Example(ReqRelation.class);
        reqRelationExample.createCriteria().andEqualTo("accountId", currentAccout.getAccountId())
                .andEqualTo("type", "ReqGroup");
        List<ReqRelation> groups = reqRelationRepository.selectByExample(reqRelationExample);
        if(groups.size() == 0) {
            return null;
        }

        List<Long> groupIds = groups.stream().map(ReqRelation::getObjectId).collect(Collectors.toList());


        Example reqGroupExample = new Example(ReqGroup.class);
        reqGroupExample.createCriteria().andIn("groupId", groupIds);
        List<ReqGroup> gs = reqGroupRepository.selectByExample(reqGroupExample);

        return gs;
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

}
