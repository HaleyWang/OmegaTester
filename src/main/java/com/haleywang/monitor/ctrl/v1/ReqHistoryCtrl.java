package com.haleywang.monitor.ctrl.v1;

import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqInfo;
import com.haleywang.monitor.entity.ReqTaskHistory;
import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.service.ReqInfoService;
import com.haleywang.monitor.service.impl.ReqInfoServiceImpl;
import com.haleywang.monitor.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by haley on 2018/8/18.
 */
public class ReqHistoryCtrl extends BaseCtrl {

    //findReqTaskHistory
    public String list() throws IOException {

        ReqTaskHistory.HisType hisType = ReqTaskHistory.HisType
                .valueOf(StringUtils.upperCase(getUrlParam("hisType", "manual")));
        Long batchHistoryId = NumberUtils.createLong(getUrlParam("batchHistoryId"));

        ReqAccount acc = currentAccountAndCheck();
        ResultStatus<List<ReqTaskHistory>> res = new ResultStatus<>();

        ReqInfoService requestInfoService = new ReqInfoServiceImpl();
        if (batchHistoryId != null) {
            List<ReqTaskHistory> ll = requestInfoService.findReqTaskHistory(acc, batchHistoryId);

            return JsonUtils.toJson(res.ofData(ll));
        }

        List<ReqTaskHistory> ll = requestInfoService.findReqTaskHistory(acc, hisType);

        res.ofData(ll);

        return JsonUtils.toJson(res);
    }

    //req/history
    //RequestMapping( value = "/detail", method = RequestMethod.GET)
    public String detail() throws IOException {
        System.out.println(" ====> ");

        Long id = Long.parseLong(getUrlParam("id"));
        checkNotNull(id, "Parameter id must be not null");

        ReqAccount acc = currentAccountAndCheck();
        ResultStatus<ReqTaskHistory> res = new ResultStatus<>();

        ReqInfoService requestInfoService = new ReqInfoServiceImpl();
        ReqTaskHistory resHustory = requestInfoService.findHistoryDetail(acc, id);

        ReqInfo ri = requestInfoService.detail(resHustory.getReqId(), acc);

        resHustory.setReq(ri);

        res.ofData(resHustory);

        return JsonUtils.toJson(res);
    }

}
