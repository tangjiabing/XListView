package com.xlistview;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

public class TestActivity extends Activity {

	private FragmentManager mFragmentManager = null;
	private Fragment mCurrentFragment = null;
	private TestFragment mTab1Fragment = null;
	private TestFragment mTab2Fragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		mFragmentManager = getFragmentManager();

		mTab1Fragment = new TestFragment();
		changeFragment(mTab1Fragment);
	}

	private void changeFragment(Fragment fragment) {
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		if (mCurrentFragment == null)
			transaction.replace(R.id.content, fragment);
		else {
			if (mCurrentFragment != fragment) {
				transaction.hide(mCurrentFragment);
				if (!fragment.isAdded())
					transaction.add(R.id.content, fragment);
				else
					transaction.show(fragment);
			}
		}
		mCurrentFragment = fragment;
		transaction.commit();
	}

	public void click1(View v) {
		if (mTab1Fragment == null)
			mTab1Fragment = new TestFragment();
		changeFragment(mTab1Fragment);
	}

	public void click2(View v) {
		if (mTab2Fragment == null)
			mTab2Fragment = new TestFragment();
		changeFragment(mTab2Fragment);
	}

}
