package com.itheima.smartbj.base;

import android.content.Context;
import android.view.View;

public abstract class NewsCenterMenuBasePager {
	public Context context;
	private View root;
	public NewsCenterMenuBasePager(Context context){
		this.context  = context;
		root = initView();
	}
	
	public View getRoot(){
		return root;
	}
	public abstract View initView();
	
	public void initData(){
		
	}
	
}
