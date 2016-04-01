package com.xlistview.my;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.xlistview.pullrefresh.LoadingLayout;
import com.xlistview.pullrefresh.PullToRefreshBase;
import com.xlistview.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.xlistview.pullrefresh.PullToRefreshScrollView;
import com.xlistview.res.ResUtil;
import com.xlistview.res.StringRes;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年04月01日
 * 
 *      记得给我个star哦~
 * 
 */
public class PullScrollView {

	private SimpleDateFormat mDateFormat = null;
	private PullToRefreshScrollView mPTRScrollView = null;
	private ScrollView mScrollView = null;
	private LinearLayout mLinearLayout = null;

	public PullScrollView() {
		mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	}

	public void setView(PullToRefreshScrollView pullScrollView,
			boolean isPullRefreshEnabled, boolean isPullLoadEnabled) {
		mPTRScrollView = pullScrollView;
		mPTRScrollView.setPullRefreshEnabled(isPullRefreshEnabled);
		mPTRScrollView.setPullLoadEnabled(isPullLoadEnabled);
		mScrollView = mPTRScrollView.getRefreshableView();
		Context context = mPTRScrollView.getContext();
		mLinearLayout = new LinearLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mLinearLayout.setLayoutParams(params);
		mScrollView.addView(mLinearLayout);

		ResUtil resUtil = ResUtil.getInstance(context);

		String headerPullLabel = context.getString(resUtil
				.getIdFromString(StringRes.pull_to_refresh_header_hint_normal));
		String footerPullLabel = context.getString(resUtil
				.getIdFromString(StringRes.pull_to_refresh_footer_hint_normal));

		String headerRefreshingLabel = context
				.getString(resUtil
						.getIdFromString(StringRes.pull_to_refresh_header_hint_loading));
		String footerRefreshingLabel = context
				.getString(resUtil
						.getIdFromString(StringRes.pull_to_refresh_footer_hint_loading));

		String headerReleaseLabel = context.getString(resUtil
				.getIdFromString(StringRes.pull_to_refresh_header_hint_ready));
		String footerReleaseLabel = context.getString(resUtil
				.getIdFromString(StringRes.pull_to_refresh_footer_hint_ready));

		LoadingLayout headerLayout = mPTRScrollView.getHeaderLoadingLayout();
		LoadingLayout footerLayout = mPTRScrollView.getFooterLoadingLayout();

		headerLayout.setPullLabel(headerPullLabel);
		footerLayout.setPullLabel(footerPullLabel);

		headerLayout.setRefreshingLabel(headerRefreshingLabel);
		footerLayout.setRefreshingLabel(footerRefreshingLabel);

		headerLayout.setReleaseLabel(headerReleaseLabel);
		footerLayout.setReleaseLabel(footerReleaseLabel);

		headerLayout.setIsHeader(true);
		footerLayout.setIsHeader(false);
	}

	/**
	 * 获取ScrollView中的子控件LinearLayout
	 * 
	 * @return
	 */
	public LinearLayout getLinearLayout() {
		return mLinearLayout;
	}

	// *****************************************************************************
	// 关于刷新的一些操作

	/**
	 * 开始刷新，通常用于调用者主动刷新，典型的情况是进入界面，开始主动刷新，这个刷新并不是由用户拉动引起的
	 * 
	 * @param delayMillis
	 *            延迟时间，单位是毫秒
	 */
	public void doPullRrefreshing(long delayMillis) {
		mPTRScrollView.doPullRefreshing(true, delayMillis);
	}

	/**
	 * 结束下拉刷新
	 */
	public void onPullDownRefreshComplete() {
		if (mPTRScrollView.isPullRefreshing())
			mPTRScrollView.onPullDownRefreshComplete();
	}

	/**
	 * 结束上拉加载更多
	 */
	public void onPullUpRefreshComplete() {
		if (mPTRScrollView.isPullLoading())
			mPTRScrollView.onPullUpRefreshComplete();
	}

	/**
	 * 结束下拉刷新、上拉加载的动画
	 */
	public void onPullRefreshComplete() {
		onPullDownRefreshComplete();
		onPullUpRefreshComplete();
	}

	/**
	 * 最近更新时间
	 */
	public void setLastUpdateTime() {
		String time = mDateFormat.format(new Date(System.currentTimeMillis()));
		mPTRScrollView.setLastUpdatedLabel(time);
	}

	// *****************************************************************************
	// 设置监听事件

	/**
	 * 设置刷新事件监听器
	 * 
	 * @param refreshListener
	 */
	public void setOnRefreshListener(final OnPullRefreshListener refreshListener) {
		mPTRScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						refreshListener.onPullDownToRefresh(refreshView);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						refreshListener.onPullUpToRefresh(refreshView);
					}

				});
	}

}
