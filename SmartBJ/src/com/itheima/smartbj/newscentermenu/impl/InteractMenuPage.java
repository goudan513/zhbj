package com.itheima.smartbj.newscentermenu.impl;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.itheima.smartbj.base.NewsCenterMenuBasePager;

public class InteractMenuPage extends NewsCenterMenuBasePager {

	public InteractMenuPage(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		
		TextView tv = new TextView(context);
		tv.setText("»¥¶¯µÄÄÚÈÝ");
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(Color.RED);
		tv.setTextSize(25);
		return tv;
	}

}
