package com.itheima.smartbj.domain;

import java.util.List;

public class NewsMessage {
	public List<Data> data;
	public List<String> extend;
	public String retcode;
	
	public class Data{
		public List<Children> children;
		public String id;
		public String title;
		public String type;
		public String url;
		public String url1;
		public String dayurl;
		public String excurl;
		public String weekurl;
		
		public class Children{
			public String id;
			public String title;
			public String type;
			public String url;
		}
	}
}
