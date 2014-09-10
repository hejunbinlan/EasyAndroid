package org.hjb.easyandroid.ui.widget;

import org.hjb.easyandroid.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EAScrollViewHeader extends LinearLayout {
	public static final String TAG = EAScrollViewHeader.class.getSimpleName();
	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	private ImageView arrowImageView;
	private ProgressBar progressBar;
	private TextView titleTextView;
	private TextView subTitleTextView;

	private boolean isBack;

	private int headerHeight;

	public EAScrollViewHeader(Context context) {
		this(context, null);
	}

	public EAScrollViewHeader(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public EAScrollViewHeader(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.init();
	}

	private void init() {
		this.setTag(TAG);
		animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		LayoutInflater.from(getContext()).inflate(R.layout.view_scroll_view_head, this, true);

		arrowImageView = (ImageView) this.findViewById(R.id.arrowImageView);
		progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
		titleTextView = (TextView) this.findViewById(R.id.titleTextView);
		subTitleTextView = (TextView) this.findViewById(R.id.subTitleTextView);
		measureView();
		headerHeight = getMeasuredHeight();
		this.setPadding(0, -headerHeight, 0, 0);
	}

	private void measureView() {
		ViewGroup.LayoutParams p = getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		measure(childWidthSpec, childHeightSpec);
	}

	public void setBack(boolean isBack) {
		this.isBack = isBack;
	}

	/**
	 * 松手刷新
	 */
	public void releaseToRefresh() {
		arrowImageView.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		titleTextView.setVisibility(View.VISIBLE);
		arrowImageView.clearAnimation();
		arrowImageView.startAnimation(animation);
		titleTextView.setText(getContext().getString(R.string.refresh_release_label));
	}

	/**
	 * 下拉刷新
	 */
	public void pullToRefresh() {
		progressBar.setVisibility(View.GONE);
		titleTextView.setVisibility(View.VISIBLE);
		arrowImageView.clearAnimation();
		arrowImageView.setVisibility(View.VISIBLE);
		// 是由RELEASE_To_REFRESH状态转变来的
		if (isBack) {
			isBack = false;
			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(reverseAnimation);
		} 
		titleTextView.setText(getContext().getString(R.string.refresh_pull_label));
	}

	/**
	 * 刷新中
	 */
	public void refreshing() {
		setPadding(0, 0, 0, 0);
		progressBar.setVisibility(View.VISIBLE);
		arrowImageView.clearAnimation();
		arrowImageView.setVisibility(View.GONE);
		titleTextView.setText(getContext().getString(R.string.refresh_refreshing_label));
	}

	/**
	 * 刷新完成
	 */
	public void done() {
		setPadding(0, -headerHeight, 0, 0);
		progressBar.setVisibility(View.GONE);
		arrowImageView.clearAnimation();
		titleTextView.setText(getContext().getString(R.string.refresh_pull_label));
	}

	public void setSubTitle(String subTitle) {
		subTitleTextView.setText(subTitle);
		subTitleTextView.setVisibility(View.VISIBLE);
	}
}
