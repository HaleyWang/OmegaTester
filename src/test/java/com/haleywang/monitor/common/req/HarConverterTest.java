package com.haleywang.monitor.common.req;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.haleywang.monitor.dto.Har;
import com.haleywang.monitor.dto.MyRequest;
import com.haleywang.monitor.utils.FileTool;
import com.haleywang.monitor.utils.JsonUtils;

public class HarConverterTest {


    @Test
    public void toMyRequests() throws IOException {

        String harJson = FileTool.readInSamePkg(HarConverterTest.class, "har.json", true);
        MyRequest a = new HarConverter().toMyRequest(harJson);
        Assert.assertNotNull(a);

    }
}