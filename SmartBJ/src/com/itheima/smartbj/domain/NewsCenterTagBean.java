package com.itheima.smartbj.domain;

import java.util.List;

public class NewsCenterTagBean {
	public NewsCenterData data;
	public class NewsCenterData {
		public String countcommenturl;
		public String more;
		public String title;
		public List<NewsCenterDataNews> news;
		public List<NewsCenterDataTopic> topic;
		public List<NewsCenterDataTopnews> topnews;
		public class NewsCenterDataTopnews{
			public String comment;
			public String commentlist;
			public String commenturl;
			public String id;
			public String pubdate;
			public String title;
			public String topimage;
			public String type;
			public String url;
		}
		public class NewsCenterDataTopic{
			public String description;
			public String id;
			public String listimage;
			public String sort;
			public String title;
			public String url;
		}
		public class NewsCenterDataNews{
			public String comment;
			public String commentlist;
			public String commenturl;
			public String id;
			public String listimage;
			public String pubdate;
			public String title;
			public String type;
			public String url;
		}
	}
	public String retcode;
}
