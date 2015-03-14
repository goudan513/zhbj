package com.itheima.smartbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HorizontalScrollViewPage extends ViewPager {

	private int downx;
	private int downy;

	public HorizontalScrollViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HorizontalScrollViewPage(Context context) {
		super(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);
			downx = (int) ev.getX();
			downy = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int movex = (int) ev.getX();
			int movey = (int) ev.getY();
			int dx = movex - downx;
			int dy = movey - downy;
			if (Math.abs(dx) > Math.abs(dy)){
				if(getCurrentItem() == 0 && dx > 0) {
					// 显示的是第0张图, 并且是从左向右滑动.
					getParent().requestDisallowInterceptTouchEvent(false);
				} else if(getCurrentItem() == (getAdapter().getCount() -1)
						&& dx < 0) {
					// 显示的是最后一张图, 并且是从右向左滑动.
					getParent().requestDisallowInterceptTouchEvent(false);
				} else {
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				
			} else {
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
	

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

}
