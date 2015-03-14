package com.itheima.smartbj;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.itheima.smartbj.base.BaseFragment;
import com.itheima.smartbj.view.ContentFragment;
import com.itheima.smartbj.view.LeftFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
	public static final String LEFT = "leftfragment";
	public static final String CONTENT = "contentfragment";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.slidment_main);
		setBehindContentView(R.layout.slidment_left);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindOffset(200);
		initView();
	}

	private void initView() {
		FragmentManager supportFragmentManager = getSupportFragmentManager();
		FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
		beginTransaction.replace(R.id.fl_left, new LeftFragment(),LEFT);
		beginTransaction.replace(R.id.fl_content, new ContentFragment(), CONTENT);
		beginTransaction.commit();
	}
	
	public LeftFragment getLeft(){
		FragmentManager supportFragmentManager = getSupportFragmentManager();
		return (LeftFragment) supportFragmentManager.findFragmentByTag(LEFT);
	}
	
	public ContentFragment getContent(){
		FragmentManager supportFragmentManager = getSupportFragmentManager();
		return (ContentFragment) supportFragmentManager.findFragmentByTag(CONTENT);
	}

	
}
