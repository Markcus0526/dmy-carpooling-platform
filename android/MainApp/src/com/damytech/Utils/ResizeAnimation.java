package com.damytech.Utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsoluteLayout;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-2-8
 * Time: 下午10:07
 * To change this template use File | Settings | File Templates.
 */
public class ResizeAnimation extends Animation {
	private View mView;
	private float mToHeight;
	private float mFromHeight;

	private float mToWidth;
	private float mFromWidth;

	public ResizeAnimation(View v, float fromWidth, float fromHeight, float toWidth, float toHeight) {
		mToHeight = toHeight;
		mToWidth = toWidth;
		mFromHeight = fromHeight;
		mFromWidth = fromWidth;
		mView = v;
		setDuration(300);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		float height = (mToHeight - mFromHeight) * interpolatedTime + mFromHeight;
		float width = (mToWidth - mFromWidth) * interpolatedTime + mFromWidth;
		ViewGroup.LayoutParams p = mView.getLayoutParams();
		p.height = (int) height;
		p.width = (int) width;
		mView.requestLayout();
	}

	@Override
	public boolean willChangeBounds() {
		return true;
	}
}