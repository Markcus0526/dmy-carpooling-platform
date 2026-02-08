package com.damytech.PincheApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STCommonRoute;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 拼友主界面
 */
public class PassLongRouteMainActivity extends SuperActivity implements DialogInterface.OnDismissListener
{
	TextView txtHint = null;

    ImageButton btn_back = null;
    Button btnAdd = null;
    ListView listDatas = null;

    ArrayList<STCommonRoute> arrDatas = new ArrayList<STCommonRoute>();
    ItemAdapter adapter = null;

    DrvDailyRouteDeleteDialog dlgDelete = null;
    long nDeletedID = -1;

    private AsyncHttpResponseHandler route_handler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(String content) {
            super.onSuccess(content);
            stopProgress();

            try
            {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject result = jsonObj.getJSONObject("result");
                int nRetcode = result.getInt("retcode");
                String szRetmsg = result.getString("retmsg");

                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    arrDatas.clear();

                    JSONArray arrRoutes = result.getJSONArray("retdata");
                    for (int i = 0; i < arrRoutes.length(); i++)
                    {
                        STCommonRoute route = STCommonRoute.decodeFromJSON(arrRoutes.getJSONObject(i));
                        arrDatas.add(route);
                    }

	                if (arrDatas.size() > 0)
		                txtHint.setText(getResources().getString(R.string.STR_COMMONROUTE_HINT_HASDATA));
	                else
		                txtHint.setText(getResources().getString(R.string.STR_COMMONLONGROUTE_HINT));

                    adapter = new ItemAdapter(PassLongRouteMainActivity.this, 0, arrDatas);
                    listDatas.setAdapter(adapter);
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(szRetmsg);
                }
                else
                {
                    Global.showAdvancedToast(PassLongRouteMainActivity.this, szRetmsg, Gravity.CENTER);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);
            stopProgress();
	        Global.showAdvancedToast(PassLongRouteMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
        }
    };

    private AsyncHttpResponseHandler delete_handler = new AsyncHttpResponseHandler()
    {
        @Override
        public void onSuccess(String content) {
            super.onSuccess(content);
            stopProgress();

            try
            {
                JSONObject jsonObj = new JSONObject(content);
                JSONObject result = jsonObj.getJSONObject("result");
                int nRetcode = result.getInt("retcode");
                String szRetmsg = result.getString("retmsg");

                if (nRetcode == ConstData.ERR_CODE_NONE)
                {
                    for (int i = 0; i < arrDatas.size(); i++)
                    {
                        if (arrDatas.get(i).uid == nDeletedID)
                        {
                            arrDatas.remove(i);
                            adapter.notifyDataSetChanged();
                            nDeletedID = -1;
                            break;
                        }
                    }

	                if (arrDatas.size() > 0)
		                txtHint.setText(getResources().getString(R.string.STR_COMMONROUTE_HINT_HASDATA));
	                else
		                txtHint.setText(getResources().getString(R.string.STR_COMMONLONGROUTE_HINT));
                }
                else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
                {
                    logout(szRetmsg);
                }
                else
                {
                    Global.showAdvancedToast(PassLongRouteMainActivity.this, szRetmsg, Gravity.CENTER);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable error, String content) {
            super.onFailure(error, content);
            stopProgress();
	        Global.showAdvancedToast(PassLongRouteMainActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);

            nDeletedID = -1;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_pass_long_route_main);

        initControls();
        initResolution();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetRoutes();
    }


    private void GetRoutes()
    {
        startProgress();
        CommManager.getRoutes(Global.loadUserID(getApplicationContext()),
                1,
                Global.getIMEI(getApplicationContext()),
                route_handler);
    }


    private void initControls()
    {
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBack();
            }
        });

        btnAdd = (Button) findViewById(R.id.btnAddRoute);
        btnAdd.setOnClickListener(onClickListener);

        listDatas = (ListView) findViewById(R.id.listRoutes);
        listDatas.setDivider(new ColorDrawable(Color.parseColor("#FFCCCCCC")));
        listDatas.setCacheColorHint(Color.parseColor("#FFF1F1F1"));

	    txtHint = (TextView)findViewById(R.id.lblHint);
	    txtHint.setText("");
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btnAddRoute:
                    Intent intent = new Intent(PassLongRouteMainActivity.this, PassLongSetRouteActivity.class);
                    intent.putExtra("IsUpdate", 0);
                    intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                    PassLongRouteMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                    startActivity(intent);
                    break;
            }
        }
    };

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
        dlgDelete = (DrvDailyRouteDeleteDialog) dialog;

        int nIsDeleted = dlgDelete.IsDeleted();
        if (nIsDeleted == 1)
        {
            startProgress();
            CommManager.removeRoute(Global.loadUserID(getApplicationContext()),
                    nDeletedID,
                    Global.getIMEI(getApplicationContext()),
                    delete_handler);
        }
        else
        {
            nDeletedID = -1;
        }
    }


	private class STItemViewHolder
	{
		TextView lblPath = null;
		TextView lblDate = null;
		ImageButton btnItem = null;
	}

    public class ItemAdapter extends ArrayAdapter<STCommonRoute>
    {
        Context ctx;
        ArrayList<STCommonRoute> list = new ArrayList<STCommonRoute>();

        public ItemAdapter(Context ctx, int resourceId, ArrayList<STCommonRoute> list) {
            super(ctx, resourceId, list);
            this.ctx = ctx;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
	        STItemViewHolder holder = null;
            if (v == null)
            {
                LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.view_driver_commonrouteitem, null);
                Point point = Global.getScreenSize(PassLongRouteMainActivity.this);
                ResolutionSet.instance.iterateChild(v, point.x, point.y);

	            holder = new STItemViewHolder();
	            v.setTag(holder);
            }
	        else
            {
	            holder = (STItemViewHolder)v.getTag();
            }

            TextView lblPath = null;
	        if (holder.lblPath == null)
		        holder.lblPath = (TextView) v.findViewById(R.id.lblPath);
	        lblPath = holder.lblPath;
            lblPath.setText(list.get(position).startcity + getResources().getString(R.string.addr_separator) + list.get(position).endcity);

            TextView lblDate = null;
	        if (holder.lblDate == null)
		        holder.lblDate = (TextView) v.findViewById(R.id.lblDate);
	        lblDate = holder.lblDate;
            lblDate.setText(list.get(position).create_time);

            ImageButton btnItem = null;
	        if (holder.btnItem == null)
		        holder.btnItem = (ImageButton) v.findViewById(R.id.btn_item);
	        btnItem = holder.btnItem;
	        btnItem.setTag(list.get(position));
	        btnItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    STCommonRoute newItem = (STCommonRoute) v.getTag();
                    Intent intent = new Intent(PassLongRouteMainActivity.this, PassLongSetRouteActivity.class);
                    intent.putExtra("IsUpdate", 1);
                    intent.putExtra("Data", newItem);
                    intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                    PassLongRouteMainActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                    startActivity(intent);
                }
            });
	        btnItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    STCommonRoute newItem = (STCommonRoute) v.getTag();
                    nDeletedID = newItem.uid;
                    dlgDelete = new DrvDailyRouteDeleteDialog(PassLongRouteMainActivity.this);
                    dlgDelete.setOnDismissListener(PassLongRouteMainActivity.this);
	                dlgDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dlgDelete.show();
                    return false;
                }
            });

            return v;
        }
    }
}

