package com.itheima.smartbj;

import cn.sharesdk.onekeyshare.SharesdkUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class NewsDetailActivity extends Activity implements
		View.OnClickListener {
	private String url;
	private WebSettings settings;

	private int currentTextSizeIndex = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_detail);
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		initView();
	}

	private void initView() {
		findViewById(R.id.ib_title_bar_back).setVisibility(View.VISIBLE);
		findViewById(R.id.ib_title_bar_textsize).setVisibility(View.VISIBLE);
		findViewById(R.id.ib_title_bar_share).setVisibility(View.VISIBLE);
		findViewById(R.id.tv_title_bar_title).setVisibility(View.GONE);
		findViewById(R.id.ib_title_bar_menu).setVisibility(View.GONE);

		findViewById(R.id.ib_title_bar_back).setOnClickListener(this);
		findViewById(R.id.ib_title_bar_textsize).setOnClickListener(this);
		findViewById(R.id.ib_title_bar_share).setOnClickListener(this);
		final ProgressBar pb = (ProgressBar) findViewById(R.id.pb_news_detail);
		WebView wv = (WebView) findViewById(R.id.wv_news_detail);
		settings = wv.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setUseWideViewPort(true);
		settings.setBuiltInZoomControls(true);
		wv.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				pb.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}

		});
		wv.loadUrl(url);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_title_bar_back:
			finish();
			break;
		case R.id.ib_title_bar_textsize:
			showChangeTextSizeDialog();
		case R.id.ib_title_bar_share:
			SharesdkUtils.share(this);
		default:
			break;
		}
	}

	private void showChangeTextSizeDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("选择字体大小");
		String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
				"超小号字体" };
		dialog.setSingleChoiceItems(items, currentTextSizeIndex,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						currentTextSizeIndex = which;
					}
				});
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switchTextSize();
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.show();
	}

	protected void switchTextSize() {
		switch (currentTextSizeIndex) {
		case 0:
			settings.setTextSize(TextSize.LARGEST);
			break;
		case 1:
			settings.setTextSize(TextSize.LARGER);
			break;
		case 2:
			settings.setTextSize(TextSize.NORMAL);
			break;
		case 3:
			settings.setTextSize(TextSize.SMALLER);
			break;
		case 4:
			settings.setTextSize(TextSize.SMALLEST);
			break;

		default:
			break;
		}
	}
}
