package com.haleywang.monitor.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by haley on 2018/12/8.
 */
public class AESTest {


    @Test
    public void decrypt() throws Exception {

        String key = AES.generateKey();
        String str = "Message";
        String mm = AES.encrypt(str, key);
        String result = AES.decrypt(mm, key);
        Assert.assertEquals(str, result);

    }

    @Test
    public void encryptEmpty() throws Exception {
        String key = AES.generateKey();
        String str = "";
        String mm = AES.encrypt(str, key);
        String result = AES.decrypt(mm, key);
        Assert.assertEquals(str, result);
    }

    @Test
    public void generateKey() throws Exception {
        String key = AES.generateKey();
        Assert.assertEquals(16, key.length());

    }

}