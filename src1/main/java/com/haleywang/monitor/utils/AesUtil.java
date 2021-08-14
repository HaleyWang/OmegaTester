package com.haleywang.monitor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Random;

/**
 * @author haley
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AesUtil {

	public static final String ENCRYPTION_COMM_KEY = "agbcdzheagbcdzhe";
	private static final String ENCRYPTION_IV = "4e5Wa71fYoT7MFEX";

	public static final int AES_KEY_SIZE = 128;
	public static final int GCM_NONCE_LENGTH = 12;
	public static final int GCM_TAG_LENGTH = 16;
	public static final String DEFAULT_CODING = "utf-8";
	protected static final byte[] SALT = "Whatever I like".getBytes();

	public static String encrypt(String content, String key) throws UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException {
		return bytesToHex(encryptByte(content.getBytes(DEFAULT_CODING), key.getBytes(DEFAULT_CODING)));
	}


	public static String decrypt(String encrypted, String key) throws UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException {
		return new String(decryptByte(hexStringToByteArray(encrypted), key.getBytes(DEFAULT_CODING)), DEFAULT_CODING);
	}


	public static SecretKeySpec getSecretKeySpec(byte[] bytes) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] thedigest = md.digest(bytes);
		return new SecretKeySpec(thedigest, "AES");
	}

	public static byte[] encryptByte(byte[] content, byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {
		// Initialise random and generate key
		SecretKeySpec key = getSecretKeySpec(keyBytes);

		// Encrypt
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
		final byte[] nonce = new byte[GCM_NONCE_LENGTH];
		GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
		cipher.init(Cipher.ENCRYPT_MODE, key, spec);

		cipher.updateAAD(SALT);

		return cipher.doFinal(content);
	}

	public static byte[] decryptByte(byte[] encrypted, byte[] keyBytes) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {


		// Decrypt; nonce is shared implicitly
		SecretKeySpec key = getSecretKeySpec(keyBytes);

		Cipher cipher2 = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");
		final byte[] nonce = new byte[GCM_NONCE_LENGTH];
		GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
		cipher2.init(Cipher.DECRYPT_MODE, key, spec);

		cipher2.updateAAD(SALT);
		return cipher2.doFinal(encrypted);
	}

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		int two = 2;
		int radix16 = 16;
		byte[] data = new byte[len / two];
		for (int i = 0; i < len; i += two) {
			data[i / two] = (byte) ((Character.digit(s.charAt(i), radix16) << 4) + Character.digit(s.charAt(i + 1), radix16));
		}
		return data;
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