package com.xlistview.pullrefresh;

import com.xlistview.res.LayoutRes;
import com.xlistview.res.ResUtil;
import com.xlistview.res.StringRes;
import com.xlistview.res.ViewRes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 这个类封装了下拉刷新的布局
 * 
 * @author Li Hong
 * @since 2013-7-30
 */
public class FooterLoadingLayout extends LoadingLayout {
	/** 进度条 */
	private ProgressBar mProgressBar;
	/** 显示的文本 */
	private TextView mHintView;

	private ResUtil mResUtil = null;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 */
	public FooterLoadingLayout(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public FooterLoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 *            context
	 */
	private void init(Context context) {
		mResUtil = ResUtil.getInstance(context);

		mProgressBar = (ProgressBar) findViewById(mResUtil
				.getIdFromView(ViewRes.pull_to_load_footer_progressbar));
		mHintView = (TextView) findViewById(mResUtil
				.getIdFromView(ViewRes.pull_to_load_footer_hint_textview));

		setState(State.RESET);
	}

	@Override
	protected View createLoadingView(Context context, AttributeSet attrs) {
		if (mResUtil == null)
			mResUtil = ResUtil.getInstance(context);
		View container = LayoutInflater
				.from(context)
				.inflate(
						mResUtil.getIdFromLayout(LayoutRes.xlistview_pull_to_load_footer),
						null);
		return container;
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {
	}

	@Override
	public int getContentSize() {
		View view = findViewById(mResUtil
				.getIdFromView(ViewRes.pull_to_load_footer_content));
		if (null != view) {
			return view.getHeight();
		}

		return (int) (getResources().getDisplayMetrics().density * 40);
	}

	@Override
	protected void onStateChanged(State curState, State oldState) {
		mProgressBar.setVisibility(View.GONE);
		mHintView.setVisibility(View.INVISIBLE);

		super.onStateChanged(curState, oldState);
	}

	@Override
	protected void onReset() {
		mHintView
				.setText(mResUtil
						.getIdFromString(StringRes.pull_to_refresh_header_hint_loading));
	}

	@Override
	protected void onPullToRefresh() {
		mHintView.setVisibility(View.VISIBLE);
		mHintView.setText(mResUtil
				.getIdFromString(StringRes.pull_to_refresh_footer_hint_normal));
	}

	@Override
	protected void onReleaseToRefresh() {
		mHintView.setVisibility(View.VISIBLE);
		mHintView.setText(mResUtil
				.getIdFromString(StringRes.pull_to_refresh_header_hint_ready));
	}

	@Override
	protected void onRefreshing() {
		mProgressBar.setVisibility(View.VISIBLE);
		mHintView.setVisibility(View.VISIBLE);
		mHintView
				.setText(mResUtil
						.getIdFromString(StringRes.pull_to_refresh_header_hint_loading));
	}

	@Override
	protected void onNoMoreData() {
		mHintView.setVisibility(View.VISIBLE);
		mHintView.setText(mResUtil
				.getIdFromString(StringRes.pushmsg_center_no_more_msg));
	}

}
