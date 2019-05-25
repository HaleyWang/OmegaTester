package com.haleywang.monitor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AESUtil {
	
	public static final String ENCRYPTION_COMM_KEY = "agbcdzheagbcdzhe";
	
	private static final String ENCRYPTION_IV = "4e5Wa71fYoT7MFEX";

	public static final int AES_KEY_SIZE = 128; // in bits
	public static final int GCM_NONCE_LENGTH = 12; // in bytes
	public static final int GCM_TAG_LENGTH = 16; // in bytes
	public static final String DEFAULT_CODING = "utf-8";
	public static final byte[] SALT = "Whatever I like".getBytes();



	public static String encrypt(String content, String key) throws Exception {

		return bytesToHex(encryptByte(content.getBytes(DEFAULT_CODING), key.getBytes(DEFAULT_CODING)));

	}
	

	public static String decrypt(String encrypted, String key) throws Exception {
		
		return new String(decryptByte(hexStringToByteArray(encrypted), key.getBytes(DEFAULT_CODING)), DEFAULT_CODING);
	}


	public static SecretKeySpec getSecretKeySpec(byte[] bytes) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] thedigest = md.digest(bytes);
		return new SecretKeySpec(thedigest, "AES");
	}

	public static  byte[] encryptByte (byte[] content, byte[] keyBytes) throws Exception {
		// Initialise random and generate key

		SecretKeySpec key = getSecretKeySpec(keyBytes);


		// Encrypt
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
		final byte[] nonce = new byte[GCM_NONCE_LENGTH];
		GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
		cipher.init(Cipher.ENCRYPT_MODE, key, spec);

		cipher.updateAAD(SALT);

		return  cipher.doFinal(content);
	}

	public static  byte[] decryptByte (byte[] encrypted , byte[] keyBytes) throws Exception {


		// Decrypt; nonce is shared implicitly
		SecretKeySpec key = getSecretKeySpec(keyBytes);

		Cipher cipher2 = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
		final byte[] nonce = new byte[GCM_NONCE_LENGTH];
		GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
		cipher2.init(Cipher.DECRYPT_MODE, key, spec);


		cipher2.updateAAD(SALT);


		return cipher2.doFinal(encrypted);
	}

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}


	public static String generateKey() {
		return AES.generateKey();
	}

}