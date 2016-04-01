package com.xlistview.my;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.xlistview.pullrefresh.LoadingLayout;
import com.xlistview.pullrefresh.PullToRefreshBase;
import com.xlistview.pullrefresh.PullToRefreshBase.OnRefreshListener;
import com.xlistview.pullrefresh.PullToRefreshListView;
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
public class PullListView {

	private SimpleDateFormat mDateFormat = null;
	private PullToRefreshListView mPTRListView = null;
	private ListView mListView = null;
	private BaseAdapter mAdapter = null;

	public PullListView() {
		mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	}

	/**
	 * 设置View
	 * 
	 * @param pullListView
	 * @param adapter
	 * @param isPullRefreshEnabled
	 *            设置下拉刷新是否可用，false为不可用，true为可用
	 * @param isPullLoadEnabled
	 *            设置上拉加载是否可用，false为不可用，true为可用
	 */
	public void setView(PullToRefreshListView pullListView,
			BaseAdapter adapter, boolean isPullRefreshEnabled,
			boolean isPullLoadEnabled) {
		setView(pullListView, null, null, adapter, isPullRefreshEnabled,
				isPullLoadEnabled);
	}

	/**
	 * 设置View
	 * 
	 * @param pullListView
	 * @param header
	 * @param footer
	 * @param adapter
	 * @param isPullRefreshEnabled
	 *            设置下拉刷新是否可用，false为不可用，true为可用
	 * @param isPullLoadEnabled
	 *            设置上拉加载是否可用，false为不可用，true为可用
	 */
	public void setView(PullToRefreshListView pullListView, View header,
			View footer, BaseAdapter adapter, boolean isPullRefreshEnabled,
			boolean isPullLoadEnabled) {
		mPTRListView = pullListView;
		mAdapter = adapter;
		mListView = mPTRListView.getRefreshableView();
		if (header != null)
			mListView.addHeaderView(header);
		if (footer != null)
			mListView.addFooterView(footer);
		mListView.setAdapter(adapter);
		mPTRListView.setPullRefreshEnabled(isPullRefreshEnabled);
		mPTRListView.setPullLoadEnabled(isPullLoadEnabled);

		Context context = mPTRListView.getContext();
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

		LoadingLayout headerLayout = mPTRListView.getHeaderLoadingLayout();
		LoadingLayout footerLayout = mPTRListView.getFooterLoadingLayout();

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
	 * 获取ListView
	 * 
	 * @return
	 */
	public <T extends ListView> T getListView() {
		return (T) mListView;
	}

	// ***************************************************************************
	// 关于刷新的一些操作

	/**
	 * 开始刷新，通常用于调用者主动刷新，典型的情况是进入界面，开始主动刷新，这个刷新并不是由用户拉动引起的
	 * 
	 * @param delayMillis
	 *            延迟时间，单位是毫秒
	 */
	public void doPullRrefreshing(long delayMillis) {
		mPTRListView.doPullRefreshing(true, delayMillis);
	}

	/**
	 * 结束下拉刷新
	 */
	public void onPullDownRefreshComplete() {
		if (mPTRListView.isPullRefreshing())
			mPTRListView.onPullDownRefreshComplete();
	}

	/**
	 * 结束上拉加载更多
	 */
	public void onPullUpRefreshComplete() {
		if (mPTRListView.isPullLoading())
			mPTRListView.onPullUpRefreshComplete();
	}

	/**
	 * 结束下拉刷新、上拉加载的动画
	 */
	public void onPullRefreshComplete() {
		onPullDownRefreshComplete();
		onPullUpRefreshComplete();
	}

	/**
	 * Adapter数据发生改变，更新界面列表，结束下拉刷新、上拉加载的动画，并更新最近刷新时间
	 * 
	 */
	public void notifyDataSetChanged() {
		mAdapter.notifyDataSetChanged();
		onPullDownRefreshComplete();
		onPullUpRefreshComplete();
		setLastUpdateTime();
	}

	/**
	 * 上拉加载完后，平滑连接上一页最后的item
	 */
	public void smoothConnectLastItem() {
		int position = mListView.getFirstVisiblePosition() + 2;
		mListView.setSelection(position);
		mListView.smoothScrollToPosition(position);
	}

	/**
	 * 最近更新时间
	 */
	public void setLastUpdateTime() {
		String time = mDateFormat.format(new Date(System.currentTimeMillis()));
		mPTRListView.setLastUpdatedLabel(time);
	}

	// ***************************************************************************
	// 设置监听事件

	/**
	 * 设置刷新事件监听器
	 * 
	 * @param refreshListener
	 */
	public void setOnRefreshListener(final OnPullRefreshListener refreshListener) {
		mPTRListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				refreshListener.onPullDownToRefresh(refreshView);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				refreshListener.onPullUpToRefresh(refreshView);
			}

		});
	}

	/**
	 * 设置列表视图单击事件监听器
	 * 
	 * @param itemListener
	 */
	public void setOnItemClickListener(
			final OnListItemClickListener itemListener) {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemListener.onItemClick(parent, view, position, id);
			}
		});
	}

	/**
	 * 设置列表视图长按事件监听器
	 * 
	 * @param itemListener
	 */
	public void setOnItemLongClickListener(
			final OnListItemLongClickListener itemListener) {
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemListener.onItemLongClick(parent, view, position, id);
				return true;
			}
		});
	}

}
