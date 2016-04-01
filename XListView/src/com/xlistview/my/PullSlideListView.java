package com.xlistview.my;

import android.view.View;
import android.widget.AdapterView;

import com.xlistview.slide.SlideListView;
import com.xlistview.slide.SlideListView.OnSlideItemClickListener;
import com.xlistview.slide.SlideListView.OnSlideItemLongClickListener;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年04月01日
 * 
 *      记得给我个star哦~
 * 
 */
public class PullSlideListView extends PullListView {

	/**
	 * 获取ListView
	 * 
	 * @return
	 */
	public SlideListView getListView() {
		return super.getListView();
	}

	// **********************************************************************************
	// 设置监听事件

	@Override
	public void setOnItemClickListener(
			final OnListItemClickListener itemListener) {
		getListView().setOnSlideItemClickListener(
				new OnSlideItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						itemListener.onItemClick(parent, view, position, id);
					}
				});
	}

	@Override
	public void setOnItemLongClickListener(
			final OnListItemLongClickListener itemListener) {
		getListView().setOnSlideItemLongClickListener(
				new OnSlideItemLongClickListener() {
					@Override
					public void onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						itemListener
								.onItemLongClick(parent, view, position, id);
					}
				});
	}

}
