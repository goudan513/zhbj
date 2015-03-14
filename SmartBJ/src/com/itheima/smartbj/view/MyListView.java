package com.itheima.smartbj.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itheima.smartbj.R;

public class MyListView extends ListView {
	private View lunboView;
	private LinearLayout pullDownHead;
	private ImageView iv_arraw;
	private ProgressBar pb_loading;
	private TextView tv_state;
	private TextView tv_time;
	private int pullDownHeadHeight;
	private RotateAnimation raUp;
	private RotateAnimation raDown;
	private int mlistviewy = -1;
	private int downY = -1;

	private final int PULLDOWNREFRESH = 1;// 下拉刷新
	private final int RELEASEREFRESH = 2;// 松开刷新
	private final int REFRESHING = 3;// 正在刷新
	private int state = PULLDOWNREFRESH;
	private boolean isLoadingMore = false;

	private OnStateUpdate onStateUpdate;
	private View footer;
	private int footerHeight;
	private LinearLayout head;

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPullDownHeadView();
		initLoadingMoreView();
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPullDownHeadView();
		initLoadingMoreView();
	}

	public MyListView(Context context) {
		super(context);
		initPullDownHeadView();
		initLoadingMoreView();
	}

	private void initLoadingMoreView() {
		footer = View.inflate(getContext(), R.layout.lodingmore, null);
		footer.measure(0, 0);
		footerHeight = footer.getMeasuredHeight();
		footer.setPadding(0, -footerHeight, 0, 0);
		addFooterView(footer);
		setOnScrollListener(new MyOnScrollListener());

	}

	private class MyOnScrollListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					|| scrollState == OnScrollListener.SCROLL_STATE_FLING) {
				if (getLastVisiblePosition() == getCount() - 1
						&& !isLoadingMore) {
					isLoadingMore = true;
					footer.setPadding(0, 0, 0, 0);
					setSelection(getCount());
					if (onStateUpdate != null) {
						onStateUpdate.loadMore();
					}
				}
			}
		}

	}

	private void initPullDownHeadView() {
		head = (LinearLayout) View.inflate(getContext(), R.layout.refreshlist_head, null);
		pullDownHead = (LinearLayout) head
				.findViewById(R.id.ll_refreshlist_pulldownheader);
		iv_arraw = (ImageView) head.findViewById(R.id.iv_head_arraw);
		pb_loading = (ProgressBar) head.findViewById(R.id.pb_head_loading);
		tv_state = (TextView) head.findViewById(R.id.tv_refreshhead_state);
		tv_time = (TextView) head.findViewById(R.id.tv_refreshhead_time);

		pullDownHead.measure(0, 0);
		pullDownHeadHeight = pullDownHead.getMeasuredHeight();

		pullDownHead.setPadding(0, -pullDownHeadHeight, 0, 0);

		addHeaderView(head);

		initAnimation();
	}

	private void initAnimation() {
		raUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		raUp.setDuration(500);
		raUp.setFillAfter(true);

		raDown = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		raDown.setDuration(500);
		raDown.setFillAfter(true);
	}

	public void addListItemHeadView(View view) {
		lunboView = view;
		head.addView(view);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (downY == -1) {
				downY = (int) ev.getY();
			}
			
			if (state == REFRESHING){
				break;
			}
			if (lunboView != null) {
				int[] positions = new int[2];
				if (mlistviewy == -1) {
					this.getLocationOnScreen(positions);
					mlistviewy = positions[1];
				}

				lunboView.getLocationOnScreen(positions);

				if (positions[1] < mlistviewy) {
					// 轮播图没有完全显示
					break;
				}
			}

			int moveY = (int) ev.getY();
			int dy = moveY - downY;

			if (dy > 0 && getFirstVisiblePosition() == 0) {
				int padingTop = -pullDownHeadHeight + dy;
				if (padingTop < 0 && state != PULLDOWNREFRESH) {

					state = PULLDOWNREFRESH;
					refreshPullDownState();
				} else if (padingTop > 0 && state != RELEASEREFRESH) {
					state = RELEASEREFRESH;
					refreshPullDownState();
				}
				pullDownHead.setPadding(0, padingTop, 0, 0);
				return true;
			}

			break;
		case MotionEvent.ACTION_UP:
			downY = -1;
			if (state == PULLDOWNREFRESH) {
				pullDownHead.setPadding(0, -pullDownHeadHeight, 0, 0);
			} else if (state == RELEASEREFRESH) {
				state = REFRESHING;
				refreshPullDownState();
				pullDownHead.setPadding(0, 0, 0, 0);
				onStateUpdate.pulldown();
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	public void setOnStateUpdate(OnStateUpdate onstateUpdate) {
		this.onStateUpdate = onstateUpdate;
	}

	public interface OnStateUpdate {
		public void pulldown();

		public void loadMore();
	}

	public void onFreshStateFinish() {
		if (isLoadingMore) {
			isLoadingMore = false;
			footer.setPadding(0, -footerHeight, 0, 0);
		} else {
			iv_arraw.setVisibility(View.VISIBLE);
			pb_loading.setVisibility(View.INVISIBLE);
			tv_state.setText("下拉刷新");
			state = PULLDOWNREFRESH;
			tv_time.setText("最后刷新时间：" + getCurrentTime());
			pullDownHead.setPadding(0, -pullDownHeadHeight, 0, 0);
		}
	}

	private String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	private void refreshPullDownState() {
		switch (state) {
		case PULLDOWNREFRESH:
			iv_arraw.startAnimation(raDown);
			tv_state.setText("下拉刷新");
			break;
		case RELEASEREFRESH:
			iv_arraw.startAnimation(raUp);
			tv_state.setText("松开刷新");
			break;
		case REFRESHING:
			iv_arraw.clearAnimation();
			iv_arraw.setVisibility(View.INVISIBLE);
			pb_loading.setVisibility(View.VISIBLE);
			tv_state.setText("正在刷新中。。。。");
			break;
		default:
			break;
		}
	}

}
