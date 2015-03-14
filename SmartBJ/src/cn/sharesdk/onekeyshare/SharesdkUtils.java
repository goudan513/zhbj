package cn.sharesdk.onekeyshare;

import com.itheima.smartbj.R;

import cn.sharesdk.framework.ShareSDK;
import android.content.Context;

public class SharesdkUtils {
	public static void share(Context context) {

		ShareSDK.initSDK(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.ic_launcher,
				context.getString(R.string.app_name));
		
		// text是分享文本，所有平台都需要这个字段
		oks.setText("测试一键分享程序，吼吼~~~~~~");
		/*
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
		*/
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("便宜。。哈哈");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(context.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(context);

	}
}
