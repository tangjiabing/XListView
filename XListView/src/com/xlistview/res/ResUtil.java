package com.xlistview.res;

import android.content.Context;
import android.content.res.Resources;

public class ResUtil {

	private static ResUtil mInstance = null;
	private static String mPackageName = null;
	private static Resources mResources = null;

	public static ResUtil getInstance(Context context) {
		if (mInstance == null)
			mInstance = new ResUtil(context);
		return mInstance;
	}

	private ResUtil(Context context) {
		mPackageName = context.getPackageName();
		mResources = context.getResources();
	}

	public int getIdFromDrawable(String name) {
		return mResources.getIdentifier(name, "drawable", mPackageName);
	}

	public int getIdFromString(String name) {
		return mResources.getIdentifier(name, "string", mPackageName);
	}

	public int getIdFromLayout(String name) {
		return mResources.getIdentifier(name, "layout", mPackageName);
	}

	public int getIdFromAnim(String name) {
		return mResources.getIdentifier(name, "anim", mPackageName);
	}

	public int getIdFromColor(String name) {
		return mResources.getIdentifier(name, "color", mPackageName);
	}

	public int getIdFromDimen(String name) {
		return mResources.getIdentifier(name, "dimen", mPackageName);
	}

	public int getIdFromAttr(String name) {
		return mResources.getIdentifier(name, "attr", mPackageName);
	}

	public int getIdFromStyleable(String name) {
		return mResources.getIdentifier(name, "styleable", mPackageName);
	}

	public int getIdFromView(String name) {
		return mResources.getIdentifier(name, "id", mPackageName);
	}

}
