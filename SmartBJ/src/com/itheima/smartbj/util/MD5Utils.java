package com.itheima.smartbj.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Utils {
	public static String encode(String str){
		StringBuilder res = new StringBuilder();
		try {
			byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes("utf-8"));
			for (byte b : digest) {
				String hexString = Integer.toHexString(b);
				if (hexString.length() == 1){
					hexString = "0" + hexString;
				}
				res.append(hexString);
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res + "";
	}
}
