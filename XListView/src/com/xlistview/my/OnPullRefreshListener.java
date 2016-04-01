package com.xlistview.my;

import android.view.View;

import com.xlistview.pullrefresh.PullToRefreshBase;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年04月01日
 * 
 *      记得给我个star哦~
 * 
 */
public interface OnPullRefreshListener<T extends View> {
	/**
	 * 下拉松手后会被调用
	 * 
	 * @param refreshView
	 *            刷新的View
	 */
	public void onPullDownToRefresh(PullToRefreshBase<T> refreshView);

	/**
	 * 加载更多时会被调用或上拉时调用
	 * 
	 * @param refreshView
	 *            刷新的View
	 */
	public void onPullUpToRefresh(PullToRefreshBase<T> refreshView);
}
