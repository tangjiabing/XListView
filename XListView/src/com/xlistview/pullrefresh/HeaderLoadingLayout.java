package com.xlistview.pullrefresh;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xlistview.res.LayoutRes;
import com.xlistview.res.ResUtil;
import com.xlistview.res.StringRes;
import com.xlistview.res.ViewRes;

/**
 * 这个类封装了下拉刷新的布局
 * 
 * @author Li Hong
 * @since 2013-7-30
 */
public class HeaderLoadingLayout extends LoadingLayout {
	/** 旋转动画时间 */
	private static final int ROTATE_ANIM_DURATION = 150;
	/** Header的容器 */
	private RelativeLayout mHeaderContainer;
	/** 箭头图片 */
	private ImageView mArrowImageView;
	/** 进度条 */
	private ProgressBar mProgressBar;
	/** 状态提示TextView */
	private TextView mHintTextView;
	/** 最后更新时间的TextView */
	private TextView mHeaderTimeView;
	/** 最后更新时间的标题 */
	private TextView mHeaderTimeViewTitle;
	/** 向上的动画 */
	private Animation mRotateUpAnim;
	/** 向下的动画 */
	private Animation mRotateDownAnim;

	/** 下拉时的内容 */
	private CharSequence mPullContent = null;
	/** 松开时的内容 */
	private CharSequence mReleaseContent = null;
	/** 正在刷新时的内容 */
	private CharSequence mRefreshingContent = null;
	/** true为header，false为footer */
	private boolean mIsHeader = true;

	private ResUtil mResUtil = null;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 */
	public HeaderLoadingLayout(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public HeaderLoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 *            context
	 */
	private void init(Context context) {
		mResUtil = ResUtil.getInstance(context);

		mHeaderContainer = (RelativeLayout) findViewById(mResUtil
				.getIdFromView(ViewRes.pull_to_refresh_header_content));
		mArrowImageView = (ImageView) findViewById(mResUtil
				.getIdFromView(ViewRes.pull_to_refresh_header_arrow));
		mHintTextView = (TextView) findViewById(mResUtil
				.getIdFromView(ViewRes.pull_to_refresh_header_hint_textview));
		mProgressBar = (ProgressBar) findViewById(mResUtil
				.getIdFromView(ViewRes.pull_to_refresh_header_progressbar));
		mHeaderTimeView = (TextView) findViewById(mResUtil
				.getIdFromView(ViewRes.pull_to_refresh_header_time));
		mHeaderTimeViewTitle = (TextView) findViewById(mResUtil
				.getIdFromView(ViewRes.pull_to_refresh_last_update_time_text));

		float pivotValue = 0.5f; // SUPPRESS CHECKSTYLE
		float toDegree = -180f; // SUPPRESS CHECKSTYLE
		// 初始化旋转动画
		mRotateUpAnim = new RotateAnimation(0.0f, toDegree,
				Animation.RELATIVE_TO_SELF, pivotValue,
				Animation.RELATIVE_TO_SELF, pivotValue);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(toDegree, 0.0f,
				Animation.RELATIVE_TO_SELF, pivotValue,
				Animation.RELATIVE_TO_SELF, pivotValue);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {
		// 如果最后更新的时间的文本是空的话，隐藏前面的标题
		mHeaderTimeViewTitle
				.setVisibility(TextUtils.isEmpty(label) ? View.INVISIBLE
						: View.VISIBLE);
		mHeaderTimeView.setText(label);
	}

	@Override
	public int getContentSize() {
		if (null != mHeaderContainer) {
			return mHeaderContainer.getHeight();
		}

		return (int) (getResources().getDisplayMetrics().density * 60);
	}

	@Override
	protected View createLoadingView(Context context, AttributeSet attrs) {
		if (mResUtil == null)
			mResUtil = ResUtil.getInstance(context);
		View container = LayoutInflater
				.from(context)
				.inflate(
						mResUtil.getIdFromLayout(LayoutRes.xlistview_pull_to_refresh_header),
						null);
		return container;
	}

	@Override
	protected void onStateChanged(State curState, State oldState) {
		mArrowImageView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.INVISIBLE);

		super.onStateChanged(curState, oldState);
	}

	@Override
	protected void onReset() {
		mArrowImageView.clearAnimation();
		if (mPullContent != null)
			mHintTextView.setText(mPullContent);
		else
			mHintTextView
					.setText(mResUtil
							.getIdFromString(StringRes.pull_to_refresh_header_hint_normal));
	}

	@Override
	protected void onPullToRefresh() {
		if (State.RELEASE_TO_REFRESH == getPreState()) {
			mArrowImageView.clearAnimation();
			if (mIsHeader)
				mArrowImageView.startAnimation(mRotateDownAnim);
			else
				mArrowImageView.startAnimation(mRotateUpAnim);
		}
		if (mPullContent != null)
			mHintTextView.setText(mPullContent);
		else
			mHintTextView
					.setText(mResUtil
							.getIdFromString(StringRes.pull_to_refresh_header_hint_normal));
	}

	@Override
	protected void onReleaseToRefresh() {
		mArrowImageView.clearAnimation();
		if (mIsHeader)
			mArrowImageView.startAnimation(mRotateUpAnim);
		else
			mArrowImageView.startAnimation(mRotateDownAnim);
		if (mReleaseContent != null)
			mHintTextView.setText(mReleaseContent);
		else
			mHintTextView
					.setText(mResUtil
							.getIdFromString(StringRes.pull_to_refresh_header_hint_ready));
	}

	@Override
	protected void onRefreshing() {
		mArrowImageView.clearAnimation();
		mArrowImageView.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.VISIBLE);

		if (mRefreshingContent != null)
			mHintTextView.setText(mRefreshingContent);
		else
			mHintTextView
					.setText(mResUtil
							.getIdFromString(StringRes.pull_to_refresh_header_hint_loading));
	}

	@Override
	public void setPullLabel(CharSequence pullLabel) {
		mPullContent = pullLabel;
	}

	@Override
	public void setRefreshingLabel(CharSequence refreshingLabel) {
		mRefreshingContent = refreshingLabel;
	}

	@Override
	public void setReleaseLabel(CharSequence releaseLabel) {
		mReleaseContent = releaseLabel;
	}

	/**
	 * 设置是否为HeaderLayout，
	 * 
	 * @param isHeader
	 *            true为header，false为footer
	 */
	@Override
	public void setIsHeader(boolean isHeader) {
		mIsHeader = isHeader;
	}

	@Override
	public void showContainer() {
		mHeaderContainer.postDelayed(new Runnable() {
			@Override
			public void run() {
				mHeaderContainer.setVisibility(View.VISIBLE);
			}
		}, 10);
	}

}
