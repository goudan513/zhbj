package com.itheima.smartbj.newscentermenu.impl;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itheima.smartbj.NewsDetailActivity;
import com.itheima.smartbj.R;
import com.itheima.smartbj.base.NewsCenterMenuBasePager;
import com.itheima.smartbj.domain.NewsCenterTagBean;
import com.itheima.smartbj.domain.NewsCenterTagBean.NewsCenterData.NewsCenterDataNews;
import com.itheima.smartbj.domain.NewsCenterTagBean.NewsCenterData.NewsCenterDataTopnews;
import com.itheima.smartbj.domain.NewsMessage.Data.Children;
import com.itheima.smartbj.util.CacheUtils;
import com.itheima.smartbj.util.Constants;
import com.itheima.smartbj.view.MyListView;
import com.itheima.smartbj.view.MyListView.OnStateUpdate;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class NewsMenuTabDetailPage extends NewsCenterMenuBasePager {

	@ViewInject(R.id.vp_topnews)
	private com.itheima.smartbj.view.HorizontalScrollViewPage vp_topnews;

	@ViewInject(R.id.tv_picdec)
	private TextView tv_picdec;

	@ViewInject(R.id.ll_picpoints)
	private LinearLayout ll_picpoints;

	@ViewInject(R.id.lv_listnews)
	private MyListView lv_listnews;

	private String detailUrl;
	private Children childTag;
	private List<NewsCenterTagBean.NewsCenterData.NewsCenterDataTopnews> topnews;
	private List<NewsCenterTagBean.NewsCenterData.NewsCenterDataNews> listNews;
	private BitmapUtils bu;

	private MyPageAdapter myPageAdapter;
	private MyListAdapter listAdapter;
	private int previousIndex;
	
	private String moreUrl;
	
	public NewsMenuTabDetailPage(Context context) {
		super(context);
	}

	public NewsMenuTabDetailPage(Context context, Children childTag) {
		this(context);
		this.childTag = childTag;
		detailUrl = Constants.URL + childTag.url;
		bu = new BitmapUtils(context);
		bu.configDefaultBitmapConfig(Config.ARGB_4444);
	}

	@Override
	public View initView() {
		View top = View.inflate(context, R.layout.tab_detail_view_top, null);
		ViewUtils.inject(this, top);
		
		View root = View.inflate(context, R.layout.tab_detail_view_list, null);
		ViewUtils.inject(this, root);
		
		lv_listnews.addListItemHeadView(top);
		lv_listnews.setOnStateUpdate(new MyOnStateUpdate());
		lv_listnews.setOnItemClickListener(new MyOnItemClickListener());
		return root;
	}
	
	private class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int location,
				long arg3) {
			NewsCenterDataNews newsCenterDataNews = listNews.get(location - 1);
			String ids = CacheUtils.getString(context, Constants.READIDS, "");
			if (TextUtils.isEmpty(ids)){
				ids = newsCenterDataNews.id;
			} else {
				ids += ( "," + newsCenterDataNews.id);
			}
			
			CacheUtils.putString(context, Constants.READIDS, ids);
			listAdapter.notifyDataSetChanged();
			
			Intent intent = new Intent(context,NewsDetailActivity.class);
			intent.putExtra("url", newsCenterDataNews.url);
			context.startActivity(intent);
			
			
		}
		
	}
	
	
	private class MyOnStateUpdate implements OnStateUpdate{

		@Override
		public void pulldown() {
			
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.send(HttpMethod.GET, detailUrl, new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					Toast.makeText(context, "更新数据成功", 1).show();
					CacheUtils.putString(context, detailUrl, responseInfo.result);
					processData(responseInfo.result);
					lv_listnews.onFreshStateFinish();
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					Toast.makeText(context, "更新失败", 1).show();
					lv_listnews.onFreshStateFinish();
				}
			});
			
						
		}

		@Override
		public void loadMore() {
			if (TextUtils.isEmpty(moreUrl)) {
				Toast.makeText(context, "没有更多数据了", 1).show();
				lv_listnews.onFreshStateFinish();
			} else {
				HttpUtils utils = new HttpUtils();
				utils.send(HttpMethod.GET, moreUrl, new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Toast.makeText(context, "加载更多数据成功", 1).show();
						NewsCenterTagBean parseJson = parseJson(responseInfo.result);
						listNews.addAll(parseJson.data.news);
						listAdapter.notifyDataSetChanged();
						lv_listnews.onFreshStateFinish();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(context, "加载更多数据失败", 1).show();
						lv_listnews.onFreshStateFinish();
					}
				});
			}
		}
		
	}

	@Override
	public void initData() {
		getDataFromNet(detailUrl);

	}

	private class MyPageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return topnews.size();
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
			NewsCenterDataTopnews newsCenterDataTopnews = topnews.get(position);
			ImageView iv = new ImageView(context);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setImageResource(R.drawable.news_pic_default);
			iv.setOnTouchListener(new IVtouchListener());
			container.addView(iv);
			
			bu.display(iv, newsCenterDataTopnews.topimage);
			return iv;
		}

	}
	
	private class IVtouchListener implements OnTouchListener{

		private int downx;
		private int downy;
		private long currenttime;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downx = (int) event.getX();
				downy = (int) event.getY();
				myhandler.removeCallbacksAndMessages(null);
				currenttime = System.currentTimeMillis();
				break;
			case MotionEvent.ACTION_CANCEL:
				myhandler.removeCallbacksAndMessages(null);
				myhandler.postDelayed(new MyRunnable(), 3000);
				break;
			case MotionEvent.ACTION_UP:
				int upx = (int) event.getX();
				int upy = (int) event.getY();
				if (upx == downx && upy == downy){
					long lasttime = System.currentTimeMillis();
					if (lasttime - currenttime < 100){
						System.out.println("点击处理");
					}
				}
				myhandler.removeCallbacksAndMessages(null);
				myhandler.postDelayed(new MyRunnable(), 3000);
				break;
			default:
				break;
			}
			return true;
		}
		
	}

	private void processData(String jsonData) {
		NewsCenterTagBean parseJson = parseJson(jsonData);
		topnews = parseJson.data.topnews;
		listNews = parseJson.data.news;
		
		
		if (myPageAdapter == null) {
			myPageAdapter = new MyPageAdapter();
			vp_topnews.setAdapter(myPageAdapter);
			vp_topnews.setOnPageChangeListener(new MyOnPageChangeListener());
		} else {
			myPageAdapter.notifyDataSetChanged();
		}
		
		ll_picpoints.removeAllViews();
		for (int i = 0; i < topnews.size(); i++){
			View view = new View(context);
			view.setBackgroundResource(R.drawable.topnews_point_selector);
			LayoutParams lp = new LayoutParams(5, 5);
			if (i != 0){
				lp.leftMargin = 10;
			}
			view.setLayoutParams(lp);
			view.setEnabled(false);
			ll_picpoints.addView(view);
		}
		previousIndex = 0;
		tv_picdec.setText(topnews.get(previousIndex).title);
		ll_picpoints.getChildAt(previousIndex).setEnabled(true);
		
		myhandler.removeCallbacksAndMessages(null);
		myhandler.postDelayed(new MyRunnable(), 2000);
		
		
		if (listAdapter == null){
			listAdapter = new MyListAdapter();
			lv_listnews.setAdapter(listAdapter);
		} else {
			listAdapter.notifyDataSetChanged();
		}
	}

	public NewsCenterTagBean parseJson(String jsonData) {
		Gson gson = new Gson();
		NewsCenterTagBean fromJson = gson.fromJson(jsonData,
				NewsCenterTagBean.class);
		moreUrl = fromJson.data.more;
		if (!TextUtils.isEmpty(moreUrl)) {
			moreUrl = Constants.URL + moreUrl;
		}
		return fromJson;
	}
	
	private class MyListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listNews.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			/*if (position == 0) {
				return convertView;
			}*/
			if (convertView == null){
				convertView = View.inflate(context, R.layout.item_newslist_view, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_listitem_pic);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_listitem_time);
				holder.tv_title = (TextView) convertView.findViewById(R.id.tv_listitem_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			NewsCenterDataNews newsCenterDataNews = listNews.get(position);
			bu.display(holder.iv_icon, newsCenterDataNews.listimage);
			holder.tv_time.setText(newsCenterDataNews.pubdate);
			holder.tv_title.setText(newsCenterDataNews.title);
			
			String ids = CacheUtils.getString(context, Constants.READIDS, "");
			if (!TextUtils.isEmpty(ids)) {
				if (ids.contains(newsCenterDataNews.id)) {
					holder.tv_title.setTextColor(Color.GRAY);
				} else {
					holder.tv_title.setTextColor(Color.BLACK);
				}
			} else {
				holder.tv_title.setTextColor(Color.BLACK);
			}
			
			return convertView;
		}
		
	}
	
	private class ViewHolder{
		ImageView iv_icon;
		TextView tv_title;
		TextView tv_time;
	}
	private class MyRunnable implements Runnable{

		@Override
		public void run() {
			myhandler.obtainMessage().sendToTarget();
		}
		
	}
	private Handler myhandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int nextPosition = (vp_topnews.getCurrentItem() + 1) % topnews.size();
			vp_topnews.setCurrentItem(nextPosition);
			postDelayed(new MyRunnable(), 2000);
		};
	};
	
	private class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int position) {
			ll_picpoints.getChildAt(previousIndex).setEnabled(false);
			ll_picpoints.getChildAt(position).setEnabled(true);
			previousIndex = position;
			String title = topnews.get(position).title;
			tv_picdec.setText(title);
		}
		
	}

	private void getDataFromNet(final String url) {
		String jsonData = CacheUtils.getString(context, url, "");
		if (!TextUtils.isEmpty(jsonData)) {
			processData(jsonData);
		}

		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String jsonData = responseInfo.result;

				CacheUtils.putString(context, url, jsonData);
				processData(jsonData);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(context, "网络无响应", 1).show();
			}
		});
	}

}
