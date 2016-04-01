package com.xlistview.my;

import android.view.View;
import android.widget.AdapterView;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年04月01日
 * 
 *      记得给我个star哦~
 * 
 */
public interface OnListItemClickListener {
	/**
	 * 点击列表项
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id);
}
