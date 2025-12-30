package com.damytech.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.Misc.Global;
import com.damytech.MainApp.R;

/**
 * Created by KimHM on 2014-09-28.
 */
public class ProgressDialog extends Dialog {
	public static final int MODE_COUNT_TIMER = 1;
	public static final int MODE_WAITER = 2;

	Context parentContext;
	private String message;
	private int progress_mode = MODE_WAITER;

	private ImageView imgProgressor = null;
	private TextView txtMessage = null;
	private TextView txtCountView = null;

	private OnDismissListener dismissListener = null;

	public ProgressDialog(Context ctx) {
		super(ctx);
		parentContext = ctx;
	}

	public ProgressDialog(Context context, int theme) {
		super(context, theme);
		parentContext = context;
	}

	protected ProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		parentContext = context;
	}

	public ProgressDialog(Context ctx, String szMsg, int nMode, OnDismissListener dismissListener) {
		super(ctx);

		parentContext = ctx;

		this.message = szMsg;
		this.progress_mode = nMode;
		this.dismissListener = dismissListener;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_progressor);

		initControls();
		initVariables();
		initResolution();

		startRotation();
	}

	private void initControls()
	{
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		imgProgressor = (ImageView)findViewById(R.id.img_progressor);
		txtMessage = (TextView)findViewById(R.id.txt_message);
		txtCountView = (TextView)findViewById(R.id.txt_counter);

		txtMessage.setText(message);
		txtCountView.setText(message);

		imgProgressor.setVisibility(View.VISIBLE);

		if (progress_mode == MODE_WAITER) {
			txtCountView.setVisibility(View.INVISIBLE);
			txtMessage.setVisibility(View.VISIBLE);
		} else {
			txtCountView.setVisibility(View.VISIBLE);
			txtMessage.setVisibility(View.INVISIBLE);
		}
	}

	private void initVariables()
	{
		message = parentContext.getResources().getString(R.string.STR_QINGSHAOHOU);
		progress_mode = MODE_WAITER;
	}

	private void initResolution()
	{
		Point ptScreen = Global.getScreenSize(parentContext.getApplicationContext());
		ResolutionSet.instance.iterateChild(((RelativeLayout)findViewById(R.id.parent_layout)).getChildAt(0), ptScreen.x, ptScreen.y);
	}


	private void startRotation() {
		imgProgressor.clearAnimation();

		Animation operatingAnim = AnimationUtils.loadAnimation(parentContext, R.anim.wait_rotation);
		operatingAnim.setInterpolator(new LinearInterpolator());
		if (operatingAnim != null) {
			imgProgressor.startAnimation(operatingAnim);
		}
	}


	public void setMessage(String message) {
		if (progress_mode == MODE_WAITER) {
			if (txtMessage != null)
				txtMessage.setText(message);
		} else {
			if (txtCountView != null)
				txtCountView.setText(message);
		}
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMode(int progress_mode) {
		this.progress_mode = progress_mode;
	}

	private int getMode() {
		return this.progress_mode;
	}
}
