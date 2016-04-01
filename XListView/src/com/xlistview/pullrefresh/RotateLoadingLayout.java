package com.xlistview.pullrefresh;

import com.xlistview.res.DrawableRes;
import com.xlistview.res.LayoutRes;
import com.xlistview.res.ResUtil;
import com.xlistview.res.StringRes;
import com.xlistview.res.ViewRes;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 这个类封装了下拉刷新的布局
 * 
 * @author Li Hong
 * @since 2013-7-30
 */
public class RotateLoadingLayout extends LoadingLayout {
	/** 旋转动画的时间 */
	static final int ROTATION_ANIMATION_DURATION = 1200;
	/** 动画插值 */
	static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
	/** Header的容器 */
	private RelativeLayout mHeaderContainer;
	/** 箭头图片 */
	private ImageView mArrowImageView;
	/** 状态提示TextView */
	private TextView mHintTextView;
	/** 最后更新时间的TextView */
	private TextView mHeaderTimeView;
	/** 最后更新时间的标题 */
	private TextView mHeaderTimeViewTitle;
	/** 旋转的动画 */
	private Animation mRotateAnimation;

	private ResUtil mResUtil = null;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 */
	public RotateLoadingLayout(Context context) {
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
	public RotateLoadingLayout(Context context, AttributeSet attrs) {
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
		mHeaderTimeView = (TextView) findViewById(mResUtil
				.getIdFromView(ViewRes.pull_to_refresh_header_time));
		mHeaderTimeViewTitle = (TextView) findViewById(mResUtil
				.getIdFromView(ViewRes.pull_to_refresh_last_update_time_text));

		mArrowImageView.setScaleType(ScaleType.CENTER);
		mArrowImageView.setImageResource(mResUtil
				.getIdFromDrawable(DrawableRes.xlistview_default_ptr_rotate));

		float pivotValue = 0.5f; // SUPPRESS CHECKSTYLE
		float toDegree = 720.0f; // SUPPRESS CHECKSTYLE
		mRotateAnimation = new RotateAnimation(0.0f, toDegree,
				Animation.RELATIVE_TO_SELF, pivotValue,
				Animation.RELATIVE_TO_SELF, pivotValue);
		mRotateAnimation.setFillAfter(true);
		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
	}

	@Override
	protected View createLoadingView(Context context, AttributeSet attrs) {
		View container = LayoutInflater
				.from(context)
				.inflate(
						/* R.layout.pull_to_refresh_header2 */mResUtil
								.getIdFromLayout(LayoutRes.xlistview_pull_to_refresh_header),
						null);
		return container;
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
	protected void onStateChanged(State curState, State oldState) {
		super.onStateChanged(curState, oldState);
	}

	@Override
	protected void onReset() {
		resetRotation();
		mHintTextView.setText(mResUtil
				.getIdFromString(StringRes.pull_to_refresh_header_hint_normal));
	}

	@Override
	protected void onReleaseToRefresh() {
		mHintTextView.setText(mResUtil
				.getIdFromString(StringRes.pull_to_refresh_header_hint_ready));
	}

	@Override
	protected void onPullToRefresh() {
		mHintTextView.setText(mResUtil
				.getIdFromString(StringRes.pull_to_refresh_header_hint_normal));
	}

	@Override
	protected void onRefreshing() {
		resetRotation();
		mArrowImageView.startAnimation(mRotateAnimation);
		mHintTextView
				.setText(mResUtil
						.getIdFromString(StringRes.pull_to_refresh_header_hint_loading));
	}

	private ImageViewRotationHelper mRotationHelper = null;

	@Override
	public void onPull(float scale) {
		if (null == mRotationHelper) {
			mRotationHelper = new ImageViewRotationHelper(mArrowImageView);
		}

		float angle = scale * 180f; // SUPPRESS CHECKSTYLE
		mRotationHelper.setRotation(angle);
	}

	/**
	 * 重置动画
	 */
	private void resetRotation() {
		mArrowImageView.clearAnimation();
		mArrowImageView.setRotation(0);
	}

	/**
	 * The image view rotation helper
	 * 
	 * @author lihong06
	 * @since 2014-5-2
	 */
	static class ImageViewRotationHelper {
		/** The imageview */
		private final ImageView mImageView;
		/** The matrix */
		private Matrix mMatrix;
		/** Pivot X */
		private float mRotationPivotX;
		/** Pivot Y */
		private float mRotationPivotY;

		/**
		 * The constructor method.
		 * 
		 * @param imageView
		 *            the image view
		 */
		public ImageViewRotationHelper(ImageView imageView) {
			mImageView = imageView;
		}

		/**
		 * Sets the degrees that the view is rotated around the pivot point.
		 * Increasing values result in clockwise rotation.
		 * 
		 * @param rotation
		 *            The degrees of rotation.
		 * 
		 * @see #getRotation()
		 * @see #getPivotX()
		 * @see #getPivotY()
		 * @see #setRotationX(float)
		 * @see #setRotationY(float)
		 * 
		 * @attr ref android.R.styleable#View_rotation
		 */
		public void setRotation(float rotation) {
			if (android.os.Build.VERSION.SDK_INT > 10) {
				mImageView.setRotation(rotation);
			} else {
				if (null == mMatrix) {
					mMatrix = new Matrix();

					// 计算旋转的中心点
					Drawable imageDrawable = mImageView.getDrawable();
					if (null != imageDrawable) {
						mRotationPivotX = Math.round(imageDrawable
								.getIntrinsicWidth() / 2f);
						mRotationPivotY = Math.round(imageDrawable
								.getIntrinsicHeight() / 2f);
					}
				}

				mMatrix.setRotate(rotation, mRotationPivotX, mRotationPivotY);
				mImageView.setImageMatrix(mMatrix);
			}
		}
	}
}
