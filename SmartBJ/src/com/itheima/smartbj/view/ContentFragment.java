package com.itheima.smartbj.view;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.itheima.smartbj.MainActivity;
import com.itheima.smartbj.R;
import com.itheima.smartbj.base.BaseFragment;
import com.itheima.smartbj.base.TabBasePage;
import com.itheima.smartbj.base.impl.GovAffairsPage;
import com.itheima.smartbj.base.impl.HomePage;
import com.itheima.smartbj.base.impl.NewsCenterPage;
import com.itheima.smartbj.base.impl.SettingPage;
import com.itheima.smartbj.base.impl.SmartServicePage;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ContentFragment extends BaseFragment {

	@ViewInject(R.id.vp_fgmain_content)
	private com.itheima.smartbj.view.NoScrollViewPage viewPager;

	@ViewInject(R.id.rg_fgmain_bottom)
	private RadioGroup radioGroup;

	private List<TabBasePage> pages;

	@Override
	public View initView() {
		View root = View.inflate(mActivity, R.layout.fragment_main, null);
		ViewUtils.inject(this, root);
		return root;
	}

	@Override
	public void initData() {
		pages = new ArrayList<TabBasePage>();
		pages.add(new HomePage(mActivity));
		pages.add(new NewsCenterPage(mActivity));
		pages.add(new SmartServicePage(mActivity));
		pages.add(new GovAffairsPage(mActivity));
		pages.add(new SettingPage(mActivity));

		viewPager.setAdapter(new MyAdapter());

		radioGroup.setOnCheckedChangeListener(listener);
		radioGroup.check(R.id.rb_firstpage);
		
	}
	private OnCheckedChangeListener listener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.rb_firstpage:// 首页
				enableMenu(false);
				viewPager.setCurrentItem(0);
				break;
			case R.id.rb_newscenter:// 新闻中心
				enableMenu(true);
				viewPager.setCurrentItem(1);
				break;
			case R.id.rb_smartservice:// 智慧服务
				enableMenu(true);
				viewPager.setCurrentItem(2);
				break;
			case R.id.rb_govaffirs:// 政务
				enableMenu(true);
				viewPager.setCurrentItem(3);
				break;
			case R.id.rb_setting:// 设置中心
				enableMenu(false);
				viewPager.setCurrentItem(4);
				break;

			default:
				break;
			}
		}
	};

	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pages.size();
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
			TabBasePage page = pages.get(position);
			View root = page.getRootView();
			container.addView(root);
			page.initData();
			return root;
		}

	}
	
	/**
	 * 
	 * @param flag
	 *    true  左侧菜单可用
	 */
	private void enableMenu(boolean flag){
		MainActivity ma = (MainActivity) mActivity;
		SlidingMenu slidingMenu = ma.getSlidingMenu();
		if (flag){
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	
	public NewsCenterPage getNewsCenterPage(){
		return (NewsCenterPage) pages.get(1);
	}

}
