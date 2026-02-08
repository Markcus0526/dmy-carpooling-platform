package com.damytech.PincheApp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.*;
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STWithdrawInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshBase;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshListView;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class WithdrawLogActivity extends SuperActivity implements DialogInterface.OnDismissListener
{
    long nDeletedID = -1;
    WithDrawFinishDialog dlgDelete = null;

    ImageButton btn_back = null;
    TextView lblBalanceSum = null;

    int withdraw_pageno = 0;
    PullToRefreshListView log_list = null;
    private ArrayList<STWithdrawInfo> arrLogs = new ArrayList<STWithdrawInfo>();
    private ArrayList<STWithdrawInfo> arrTempLogs = new ArrayList<STWithdrawInfo>();
    WithdrawLogAdapter adapter = new WithdrawLogAdapter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_withdrawlog);

		initControls();
		initResolution();
	}

	private void initControls()
	{
        lblBalanceSum = (TextView) findViewById(R.id.lblMark);

		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onClickBack();
            }
        });

        log_list = (PullToRefreshListView)findViewById(R.id.scroll_view);
        {
            log_list.setMode(PullToRefreshBase.Mode.BOTH);
            log_list.setOnRefreshListener(log_list_refresh_listener);
            log_list.setAdapter(adapter);
        }

        startProgress();
        getOlderLogs();
	}

    private PullToRefreshBase.OnRefreshListener log_list_refresh_listener = new PullToRefreshBase.OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshBase refreshView) {
            PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
            if (mode == PullToRefreshBase.Mode.PULL_FROM_START)
                getLatestLogs();
            else
                getOlderLogs();
        }
    };

    private void getLatestLogs()
    {
        if (arrLogs == null || arrLogs.size() == 0)
        {
            withdraw_pageno = 0;
            CommManager.pagedWithdrawLogs(Global.loadUserID(getApplicationContext()), withdraw_pageno, Global.getIMEI(getApplicationContext()), older_handler);
        }
        else
            CommManager.latestWithdrawLogs(Global.loadUserID(getApplicationContext()), arrLogs.get(0).uid, Global.getIMEI(getApplicationContext()), latest_handler);
    }

    private void getOlderLogs()
    {
        CommManager.pagedWithdrawLogs(Global.loadUserID(getApplicationContext()), withdraw_pageno, Global.getIMEI(getApplicationContext()), older_handler);
    }

    private AsyncHttpResponseHandler older_handler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(int statusCode, String content) {
            super.onSuccess(statusCode, content);    //To change body of overridden methods use File | Settings | File Templates.

            stopProgress();
            log_list.onRefreshComplete();

            try
            {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject result = jsonObj.getJSONObject("result");

                int nRetcode = result.getInt("retcode");
                String szRetmsg = result.getString("retmsg");
                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    JSONArray retdata = result.getJSONArray("retdata");
                    arrTempLogs.clear();
                    for (int i = 0; i < retdata.length(); i++)
                    {
                        JSONObject logitem = retdata.getJSONObject(i);

                        STWithdrawInfo info  =STWithdrawInfo.decodeFromJSON(logitem);
                        arrTempLogs.add(info);
                    }

                    addOldLogs();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(szRetmsg);
                }
                else
                {
                    Global.showAdvancedToast(WithdrawLogActivity.this, szRetmsg, Gravity.CENTER);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
            log_list.onRefreshComplete();
            stopProgress();
	        Global.showAdvancedToast(WithdrawLogActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    private AsyncHttpResponseHandler latest_handler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(int statusCode, String content) {
            super.onSuccess(statusCode, content);    //To change body of overridden methods use File | Settings | File Templates.
            log_list.onRefreshComplete();
            stopProgress();

            try
            {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject result = jsonObj.getJSONObject("result");

                int nRetcode = result.getInt("retcode");
                String szRetmsg = result.getString("retmsg");
                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    JSONArray retData = result.getJSONArray("retdata");

                    arrTempLogs.clear();
                    for (int i = 0; i < retData.length(); i++)
                    {
                        JSONObject logitem = retData.getJSONObject(i);

                        STWithdrawInfo info = STWithdrawInfo.decodeFromJSON(logitem);
                        arrTempLogs.add(info);
                    }

                    insertLatestLogs();
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(szRetmsg);
                }
                else
                {
                    Global.showAdvancedToast(WithdrawLogActivity.this, szRetmsg, Gravity.CENTER);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
            log_list.onRefreshComplete();
            stopProgress();
	        Global.showAdvancedToast(WithdrawLogActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    private void addOldLogs()
    {
        boolean hasUpdate = false;

        for (int i = 0; i < arrTempLogs.size(); i++)
        {
            STWithdrawInfo new_item = arrTempLogs.get(i);

            boolean isExist = false;
            for (int j = 0; j < arrLogs.size(); j++)
            {
                STWithdrawInfo old_item = arrLogs.get(j);
                if (old_item.uid == new_item.uid)
                {
                    isExist = true;
                    break;
                }
            }

            if (isExist)
                continue;

            arrLogs.add(new_item);

            if (!hasUpdate)
                hasUpdate = true;
        }

        if (hasUpdate)
        {
            withdraw_pageno = arrLogs.size() / Global.getPageItemCount();
        }

        double fSum = 0.0f;
        for (int i = 0; i < arrLogs.size(); i++)
        {
            if (arrLogs.get(i).state == 1 || arrLogs.get(i).state == 5)
                fSum += arrLogs.get(i).balance;
        }
        String strBalance = String.format("%.2f", fSum);
        lblBalanceSum.setText(strBalance);

        adapter.notifyDataSetChanged();
    }

    private void insertLatestLogs()
    {
        boolean hasUpdate = false;

        for (int i = 0; i < arrTempLogs.size(); i++)
        {
            STWithdrawInfo new_item = arrTempLogs.get(i);

            boolean isExist = false;
            for (int j = 0; j < arrLogs.size(); j++)
            {
                STWithdrawInfo old_item = arrLogs.get(j);
                if (old_item.uid == new_item.uid)
                {
                    isExist = true;
                    break;
                }
            }

            if (isExist)
                continue;

            arrLogs.add(0, new_item);

            if (!hasUpdate)
                hasUpdate = true;
        }

        if (hasUpdate)
        {
            withdraw_pageno = arrLogs.size() / Global.getPageItemCount();
        }

        adapter.notifyDataSetChanged();
    }

	private void initResolution()
	{
		RelativeLayout parent_layout = (RelativeLayout)findViewById(R.id.parent_layout);
		parent_layout.getViewTreeObserver().addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Point ptTemp = getScreenSize();
				boolean bNeedUpdate = false;

				if (mScrSize.x == 0 && mScrSize.y == 0)
				{
					mScrSize = ptTemp;
					bNeedUpdate = true;
				}
				else if (mScrSize.x != ptTemp.x || mScrSize.y != ptTemp.y)
				{
					mScrSize = ptTemp;
					bNeedUpdate = true;
				}

				if (bNeedUpdate)
				{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ResolutionSet.instance.iterateChild(findViewById(R.id.parent_layout), mScrSize.x, mScrSize.y);
						}
					});
				}
			}
		});
	}

	private void onClickBack()
	{
		finishWithAnimation();
	}

    @Override
    public void onStop()
    {
        super.onStop();

        if (dlgDelete != null && dlgDelete.isShowing())
            dlgDelete.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        dlgDelete = (WithDrawFinishDialog) dialog;

        int nIsDeleted = dlgDelete.IsDeleted();
        if (nIsDeleted == 1)
        {
            startProgress();
            CommManager.cancelWithdraw(Global.loadUserID(getApplicationContext()),
                    nDeletedID,
                    Global.getIMEI(getApplicationContext()),
                    cancel_handler);
        }
        else
        {
            nDeletedID = -1;
        }
    }

    private class WithdrawLogAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return arrLogs.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

	    @Override
	    public boolean isEnabled(int position) {
		    return false;
	    }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public Object getItem(int position) {
            return arrLogs.get(position);
        }

        @Override
        public boolean isEmpty() {
            if (arrLogs == null)
                return true;

            return arrLogs.isEmpty();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            STWithdrawInfo withInfo = arrLogs.get(position);

            if (convertView == null)
            {
                convertView = getLayoutInflater().inflate(R.layout.view_driver_withdrawlogitem, null);

                STWithdrawLogViewHolder holder = new STWithdrawLogViewHolder();

                holder.lblReqTime = (TextView)convertView.findViewById(R.id.txt_reqtime);
                holder.lblReqTime.setText(withInfo.req_date);

                holder.lblMark = (TextView)convertView.findViewById(R.id.txt_mark);
                holder.lblMark.setText(Double.toString(withInfo.balance));

                holder.lblState = (TextView)convertView.findViewById(R.id.txt_state);
                String strState = "";
                switch (withInfo.state)
                {
                    case 1:
                        strState = getString(R.string.STR_BALANCE_DEICHULI);
                        break;
                    case 2:
                        strState = getString(R.string.STR_BALANCE_YICHULI);
                        break;
                    case 3:
                        strState = getString(R.string.STR_BALANCE_YIGUANBI);
                        break;
                    case 4:
                        strState = getString(R.string.STR_BALANCE_YICHEXIAO);
                        break;
                    case 5:
                        strState = getString(R.string.STR_BALANCE_CHULIZHONG);
                        break;
                }
                holder.lblState.setText(strState);

                holder.btnOperation = (Button)convertView.findViewById(R.id.btnOperation);
                holder.btnOperation.setTag(withInfo);
                holder.btnOperation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        STWithdrawInfo info = (STWithdrawInfo) v.getTag();
                        if (info != null)
                        {
                            nDeletedID = info.uid;
                            dlgDelete = new WithDrawFinishDialog(WithdrawLogActivity.this);
                            dlgDelete.setOnDismissListener(WithdrawLogActivity.this);
	                        dlgDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dlgDelete.show();
                        }
                    }
                });
                if (withInfo.state != 1)
                    holder.btnOperation.setVisibility(View.INVISIBLE);
	            else
                    holder.btnOperation.setVisibility(View.VISIBLE);

                holder.uid = withInfo.uid;

                convertView.setTag(holder);

                ResolutionSet.instance.iterateChild(convertView, mScrSize.x, mScrSize.y);
            }
            else
            {
                STWithdrawLogViewHolder viewHolder = (STWithdrawLogViewHolder)convertView.getTag();

                viewHolder.lblReqTime.setText(withInfo.req_date);
                viewHolder.lblMark.setText(Double.toString(withInfo.balance));
                String strState = "";
                switch (withInfo.state)
                {
                    case 1:
                        strState = getString(R.string.STR_BALANCE_DEICHULI);
                        break;
                    case 2:
                        strState = getString(R.string.STR_BALANCE_YICHULI);
                        break;
                    case 3:
                        strState = getString(R.string.STR_BALANCE_YIGUANBI);
                        break;
                    case 4:
                        strState = getString(R.string.STR_BALANCE_YICHEXIAO);
                        break;
                    case 5:
                        strState = getString(R.string.STR_BALANCE_CHULIZHONG);
                        break;
                }
                viewHolder.lblState.setText(strState);
	            if (withInfo.state != 1)
                    viewHolder.btnOperation.setVisibility(View.INVISIBLE);
	            else
	                viewHolder.btnOperation.setVisibility(View.VISIBLE);
                viewHolder.btnOperation.setTag(withInfo);
                viewHolder.btnOperation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        STWithdrawInfo info = (STWithdrawInfo) v.getTag();
                        if (info != null)
                        {
                            nDeletedID = info.uid;
                            dlgDelete = new WithDrawFinishDialog(WithdrawLogActivity.this);
                            dlgDelete.setOnDismissListener(WithdrawLogActivity.this);
	                        dlgDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dlgDelete.show();
                        }
                    }
                });
                viewHolder.uid = withInfo.uid;
            }

            return convertView;
        }
    }

    private class STWithdrawLogViewHolder
    {
        public long uid = 0;
        public TextView lblReqTime = null;
        public TextView lblMark = null;
        public TextView lblState = null;
        public Button btnOperation = null;
    }

    private AsyncHttpResponseHandler cancel_handler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(String content) {
            super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
            stopProgress();

            try {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject result = jsonObj.getJSONObject("result");

                int nRetcode = result.getInt("retcode");
                String szRetmsg = result.getString("retmsg");

                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    Global.showAdvancedToast(WithdrawLogActivity.this, getResources().getString(R.string.STR_BALANCE_WITHDRAW_CANCELSUCCESS), Gravity.CENTER);

                    JSONObject retdata = result.getJSONObject("retdata");

                    for (int i = 0; i < arrLogs.size(); i++)
                    {
                        if (arrLogs.get(i).uid == nDeletedID)
                        {
                            arrLogs.get(i).state = 4;
                            adapter.notifyDataSetChanged();
                            nDeletedID = -1;
                            break;
                        }
                    }

                    double last_balance = 0.0f;
                    for (int i = 0; i < arrLogs.size(); i++)
                    {
                        if (arrLogs.get(i).state == 1 || arrLogs.get(i).state == 5)
                        {
                            last_balance += arrLogs.get(i).balance;
                        }
                    }
                    String strBalance = String.format("%.2f", last_balance);
                    lblBalanceSum.setText(strBalance);
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(szRetmsg);
                }
                else
                {
                    Global.showAdvancedToast(WithdrawLogActivity.this, szRetmsg, Gravity.CENTER);
                }

                nDeletedID = -1;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
            stopProgress();
            nDeletedID = -1;
	        Global.showAdvancedToast(WithdrawLogActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };
}
