package com.itheima.smartbj.util;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtils {
	private static final String CONFIG = "config";
	private static SharedPreferences sp;

	/**
	 * 
	 * @param context
	 * @param key
	 *            关键字
	 * @param defValue
	 *            默认值
	 * 
	 *            往SharedPreferences中写个key-value保存
	 */
	public static void putBoolean(Context context, String key, boolean value) {
		if (sp == null) {
			sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}

	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defValue);
	}

	public static void putString(Context context, String key, String value) {
		if (sp == null) {
			sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}

	public static String getString(Context context, String key, String defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		return sp.getString(key, defValue);
	}
}
