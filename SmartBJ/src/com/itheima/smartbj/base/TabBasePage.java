package com.itheima.smartbj.base;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itheima.smartbj.MainActivity;
import com.itheima.smartbj.R;


public class TabBasePage {
	public Context context;
	private View root;

	
	public TextView tv_title;

	
	public FrameLayout fl_content;

	
	public ImageButton ib_menu;
	
	public ImageButton ib_listOrGrid;

	public TabBasePage(final Context context) {
		this.context = context;
		root = initView();
		initEvent();
	}

	private View initView() {
		View view = View.inflate(context, R.layout.tab_base_page, null);
		tv_title = (TextView) view.findViewById(R.id.tv_title_bar_title);
		fl_content = (FrameLayout) view.findViewById(R.id.fl_tab_base_page_content);
		ib_menu = (ImageButton) view.findViewById(R.id.ib_title_bar_menu);
		ib_listOrGrid = (ImageButton) view.findViewById(R.id.ib_list_grid_button);
		return view;
	}
	
	private void initEvent(){
		ib_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity ma = (MainActivity) context;
				ma.getSlidingMenu().toggle();				
			}
		});
	}

	public View getRootView() {
		return root;
	}

	/**
	 * 需子类完成数据初始化
	 */
	public void initData() {
		
	}

	
	
}
