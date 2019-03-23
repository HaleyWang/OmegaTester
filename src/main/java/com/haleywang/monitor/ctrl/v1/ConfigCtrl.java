package com.haleywang.monitor.ctrl.v1;

import com.google.common.collect.ImmutableSet;
import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.common.req.HttpMethod;
import com.haleywang.monitor.dto.ConfigDto;
import com.haleywang.monitor.dto.IdValuePair;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.utils.FileTool;
import com.haleywang.monitor.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;

import javax.xml.bind.*;
import javax.xml.bind.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class ConfigCtrl extends BaseCtrl {

    public String list() {
        System.out.println(" ====> methods");
        ResultStatus<ConfigDto> res = new ResultStatus<>();

        HttpMethod[] methods = HttpMethod.values();

        ConfigDto dto = parseConfigDto();
        dto.setMethods(methods);

        return JsonUtils.toJson(res.ofData(dto));
    }

    public static void main(String[] args) {
        ConfigCtrl.parseConfigDto();
    }


    private static ConfigDto parseConfigDto(){
        ConfigDto configDto = new ConfigDto();

        try {



            String reqDemoData = FileTool.readInSamePkg(ConfigCtrl.class, "request_demo_data.xml");

            Document reqDemoDataDoc = DocumentHelper. parseText(reqDemoData);
            List<Node> nodes = reqDemoDataDoc.selectNodes("/req_datas/req_data") ;


            ImmutableSet<String> exampleGroups = configDto.examplesMap().keySet();
            for(int i = 0 , n = nodes.size(); i < n ; i++) {
                Node node = nodes.get(i);

                final int idx = i;

                for (String exampleCategory : exampleGroups) {
                    Optional.ofNullable(node.selectSingleNode(exampleCategory))
                            .map(Node::getStringValue)
                            .ifPresent( text -> {
                                configDto.examplesMap().get(exampleCategory).add(new IdValuePair().of(idx, StringUtils.trim(text)));
                            });
                }

            }
        } catch (DocumentException e ) {
            throw new ReqException(e);
        } catch (Exception e ) {
            throw new ReqException(e);

        }

        return configDto;

    }

    public String methods() {
        System.out.println(" ====> methods");
        ResultStatus<HttpMethod[]> res = new ResultStatus<>();

        HttpMethod[] values = HttpMethod.values();

        return JsonUtils.toJson(res.ofData(values));
    }
}
