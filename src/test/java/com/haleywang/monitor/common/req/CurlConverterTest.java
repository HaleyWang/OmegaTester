package com.haleywang.monitor.common.req;

import java.io.IOException;

import com.haleywang.monitor.common.req.converter.CurlConverter;
import com.haleywang.monitor.dto.TypeValuePair;
import org.junit.Assert;
import org.junit.Test;

import com.haleywang.monitor.dto.MyRequest;
import com.haleywang.monitor.utils.FileTool;

public class CurlConverterTest {

    @Test
    public void toMyRequest() throws IOException {

        String input = FileTool.readInSamePkg(this.getClass(), "curl.json", true);
        System.out.println(input);

        MyRequest a = new CurlConverter().toMyRequest(TypeValuePair.builder().type("cURL").value(input).build());
        Assert.assertNotNull(a);
    }

    @Test
    public void fromMyRequest() {
    }
}