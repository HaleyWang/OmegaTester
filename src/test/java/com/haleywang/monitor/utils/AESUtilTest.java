package com.haleywang.monitor.utils;

import org.junit.Test;

import static com.haleywang.monitor.utils.AESUtil.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by haley on 2017/3/10.
 */
public class AESUtilTest {
    @Test
    public void testEncrypt() throws Exception {
        String en = encrypt("a" , ENCRYPTION_COMM_KEY);

        assertEquals("a", decrypt(en, ENCRYPTION_COMM_KEY));
    }

    @Test
    public void testDecrypt() throws Exception {
        String en = encrypt("a" , ENCRYPTION_COMM_KEY);

        assertEquals("a", decrypt(en, ENCRYPTION_COMM_KEY));
    }

    @Test
    public void testGenerateKey() throws Exception {
        String key = generateKey();

        assertEquals(16, key.length());

    }




}