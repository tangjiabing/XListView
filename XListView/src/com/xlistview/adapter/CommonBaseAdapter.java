package com.xlistview.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年04月01日
 * 
 *      记得给我个star哦~
 * 
 */
public abstract class CommonBaseAdapter<T> extends BaseAdapter {

	private Context mContext = null;
	private ArrayList<T> mDataList = null;
	private int mLayoutId = 0;

	public CommonBaseAdapter(Context context, ArrayList<T> dataList,
			int layoutId) {
		mContext = context;
		mDataList = dataList;
		mLayoutId = layoutId;
	}

	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.getInstance(mContext, mLayoutId,
				convertView);
		T bean = mDataList.get(position);
		convert(holder, bean, position);
		return holder.getConvertView();
	}

	public abstract void convert(ViewHolder holder, T bean, int position);

}
