package com.haleywang.monitor.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by haley on 2018/12/9.
 */
public class CollectionUtilsTest {
    @Test
    public void isEmpty() throws Exception {

        Assert.assertTrue(CollectionUtils.isEmpty(null));
        Assert.assertTrue(CollectionUtils.isEmpty(new ArrayList<>()));
        Assert.assertFalse( CollectionUtils.isEmpty(Arrays.asList("1","2")));
    }

    @Test
    public void size() throws Exception {
        Assert.assertEquals(-1, CollectionUtils.size(null));
        Assert.assertEquals(0, CollectionUtils.size(new ArrayList<>()));
        Assert.assertEquals(2, CollectionUtils.size(Arrays.asList("1","2")));
    }

}