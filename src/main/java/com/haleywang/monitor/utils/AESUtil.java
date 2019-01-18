package com.haleywang.monitor.utils;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AESUtil {
	
	public static final String ENCRYPTION_COMM_KEY = "agbcdzheagbcdzhe";
	
	private static final String ENCRYPTION_IV = "4e5Wa71fYoT7MFEX";


	public static String encrypt(String content, String key) throws Exception {

		return AES.encrypt(content, key);

	}
	

	public static String decrypt(String encrypted, String key) throws Exception {
		
		return AES.decrypt(encrypted, key);
	}

	public static String generateKey() {
		return AES.generateKey();
	}

}