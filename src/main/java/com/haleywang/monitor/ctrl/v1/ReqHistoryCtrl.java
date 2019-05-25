package com.haleywang.monitor.ctrl.v1;

import com.haleywang.monitor.common.mvc.BaseCtrl;
import com.haleywang.monitor.dto.ResultStatus;
import com.haleywang.monitor.entity.ReqAccount;
import com.haleywang.monitor.entity.ReqInfo;
import com.haleywang.monitor.entity.ReqTaskHistory;
import com.haleywang.monitor.service.ReqInfoService;
import com.haleywang.monitor.service.impl.ReqInfoServiceImpl;
import com.haleywang.monitor.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by haley on 2018/8/18.
 */
public class ReqHistoryCtrl extends BaseCtrl {

    public ResultStatus<List<ReqTaskHistory>> list() {

        ReqTaskHistory.HisType hisType = ReqTaskHistory.HisType
                .valueOf(StringUtils.upperCase(getUrlParam("hisType", "manual")));
        Long batchHistoryId = NumberUtils.createLong(getUrlParam("batchHistoryId"));

        ReqAccount acc = currentAccountAndCheck();
        ResultStatus<List<ReqTaskHistory>> res = new ResultStatus<>();

        ReqInfoService requestInfoService = new ReqInfoServiceImpl();
        if (batchHistoryId != null) {
            List<ReqTaskHistory> ll = requestInfoService.findReqTaskHistory(acc, batchHistoryId);
            return res.ofData(ll);
        }

        List<ReqTaskHistory> ll = requestInfoService.findReqTaskHistory(acc, hisType);

        return res.ofData(ll);
    }

    public ResultStatus<ReqTaskHistory> detail() {

        Long id = Long.parseLong(getUrlParam("id"));
        checkNotNull(id, "Parameter id must be not null");

        ReqAccount acc = currentAccountAndCheck();

        ReqInfoService requestInfoService = new ReqInfoServiceImpl();
        ReqTaskHistory resHistory = requestInfoService.findHistoryDetail(acc, id);
        ReqInfo ri = requestInfoService.detail(resHistory.getReqId(), acc);
        resHistory.setReq(ri);

        return new ResultStatus<>(resHistory);
    }

}
