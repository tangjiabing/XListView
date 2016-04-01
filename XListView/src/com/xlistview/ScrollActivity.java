package com.xlistview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.xlistview.pullrefresh.PullToRefreshScrollView;

public class ScrollActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scroll);

		PullToRefreshScrollView pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pullToRefreshScrollView);

		ScrollView scrollView = pullToRefreshScrollView.getRefreshableView();

		LinearLayout linearLayout = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		linearLayout.setLayoutParams(params);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		scrollView.addView(linearLayout);

		for (int i = 0; i < 30; i++) {
			Button button = new Button(this);
			button.setText("button" + i);
			linearLayout.addView(button);
		}

	}

}
