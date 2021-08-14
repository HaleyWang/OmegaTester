package com.haleywang.monitor.common.req;

import java.io.IOException;

import com.haleywang.monitor.common.req.converter.HarConverter;
import com.haleywang.monitor.dto.TypeValuePair;
import org.junit.Assert;
import org.junit.Test;

import com.haleywang.monitor.dto.MyRequest;
import com.haleywang.monitor.utils.FileTool;

public class HarConverterTest {


    @Test
    public void toMyRequests() throws IOException {

        String harJson = FileTool.readInSamePkg(HarConverterTest.class, "har.json", true);
        MyRequest a = new HarConverter().toMyRequest(TypeValuePair.builder().type("HAR").value(harJson).build());
        Assert.assertNotNull(a);

    }
}