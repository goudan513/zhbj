package com.itheima.smartbj.newscentermenu.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.smartbj.MainActivity;
import com.itheima.smartbj.R;
import com.itheima.smartbj.base.NewsCenterMenuBasePager;
import com.itheima.smartbj.domain.NewsMessage.Data;
import com.itheima.smartbj.domain.NewsMessage.Data.Children;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

public class NewsMenuPage extends NewsCenterMenuBasePager {
	
	private List<Children> tags;//Ò³Ç©µÄÊý¾Ý
	private List<NewsMenuTabDetailPage> tagPages;
	
	@ViewInject(R.id.pi_newsmenupage)
	private TabPageIndicator pageIndicator;
	
	@ViewInject(R.id.vp_newsmenupage_content)
	private ViewPager viewPager;
	
	public NewsMenuPage(Context context) {
		super(context);
	}

	public NewsMenuPage(Context context, Data data) {
		this(context);
		tags = data.children;
	}

	@Override
	public View initView() {
		View root = View.inflate(context, R.layout.news_menupage, null);
		ViewUtils.inject(this, root);
		return root;
	}
	
	@OnClick(R.id.ib_newsmenupage_next)
	public void nextTag(View v){
		viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
	}
	
	@Override
	public void initData() {
		tagPages = new ArrayList<NewsMenuTabDetailPage>();
		System.out.println(tags.size());
		for (int i = 0; i <tags.size(); i++) {
			Children children = tags.get(i);
			NewsMenuTabDetailPage newsMenuTabDetailPage = new NewsMenuTabDetailPage(context,children);
			tagPages.add(newsMenuTabDetailPage);
		}
		
		MyPageAdapter pa = new MyPageAdapter();
		System.out.println("mViewPager:" + viewPager);
		viewPager.setAdapter(pa);
		
		pageIndicator.setViewPager(viewPager);
		
		pageIndicator.setOnPageChangeListener(new MyOnPageChangeListener());
		
	}
	private class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int position) {
			MainActivity ma = (MainActivity) context;
			if (position == 0){
				ma.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			} else {
				ma.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			
		}

	}
	
	private class MyPageAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return tagPages.size();
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
		public CharSequence getPageTitle(int position) {
			return tags.get(position).title;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			NewsMenuTabDetailPage page = (NewsMenuTabDetailPage) tagPages.get(position);
			View view = page.getRoot();
			container.addView(view);
			page.initData();
			return view;
		}
		
	}

}
