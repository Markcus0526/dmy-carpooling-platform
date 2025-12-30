package com.damytech.Utils.mutil;

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
import com.damytech.PincheApp.R;
import com.damytech.Utils.ResolutionSet;

/**
 * Created by KimHM on 2014-09-28.
 */
public class MWaitingDialog extends Dialog {
	Context parentContext;
	private String message="";
    private String count="";

	private ImageView imgProgressor = null;
	private TextView txtMessage = null;
	private TextView txtCountView = null;

	private OnDismissListener dismissListener = null;

	public MWaitingDialog(Context ctx) {
		super(ctx);
		parentContext = ctx;
        onCreate(null);
	}

	public MWaitingDialog(Context context, int theme) {
		super(context, theme);
		parentContext = context;
        onCreate(null);
	}

	protected MWaitingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		parentContext = context;
        onCreate(null);
	}

	public MWaitingDialog(Context ctx, String szMsg, String count, OnDismissListener dismissListener) {
		super(ctx);

		parentContext = ctx;

		this.message = szMsg;
		this.dismissListener = dismissListener;
        this.count = count;
        onCreate(null);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_waitingdialog);

		initControls();
		initVariables();
		initResolution();

		startRotation();
	}

	private void initControls()
	{
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		imgProgressor = (ImageView)findViewById(R.id.img_progressor1);
		txtMessage = (TextView)findViewById(R.id.txt_message1);
		txtCountView = (TextView)findViewById(R.id.txt_counter1);

		txtMessage.setText(message);
		txtCountView.setText(count);

		imgProgressor.setVisibility(View.VISIBLE);

        txtCountView.setVisibility(View.VISIBLE);
        txtMessage.setVisibility(View.VISIBLE);
	}

	private void initVariables()
	{
		message = parentContext.getResources().getString(R.string.STR_QINGSHAOHOU);
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
        this.message = message;
        txtMessage.setText(message);
	}

	public String getMessage() {
		return this.message;
	}
    public void setCount(String count) {
        this.count = count;
        txtCountView.setText(count);
	}

	public String getCount() {
		return this.count;
	}

}
