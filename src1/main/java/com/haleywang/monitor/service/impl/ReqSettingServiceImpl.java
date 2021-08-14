package com.haleywang.monitor.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.ctrl.v1.ConfigCtrl;
import com.haleywang.monitor.dao.ReqSettingRepository;
import com.haleywang.monitor.dto.ConfigDto;
import com.haleywang.monitor.dto.IdValuePair;
import com.haleywang.monitor.dto.ResultMessage;
import com.haleywang.monitor.dto.msg.SettingDeleteMsg;
import com.haleywang.monitor.dto.msg.SettingSaveMsg;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqSetting;
import com.haleywang.monitor.service.ReqSettingService;
import com.haleywang.monitor.utils.FileTool;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author haley
 * @date 2018/12/16
 */
public class ReqSettingServiceImpl extends BaseServiceImpl<ReqSetting> implements ReqSettingService {

    private ReqSettingRepository reqSettingRepository;

    public ReqSettingServiceImpl() {
        setReqSettingRepository();
    }

    private void setReqSettingRepository() {
        this.reqSettingRepository = getMapper(ReqSettingRepository.class);
        this.mapper = reqSettingRepository;
    }


    public ResultMessage<ReqSetting, SettingSaveMsg> saveSetting(ReqSetting ri, ReqAccount acc) {

        ResultMessage<ReqSetting, SettingSaveMsg> res = new ResultMessage<>();

        if (ri.getId() != null) {
            ReqSetting ri1 = findOne(ri.getId());
            if (ri1 == null) {
                return res.ofMessage(SettingSaveMsg.NOT_FOUND);
            }

            if (!Objects.equals(ri1.getOnwer() , acc.getAccountId())) {
                return res.ofMessage(SettingSaveMsg.NOT_ALLOWED);
            }

            if (ri.getId() != null) {
                update(ri);

            } else {
                save(ri);
            }

            return res;
        }

        ri.setOnwer(acc.getAccountId());
        Preconditions.checkNotNull(ri.getType(), "Setting type must be not null");
        save(ri);
        return res;
    }


    @Override
    public ReqSetting findByTypeAndOnwerAndCurrent(ReqSetting.SettingType type, Long onwer, int current) {

        Example reqSettingExample = new Example(ReqSetting.class);
        reqSettingExample.createCriteria().andEqualTo("type", type)
                .andEqualTo("onwer", onwer)
                .andEqualTo("current", current);

        return reqSettingRepository.selectOneByExample(reqSettingExample);

    }


    @Override
    public List<ReqSetting> findByOnwer(Long onwer) {

        Example reqSettingExample = new Example(ReqSetting.class);
        reqSettingExample.createCriteria()
                .andEqualTo("onwer", onwer);

        return reqSettingRepository.selectByExample(reqSettingExample);
    }

    public ResultMessage<Long, SettingDeleteMsg> delete(Long id, ReqAccount acc)  {

        ResultMessage<Long, SettingDeleteMsg> res = new ResultMessage<>();

        ReqSetting ri1 = findOne(id);
        if (ri1 == null) {
            return res.ofMessage(SettingDeleteMsg.NOT_FOUND);
        }

        if (!Objects.equals(ri1.getOnwer() , acc.getAccountId())) {
            return res.ofMessage(SettingDeleteMsg.NOT_ALLOWED);
        }

        delete(ri1);

        return res.ofData(id);
    }

    public ConfigDto parseConfigDto() {
        ConfigDto configDto = new ConfigDto();

        try {

            String reqDemoData = FileTool.readInSamePkg(ConfigCtrl.class, "request_demo_data.xml");

            Document reqDemoDataDoc = DocumentHelper.parseText(reqDemoData);
            List<Node> nodes = reqDemoDataDoc.selectNodes("/req_datas/req_data");


            ImmutableSet<String> exampleGroups = configDto.examplesMap().keySet();
            for (int i = 0, n = nodes.size(); i < n; i++) {
                Node node = nodes.get(i);

                final int idx = i;

                for (String exampleCategory : exampleGroups) {
                    Optional.ofNullable(node.selectSingleNode(exampleCategory))
                            .map(Node::getStringValue)
                            .ifPresent(text ->
                                    configDto.examplesMap().get(exampleCategory).add(new IdValuePair().of(idx, StringUtils.trim(text)))
                            );
                }

            }
        } catch (Exception e) {
            throw new ReqException(e);
        }

        return configDto;

    }
}
