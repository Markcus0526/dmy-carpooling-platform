package com.damytech.PincheApp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;

public class ConfirmPasswordDialog extends Dialog
{
    Context mContext;
	boolean isCancellable = false;

    String strPass = "";
    long mPassId = 0;
    OnDismissListener dismissListener = null;

    EditText txtPassword;
    Button btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine, btnZero, btnClear, btnOk;

    public ConfirmPasswordDialog(Context context)
    {
        super(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dlg_driver_confirmpassword);

        Point ptScreen = Global.getScreenSize(mContext.getApplicationContext());
        ResolutionSet.instance.iterateChild(((RelativeLayout)findViewById(R.id.parent_layout)).getChildAt(0), ptScreen.x, ptScreen.y);

        initControl();
    }


	@Override
	public void setCancelable(boolean flag) {
		isCancellable = flag;
	}

	private void initControl()
    {
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        btnOne = (Button) findViewById(R.id.btnOne);
        btnOne.setOnClickListener(onClickListener);
        btnTwo = (Button) findViewById(R.id.btnTwo);
        btnTwo.setOnClickListener(onClickListener);
        btnThree = (Button) findViewById(R.id.btnThree);
        btnThree.setOnClickListener(onClickListener);
        btnFour = (Button) findViewById(R.id.btnFour);
        btnFour.setOnClickListener(onClickListener);
        btnFive = (Button) findViewById(R.id.btnFive);
        btnFive.setOnClickListener(onClickListener);
        btnSix = (Button) findViewById(R.id.btnSix);
        btnSix.setOnClickListener(onClickListener);
        btnSeven = (Button) findViewById(R.id.btnSeven);
        btnSeven.setOnClickListener(onClickListener);
        btnEight = (Button) findViewById(R.id.btnEight);
        btnEight.setOnClickListener(onClickListener);
        btnNine = (Button) findViewById(R.id.btnNine);
        btnNine.setOnClickListener(onClickListener);
        btnZero = (Button) findViewById(R.id.btnZero);
        btnZero.setOnClickListener(onClickListener);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(onClickListener);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(onClickListener);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void setOnDismissListener(OnDismissListener listener)
    {
        dismissListener = listener;
    }

    public String getPassword()
    {
        strPass = txtPassword.getText().toString();
        return strPass;
    }

    public long getPassId()
    {
        return mPassId;
    }

    public void setPassId(long passid)
    {
        mPassId = passid;
    }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == R.id.btnClear)
            {
                txtPassword.setText("");
                strPass = "";
                return;
            }
            if (v.getId() == R.id.btnOk)
            {
                strPass = txtPassword.getText().toString();
                if (strPass.length() == 0 || strPass.length() != 4)
                {
                    String message = mContext.getResources().getString(R.string.STR_ERROR_PASSWORD_LENGTH);
                    Global.showAdvancedToast((Activity) mContext, message, Gravity.CENTER);
                    return;
                }

                dismissListener.onDismiss(ConfirmPasswordDialog.this);
                dismiss();

                return;
            }

            strPass = txtPassword.getText().toString();
            if (strPass != null && strPass.length() >= 4)
                return;

            switch (v.getId())
            {
                case R.id.btnOne:
                    strPass += "1";
                    txtPassword.setText(strPass);
                    break;
                case R.id.btnTwo:
                    strPass += "2";
                    txtPassword.setText(strPass);
                    break;
                case R.id.btnThree:
                    strPass += "3";
                    txtPassword.setText(strPass);
                    break;
                case R.id.btnFour:
                    strPass += "4";
                    txtPassword.setText(strPass);
                    break;
                case R.id.btnFive:
                    strPass += "5";
                    txtPassword.setText(strPass);
                    break;
                case R.id.btnSix:
                    strPass += "6";
                    txtPassword.setText(strPass);
                    break;
                case R.id.btnSeven:
                    strPass += "7";
                    txtPassword.setText(strPass);
                    break;
                case R.id.btnEight:
                    strPass += "8";
                    txtPassword.setText(strPass);
                    break;
                case R.id.btnNine:
                    strPass += "9";
                    txtPassword.setText(strPass);
                    break;
                case R.id.btnZero:
                    strPass += "0";
                    txtPassword.setText(strPass);
                    break;
            }
        }
    };


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && !isCancellable) {
            return true;
        }

		return super.onKeyDown(keyCode, event);
	}
}