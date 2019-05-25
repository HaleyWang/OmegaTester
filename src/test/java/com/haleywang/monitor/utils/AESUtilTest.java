package com.haleywang.monitor.utils;

import org.junit.Assert;
import org.junit.Test;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

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
        byte[] input = "Hello AES-GCM World!".getBytes(DEFAULT_CODING);
        String inputText = new String(input, DEFAULT_CODING);
        String keyStr = ENCRYPTION_COMM_KEY;

        byte[] keyBytes = keyStr.getBytes(DEFAULT_CODING);

        byte[] cipherText = AESUtil.encryptByte(input, keyBytes);

        byte[] bytes2 = Base64.getDecoder().decode(new String(Base64.getEncoder().encode(cipherText)));


        Assert.assertTrue(Arrays.equals(cipherText, bytes2));


        byte[] plainText = AESUtil.decryptByte(bytes2, keyBytes);

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

        byte[] cipherText = AESUtil.encryptByte(input, keyBytes);

        byte[] plainText = AESUtil.decryptByte(cipherText, keyBytes);

        Assert.assertTrue(Arrays.equals(input, plainText));
    }



}