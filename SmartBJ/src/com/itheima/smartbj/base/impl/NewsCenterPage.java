package com.itheima.smartbj.base.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itheima.smartbj.MainActivity;
import com.itheima.smartbj.base.NewsCenterMenuBasePager;
import com.itheima.smartbj.base.TabBasePage;
import com.itheima.smartbj.domain.NewsMessage;
import com.itheima.smartbj.domain.NewsMessage.Data;
import com.itheima.smartbj.newscentermenu.impl.InteractMenuPage;
import com.itheima.smartbj.newscentermenu.impl.NewsMenuPage;
import com.itheima.smartbj.newscentermenu.impl.PhotosMenuPage;
import com.itheima.smartbj.newscentermenu.impl.TopicMenuPage;
import com.itheima.smartbj.util.CacheUtils;
import com.itheima.smartbj.util.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NewsCenterPage extends TabBasePage {
	private List<NewsCenterMenuBasePager> newsPages;
	private List<NewsMessage.Data> datas;
	
	public NewsCenterPage(Context context) {
		super(context);
	}
	
	@Override
	public void initData() {
		tv_title.setText("新闻");
		ib_menu.setVisibility(View.VISIBLE);
		
		getDatafromNet();
	}
	
	private void processData(String jsonData){
		Gson gson = new Gson();
		NewsMessage newsData = gson.fromJson(jsonData, NewsMessage.class);
		
		newsPages = new ArrayList<NewsCenterMenuBasePager>();
		Data data = newsData.data.get(0);
		
		newsPages.add(new NewsMenuPage(context,data));
		newsPages.add(new TopicMenuPage(context));
		newsPages.add(new PhotosMenuPage(context));
		newsPages.add(new InteractMenuPage(context));
		
		datas = newsData.data;
		MainActivity ma = (MainActivity) context;
		ma.getLeft().setData(datas);
	}
	
	public void switchPage(int position){
		NewsCenterMenuBasePager pager = newsPages.get(position);
		View root = pager.getRoot();
		fl_content.removeAllViews();
		fl_content.addView(root);
		pager.initData();
		tv_title.setText(datas.get(position).title);
		
		if (position == 2){
			ib_listOrGrid.setTag(pager);
			ib_listOrGrid.setVisibility(View.VISIBLE);
			ib_listOrGrid.setOnClickListener(new MyOnClickListener());
		} else {
			ib_listOrGrid.setVisibility(View.GONE);
		}
	}
	
	private class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			PhotosMenuPage pmp = (PhotosMenuPage) v.getTag();
			pmp.switchListOrGrid((ImageButton) v);
		}
		
	}

	private void getDatafromNet() {
		String jsonData = CacheUtils.getString(context, Constants.NEWSURL, "");
		if (!TextUtils.isEmpty(jsonData)){
			processData(jsonData);
		}
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, Constants.NEWSURL, new RequestCallBack<String>() {

			/**
			 * 成功
			 */
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String jsonData = responseInfo.result;
				processData(jsonData);
				CacheUtils.putString(context, Constants.NEWSURL, jsonData);
			}
			
			/**
			 * 失败
			 */
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(context, "没有网络", 1).show();
			}
			
		});
	}

}
