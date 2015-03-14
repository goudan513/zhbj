package com.itheima.smartbj.domain;

import java.util.List;

public class PhotoMessage {
	public String retcode;
	public PhotoMessageData data;
	public class PhotoMessageData{
		public String countcommenturl;
		public String more;
		public String title;
		public List<PhotoMessageDataNews> news;
		public List topic;
		public class PhotoMessageDataNews{
			public String comment;
			public String commentlist;
			public String commenturl;
			public String id;
			public String largeimage;
			public String listimage;
			public String pubdate;
			public String smallimage;
			public String title;
			public String type;
			public String url;
		}
	}
}
