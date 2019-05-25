package com.haleywang.monitor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@NoArgsConstructor( access = AccessLevel.PRIVATE)
public class AES {


    public static final String DEFAULT_CODING = "utf-8";


    public static String decrypt(String encrypted, String seed) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        byte[] keyb = seed.getBytes(DEFAULT_CODING);
        SecretKeySpec skey = getSecretKeySpec(keyb);
        Cipher dcipher = Cipher.getInstance("AES");
        dcipher.init(Cipher.DECRYPT_MODE, skey);

        byte[] clearbyte = dcipher.doFinal(toByte(encrypted));
        return new String(clearbyte, DEFAULT_CODING);
    }


    public static String encrypt(String content, String key) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, ShortBufferException {
        byte[] input = content.getBytes(DEFAULT_CODING);

        SecretKeySpec skc = getSecretKeySpec(key.getBytes(DEFAULT_CODING));
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, skc);

        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
        cipher.doFinal(cipherText, ctLength);

        return parseByte2HexStr(cipherText);
    }

    public static SecretKeySpec getSecretKeySpec(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytes);
        return new SecretKeySpec(thedigest, "AES");
    }


    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    private static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private static Random random = new Random();

    private static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String generateKey() { // length表示生成字符串的长度
		return getRandomString(16);
	}
    

} 