package com.itheima.smartbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.itheima.smartbj.util.CacheUtils;
import com.itheima.smartbj.util.Constants;


/**
 * @author Administrator
 * Splash界面
 */
public class SplashActivity extends Activity {
	private View root;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		initView();
	}
	/**
	 * 初始化 View
	 */
	private void initView() {
		root = findViewById(R.id.rl_splash_root);
		/**
		 * 渐变动画
		 */
		AlphaAnimation aa = new AlphaAnimation(0, 1);
		//设置动画时间
		aa.setDuration(2000);
		//保持动画之后的状态
		aa.setFillAfter(true);
		
		/**
		 * 旋转动画
		 */
		RotateAnimation ra = new RotateAnimation(
				0, 360, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(1000);
		ra.setFillAfter(true);
		
		/**
		 * 比例动画，由原来的一般到全部
		 */
		ScaleAnimation sa = new ScaleAnimation(
				0.5f, 1, 
				0.5f, 1, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(1000);
		sa.setFillAfter(true);
		/**
		 * 设置动画集合，加载所有动画
		 */
		AnimationSet as = new AnimationSet(false);
		as.addAnimation(ra);
		as.addAnimation(sa);
		as.addAnimation(aa);
		
		/*
		 * 监听动画的状态
		 */
		as.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			/**
			 * 动画完成
			 */
			@Override
			public void onAnimationEnd(Animation animation) {
				//判断是否进入过向导界面，如果没有进入向导，否则进入主界面
				//true 表示进入过设置向导 false 没有
				if (CacheUtils.getBoolean(getApplicationContext(), Constants.ISGUIDE, false)){
					//进入主界面
					Intent intent = new Intent(SplashActivity.this,MainActivity.class);
					startActivity(intent);
				} else {
					//进入设置向导
					Intent intent = new Intent(SplashActivity.this,GuideActivity.class);
					startActivity(intent);
				}
				finish();//关闭Splash界面
			}
		});
		//开始动画
		root.startAnimation(as);
		
		
		
	}


}
