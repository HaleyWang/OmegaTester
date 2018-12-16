package com.haleywang.monitor.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by haley on 2018/12/9.
 */
public class DateUtilsTest {
    @Test
    public void copy() throws Exception {

        Date aDate = new Date();
        Date aCopyDate = DateUtils.copy(aDate);

        Assert.assertEquals(aDate.getTime(), aCopyDate.getTime());

        Assert.assertFalse(aDate == aCopyDate);
    }

}