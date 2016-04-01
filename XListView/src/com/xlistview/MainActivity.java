package com.xlistview;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xlistview.adapter.SlideBaseAdapter;
import com.xlistview.adapter.SlideViewHolder;
import com.xlistview.my.OnListItemClickListener;
import com.xlistview.my.OnListItemLongClickListener;
import com.xlistview.my.OnPullRefreshListener;
import com.xlistview.my.PullSlideListView;
import com.xlistview.pullrefresh.PullToRefreshBase;
import com.xlistview.pullrefresh.PullToRefreshListView;

public class MainActivity extends Activity {

	private PullToRefreshListView mPullToRefreshListView = null;
	private PullSlideListView mPullSlideListView = null;
	private ListBaseAdapter mListAdapter = null;
	private ArrayList<ItemBean> mDataList = null;
	private Handler mHandler = null;
	private boolean mIsRefreshing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findView();
		init();
		registerListener();
	}

	// ***************************************************************
	// findView，init，registerListener

	private void findView() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
	}

	private void init() {
		mDataList = new ArrayList<ItemBean>();
		testInitDataList();

		mPullSlideListView = new PullSlideListView();

		View header = View.inflate(this, R.layout.listview_header, null);
		LinearLayout headerLinearLayout = (LinearLayout) header
				.findViewById(R.id.headerLinearLayout);
		for (int i = 0; i < 10; i++) {
			Button button = new Button(this);
			button.setText("header Button " + i);
			headerLinearLayout.addView(button);
		}

		View footer = View.inflate(this, R.layout.listview_footer, null);
		LinearLayout footerLinearLayout = (LinearLayout) footer
				.findViewById(R.id.footerLinearLayout);
		for (int i = 0; i < 10; i++) {
			Button button = new Button(this);
			button.setText("footer Button " + i);
			footerLinearLayout.addView(button);
		}

		mListAdapter = new ListBaseAdapter(this, mDataList,
				R.layout.listview_item, R.layout.xlistview_slide_content);
		mPullSlideListView.setView(mPullToRefreshListView, null, null,
				mListAdapter, true, true);

		mHandler = new Handler();
	}

	private void registerListener() {
		mPullSlideListView.setOnItemClickListener(new ListItemClickListener());
		mPullSlideListView
				.setOnItemLongClickListener(new ListItemLongClickListener());
		mPullSlideListView.setOnRefreshListener(new ListPullRefreshListener());
	}

	// ***************************************************************
	// 私有方法

	private void testInitDataList() {
		for (int i = 0; i < 80; i++) {
			ItemBean bean = new ItemBean("title" + i);
			mDataList.add(bean);
		}
	}

	private void toast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	// ***************************************************************
	// 自定义的类

	private class ListBaseAdapter extends SlideBaseAdapter<ItemBean> {

		public ListBaseAdapter(Context context, ArrayList<ItemBean> dataList,
				int itemLayoutId, int slideLayoutId) {
			super(context, dataList, itemLayoutId, slideLayoutId);
		}

		@Override
		public void convert(SlideViewHolder holder, ItemBean bean, int position) {

			TextView titleText = holder.getView(R.id.titleText);
			Button button = holder.getView(R.id.button);
			Button deleteButton = holder.getView(R.id.deleteButton);

			titleText.setText(bean.getTitle());
			button.setOnClickListener(new ButtonClickListener(position));
			deleteButton.setOnClickListener(new ButtonClickListener(position));
		}

		class ButtonClickListener implements OnClickListener {

			private int position = 0;

			public ButtonClickListener(int position) {
				this.position = position;
			}

			@Override
			public void onClick(View v) {
				// switch (v.getId()) {
				// case R.id.button:
				// toast("单击Button按钮，第" + position + "个");
				// break;
				// case R.id.deleteButton:
				// toast("单击删除按钮，第" + position + "个");
				// break;
				// }
				if (v.getId() == R.id.button)
					toast("单击Button按钮，第" + position + "个");
				else if (v.getId() == R.id.deleteButton)
					toast("单击删除按钮，第" + position + "个");
			}
		}

	}

	private class ListItemClickListener implements OnListItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			toast("单击了第" + position + "个item");
		}
	}

	private class ListItemLongClickListener implements
			OnListItemLongClickListener {
		@Override
		public void onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			toast("长按了第" + position + "个item");
		}
	}

	private class ListPullRefreshListener implements
			OnPullRefreshListener<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (mIsRefreshing == false) {
				mIsRefreshing = true;
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mPullSlideListView.onPullDownRefreshComplete();
						mIsRefreshing = false;
					}
				}, 1000);
			}
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			if (mIsRefreshing == false) {
				mIsRefreshing = true;
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mPullSlideListView.onPullUpRefreshComplete();
						mIsRefreshing = false;
					}
				}, 1000);
			}
		}

	}

}
