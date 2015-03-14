package com.itheima.smartbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPage extends LazyViewPager {

	public NoScrollViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollViewPage(Context context) {
		super(context);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

}
