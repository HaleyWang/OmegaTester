package com.haleywang.monitor.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Base64;

import static com.haleywang.monitor.utils.AesUtil.DEFAULT_CODING;
import static com.haleywang.monitor.utils.AesUtil.ENCRYPTION_COMM_KEY;
import static com.haleywang.monitor.utils.AesUtil.decrypt;
import static com.haleywang.monitor.utils.AesUtil.decryptByte;
import static com.haleywang.monitor.utils.AesUtil.encrypt;
import static com.haleywang.monitor.utils.AesUtil.encryptByte;
import static com.haleywang.monitor.utils.AesUtil.generateKey;
import static org.junit.Assert.assertEquals;

/**
 * Created by haley on 2017/3/10.
 */
public class AesUtilTest {
    @Test
    public void testEncrypt() throws Exception {
        String en = encrypt("a", ENCRYPTION_COMM_KEY);

        assertEquals("a", decrypt(en, ENCRYPTION_COMM_KEY));
    }

    @Test
    public void testDecrypt() throws Exception {
        byte[] input = "Hello AES-GCM World!".getBytes(DEFAULT_CODING);
        String keyStr = ENCRYPTION_COMM_KEY;

        byte[] keyBytes = keyStr.getBytes(DEFAULT_CODING);

        byte[] cipherText = AesUtil.encryptByte(input, keyBytes);

        byte[] bytes2 = Base64.getDecoder().decode(new String(Base64.getEncoder().encode(cipherText)));


        Assert.assertTrue(Arrays.equals(cipherText, bytes2));


        byte[] plainText = AesUtil.decryptByte(bytes2, keyBytes);

        Assert.assertTrue(Arrays.equals(input, plainText));


    }

    @Test
    public void testGenerateKey() throws Exception {
        String key = generateKey();

        assertEquals(16, key.length());

    }


    @Test
    public void testDecrypt1() throws Exception {
        byte[] input = "Hello AES-GCM World!".getBytes(DEFAULT_CODING);


        String keyStr = ENCRYPTION_COMM_KEY;
        byte[] keyBytes = keyStr.getBytes(DEFAULT_CODING);

        byte[] cipherText = encryptByte(input, keyBytes);

        byte[] plainText = decryptByte(cipherText, keyBytes);

        Assert.assertTrue(Arrays.equals(input, plainText));
    }



}