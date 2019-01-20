package com.haleywang.monitor.common.req;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.haleywang.monitor.dto.Har;
import com.haleywang.monitor.dto.MyRequest;
import com.haleywang.monitor.utils.FileTool;
import com.haleywang.monitor.utils.JsonUtils;

import static org.junit.Assert.*;

public class CurlConverterTest {

    @Test
    public void toMyRequest() throws IOException {

        String input = FileTool.readInSamePkg(this.getClass(), "curl.json", true);
        System.out.println(input);

        MyRequest a = new CurlConverter().toMyRequest(input);
        Assert.assertNotNull(a);
    }

    @Test
    public void fromMyRequest() {
    }
}