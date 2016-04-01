package com.xlistview.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年04月01日
 * 
 *      记得给我个star哦~
 * 
 */
public class ViewHolder {

	private View mConvertView = null;
	private SparseArray<View> mViewArray = null;

	private ViewHolder(Context context, int layoutId) {
		mViewArray = new SparseArray<View>();
		LayoutInflater inflater = LayoutInflater.from(context);
		mConvertView = inflater.inflate(layoutId, null);
		mConvertView.setTag(this);
	}

	// ********************************************************************
	// 保护方法

	protected static ViewHolder getInstance(Context context, int layoutId,
			View convertView) {
		if (convertView == null)
			return new ViewHolder(context, layoutId);
		else
			return (ViewHolder) convertView.getTag();
	}

	// ********************************************************************
	// 公有方法

	public <T extends View> T getView(int viewId) {
		View view = mViewArray.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViewArray.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}

}
