package com.haleywang.monitor.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by haley on 2018/12/8.
 */
public class UrlUtilsTest {

    @Test
    public void getDomainName() throws Exception {
        String u = "https://www.haleywang.com/a/search?rlz=1C1GCEU_enSG825SG825&ei=ot8JXOjwNsz6-QaZurK4BA";
        String s = UrlUtils.getDomainName(u);
        Assert.assertEquals("haleywang.com", s);
    }

    @Test
    public void getPath() throws Exception {

        String u = "https://www.haleywang.com/a/search?rlz=1C1GCEU_enSG825SG825&ei=ot8JXOjwNsz6-QaZurK4BA";
        String s = UrlUtils.getPath(u);
        Assert.assertEquals("/a/search", s);

        String u1 = "https://www.haleywang.com/a/search#blog";
        String s1 = UrlUtils.getPath(u);
        Assert.assertEquals("/a/search", s1);
    }

}