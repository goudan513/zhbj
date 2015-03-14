package com.itheima.smartbj.newscentermenu.impl;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itheima.smartbj.R;
import com.itheima.smartbj.base.NewsCenterMenuBasePager;
import com.itheima.smartbj.domain.PhotoMessage;
import com.itheima.smartbj.domain.PhotoMessage.PhotoMessageData.PhotoMessageDataNews;
import com.itheima.smartbj.util.CacheUtils;
import com.itheima.smartbj.util.Constants;
import com.itheima.smartbj.util.ImageCacheUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PhotosMenuPage extends NewsCenterMenuBasePager {

	@ViewInject(R.id.lv_photos)
	private ListView lv_photos;

	@ViewInject(R.id.gv_photos)
	private GridView gv_photos;

	private boolean isList = true;
	private ImageCacheUtils imageCacheUtils;

	private List<PhotoMessageDataNews> photoNews;
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ImageCacheUtils.SUCCESS:
				int tag = msg.arg1;
				Bitmap bm = (Bitmap) msg.obj;
				ImageView iv = (ImageView) lv_photos.findViewWithTag(tag);
				
				if (iv != null) {
					iv.setImageBitmap(bm);
				}
				
				break;
			case ImageCacheUtils.FAILURE:
				Toast.makeText(context, "Õº∆¨Õ¯¬Á«Î«Û ß∞‹", 1).show();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	public PhotosMenuPage(Context context) {
		super(context);
		imageCacheUtils = new ImageCacheUtils(context, new MyHandler());
	}

	@Override
	public View initView() {
		View root = View.inflate(context, R.layout.photos, null);
		ViewUtils.inject(this, root);
		return root;
	}
	
	private PhotoMessage parseJson(String jsonData){
		Gson gson = new Gson();
		PhotoMessage photoMessage = gson.fromJson(jsonData, PhotoMessage.class);
		return photoMessage;
	}
	
	private void processData(String jsonData){
		PhotoMessage pm = parseJson(jsonData);
		photoNews = pm.data.news;
		
		MyAdapter adapter = new MyAdapter();
		lv_photos.setAdapter(adapter);
		gv_photos.setAdapter(adapter);
	}
	
	private class PhotoHolder {
		ImageView iv_photoitem;
		TextView tv_photoitem;
	}
	private class MyAdapter extends BaseAdapter{

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PhotoHolder holder = null;
			if (convertView == null){
				convertView = View.inflate(context, R.layout.photoitems, null);
				
				holder = new PhotoHolder();
				holder.iv_photoitem = (ImageView) convertView.findViewById(R.id.iv_photoitem);
				holder.tv_photoitem = (TextView) convertView.findViewById(R.id.tv_photoitem);
				
				convertView.setTag(holder);
			} else {
				holder = (PhotoHolder) convertView.getTag();
			}
			PhotoMessageDataNews photoMessageDataNews = photoNews.get(position);
			
			holder.tv_photoitem.setText(photoMessageDataNews.title);
			holder.iv_photoitem.setBackgroundResource(R.drawable.pic_item_list_default);
			holder.iv_photoitem.setTag(position);
			
			Bitmap bitmap = imageCacheUtils.getBitmap(photoMessageDataNews.listimage, position);
			if (bitmap != null) {
				holder.iv_photoitem.setImageBitmap(bitmap);
			}
			return convertView;
		}
		
		@Override
		public int getCount() {
			return photoNews.size();
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

		
	}

	@Override
	public void initData() {
		String jsonData = CacheUtils.getString(context, Constants.PHOTOS_URL, "");
		if (!TextUtils.isEmpty(jsonData)){
			processData(jsonData);
		}
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET,Constants.PHOTOS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String jsonData = responseInfo.result;
				CacheUtils.putString(context, Constants.PHOTOS_URL, jsonData);
				processData(jsonData);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(context, "Õº∆¨«Î«Û ß∞‹", 1).show();
			}
		});
	}

	public void switchListOrGrid(ImageButton v) {
		if (isList) {
			isList = false;
			v.setImageResource(R.drawable.icon_pic_list_type);
			gv_photos.setVisibility(View.VISIBLE);
			lv_photos.setVisibility(View.INVISIBLE);
		} else {
			isList = true;
			v.setImageResource(R.drawable.icon_pic_grid_type);
			gv_photos.setVisibility(View.INVISIBLE);
			lv_photos.setVisibility(View.VISIBLE);
		}
	}

}
