package com.itheima.smartbj;

import java.util.ArrayList;
import java.util.List;

import com.itheima.smartbj.util.CacheUtils;
import com.itheima.smartbj.util.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity {
	private ViewPager viewPager;

	private List<ImageView> views;

	private int[] pics;

	private LinearLayout ll_points;

	private Button bt_expr;

	private View point;
	
	private int pointWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initData();
		initEvent();
	}

	private void initEvent() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if (position == views.size() - 1) {
					bt_expr.setVisibility(View.VISIBLE);
				} else {
					bt_expr.setVisibility(View.GONE);
				}				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				int dis = (int) ((position + positionOffset) * pointWidth);
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) point.getLayoutParams();
				layoutParams.leftMargin = dis;
				point.setLayoutParams(layoutParams);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		bt_expr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CacheUtils.putBoolean(getApplicationContext(), Constants.ISGUIDE, true);
				Intent intent = new Intent(GuideActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void initData() {
		int[] pics = { R.drawable.guide_1, R.drawable.guide_2,
				R.drawable.guide_3 };
		views = new ArrayList<ImageView>();
		ImageView iv;
		View v;
		for (int i = 0; i < pics.length; i++) {
			iv = new ImageView(GuideActivity.this);
			iv.setBackgroundResource(pics[i]);
			views.add(iv);

			v = new View(GuideActivity.this);
			LayoutParams params = new LayoutParams(10, 10);
			v.setLayoutParams(params);
			v.setBackgroundResource(R.drawable.point_normal);
			if (i != 0) {
				params.leftMargin = 10;
			}
			ll_points.addView(v);
		}

		MyAdapter adapter = new MyAdapter();
		viewPager.setAdapter(adapter);

	}



	private void initView() {
		setContentView(R.layout.activity_guide);
		viewPager = (ViewPager) findViewById(R.id.vp_guide_view);
		ll_points = (LinearLayout) findViewById(R.id.ll_guide_points);
		bt_expr = (Button) findViewById(R.id.bt_guide_exper);
		point = findViewById(R.id.v_guide_point);
		point.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				pointWidth = ll_points.getChildAt(1).getLeft() - ll_points.getChildAt(0).getLeft();
			}
		});
	}
	
	
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			/*
			 * ImageView view = new ImageView(getApplicationContext());
			 * view.setBackgroundResource(pics[position]);
			 */
			ImageView view = views.get(position);
			container.addView(view);
			return view;
		}

	}
}
