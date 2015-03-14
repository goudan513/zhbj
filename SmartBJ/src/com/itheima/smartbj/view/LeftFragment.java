package com.itheima.smartbj.view;

import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.smartbj.MainActivity;
import com.itheima.smartbj.R;
import com.itheima.smartbj.base.BaseFragment;
import com.itheima.smartbj.base.impl.NewsCenterPage;
import com.itheima.smartbj.domain.NewsMessage;

public class LeftFragment extends BaseFragment {

	private ListView lv;
	private List<NewsMessage.Data> datas;
	private int currentPosition;
	private MenuAdapter ma;
	@Override
	public View initView() {
		lv = new ListView(mActivity);
		lv.setBackgroundColor(Color.BLACK);
		lv.setCacheColorHint(Color.TRANSPARENT);
		lv.setDividerHeight(0);
		lv.setPadding(0, 40, 0, 0);
		lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		lv.setOnItemClickListener(new MyOnItemClickListener());
		return lv;
	}
	private class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			currentPosition = position;
			ma.notifyDataSetChanged();
			switchNewsCenterPage();
			((MainActivity)mActivity).getSlidingMenu().toggle();
		}
		
	}
	public void setData(List<NewsMessage.Data> datas){
		this.datas = datas;
		ma = new MenuAdapter();
		currentPosition = 0;
		lv.setAdapter(ma);
		
		switchNewsCenterPage();
	}
	
	private void switchNewsCenterPage() {
		MainActivity ma = (MainActivity) mActivity;
		NewsCenterPage newsCenterPage = ma.getContent().getNewsCenterPage();
		newsCenterPage.switchPage(currentPosition);
	}

	private class MenuAdapter extends BaseAdapter{

		

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = null;
			if (convertView == null){
				tv = (TextView) View.inflate(mActivity, R.layout.item_left_menu, null);
			} else {
				tv = (TextView) convertView;
			}
			tv.setText(datas.get(position).title);
			tv.setEnabled(position == currentPosition );
			return tv;
		}
		
	}

}
