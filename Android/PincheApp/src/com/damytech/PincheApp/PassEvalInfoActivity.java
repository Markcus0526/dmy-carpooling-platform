package com.damytech.PincheApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.damytech.DataClasses.STDrvEvalInfo;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshBase;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshListView;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.image.SmartImage;
import com.damytech.Utils.image.SmartImageView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PassEvalInfoActivity extends SuperActivity
{
	private ImageButton btn_back = null;

	private SmartImageView imgPhoto = null;

	private ImageView img_verified = null;
	private TextView txt_verified = null;
	private TextView txt_evalgood = null;
	private TextView txt_carpoolcnt = null;
	private ImageView img_gender = null;
	private TextView txt_age = null;
	private TextView txt_psg_info = null;

	private PullToRefreshListView evalList = null;
	private EvalAdapter evalAdapter = new EvalAdapter();
	private int pageno = 0;

	private ArrayList<STDrvEvalInfo> arrEvals = new ArrayList<STDrvEvalInfo>();
	private long passid = 0;

	private RelativeLayout detailLayout = null;
	private TextView txtName = null, txtTime = null, txtContent = null;

	private ImageView imgGood = null, imgGoodIcon = null;
	private ImageView imgNormal = null, imgNormalIcon = null;
	private ImageView imgBad = null, imgBadIcon = null;
	private TextView lblGood = null, lblNormal = null, lblBad = null;

    private TextView lblNoData = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_driver_passengerinfo);

		initControls();
		initResolution();
	}


	private void initControls()
	{
		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});

		imgPhoto = (SmartImageView) findViewById(R.id.img_psg_photo);
		imgPhoto.isCircular = true;
		imgPhoto.setImage(new SmartImage() {
			@Override
			public Bitmap getBitmap(Context context) {
				return BitmapFactory.decodeResource(getResources(), R.drawable.icon_appprice_over);
			}
		});

		passid = getIntent().getLongExtra("passid", 0);

		img_verified = (ImageView)findViewById(R.id.imgSecurity);
		txt_verified = (TextView)findViewById(R.id.lblSecurity);
		txt_evalgood = (TextView)findViewById(R.id.lblRatio);
		txt_carpoolcnt = (TextView)findViewById(R.id.lblCarpoolCount);
		img_gender = (ImageView)findViewById(R.id.imgSex);
		txt_age = (TextView)findViewById(R.id.lblAge);
		txt_psg_info = (TextView)findViewById(R.id.lblName);

		evalList = (PullToRefreshListView)findViewById(R.id.eval_list);
		{
			evalList.setMode(PullToRefreshBase.Mode.BOTH);
			evalList.setOnRefreshListener(evalListListener);
			evalList.setAdapter(evalAdapter);
			evalList.getRefreshableView().setDivider(new ColorDrawable(Color.parseColor("#FFCCCCCC")));
			evalList.getRefreshableView().setCacheColorHint(Color.parseColor("#FFCCCCCC"));
		}


		detailLayout = (RelativeLayout)findViewById(R.id.detail_layout);
		detailLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				detailLayout.setVisibility(View.GONE);
			}
		});
		detailLayout.setVisibility(View.GONE);

		txtName = (TextView)findViewById(R.id.txt_name);
		txtTime = (TextView)findViewById(R.id.txt_time);
		txtContent = (TextView)findViewById(R.id.txt_content);

		imgGood = (ImageView) findViewById(R.id.imgGoodEval);
		imgGoodIcon = (ImageView) findViewById(R.id.imgGoodEvalIcon);
		lblGood = (TextView) findViewById(R.id.lblGoodEval);
		imgNormal = (ImageView) findViewById(R.id.imgNormalEval);
		imgNormalIcon = (ImageView) findViewById(R.id.imgNormalEvalIcon);
		lblNormal = (TextView) findViewById(R.id.lblNormalEval);
		imgBad = (ImageView) findViewById(R.id.imgBadEval);
		imgBadIcon = (ImageView) findViewById(R.id.imgBadEvalIcon);
		lblBad = (TextView) findViewById(R.id.lblBadEval);


        lblNoData = (TextView) findViewById(R.id.lblNoData);

		startProgress();
		CommManager.getPassengerInfo(Global.loadUserID(getApplicationContext()), passid, Global.getIMEI(getApplicationContext()), passinfo_handler);
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


	private AsyncHttpResponseHandler passinfo_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = result.getJSONObject("retdata");

					imgPhoto.setImageUrl(retdata.getString("img"), R.drawable.icon_appprice_over);

					if (retdata.getInt("pverified") == 1)
					{
						img_verified.setImageResource(R.drawable.bk_person_verified);
					}
					else
					{
						img_verified.setImageResource(R.drawable.bk_person_notverified);
					}

					txt_verified.setText(retdata.getString("pverified_desc"));
					txt_evalgood.setText(getResources().getString(R.string.STR_HAOPINGLV) + " " + retdata.getString("goodeval_rate_desc"));
					txt_carpoolcnt.setText(getResources().getString(R.string.STR_PINCHECISHU) + " " + retdata.getString("carpool_count_desc"));

					if (retdata.getInt("gender") == 0)
						img_gender.setImageResource(R.drawable.bk_manmark);
					else
						img_gender.setImageResource(R.drawable.bk_womanmark);

					txt_age.setText("" + retdata.getInt("age"));
					txt_psg_info.setText(retdata.getString("name"));

					// Evaluation array
					JSONArray arrJSONEvals = retdata.getJSONArray("eval");
					for (int i = 0; i < arrJSONEvals.length(); i++)
					{
						JSONObject jsonItem = arrJSONEvals.getJSONObject(i);
						STDrvEvalInfo evalItem = STDrvEvalInfo.decodeFromJSON(jsonItem);

						arrEvals.add(evalItem);
					}

					pageno = arrEvals.size() / Global.getPageItemCount();

					evalAdapter.notifyDataSetChanged();


                    // show/hide hint
                    if (arrEvals.size() > 0)
                    {
                        lblNoData.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        lblNoData.setVisibility(View.VISIBLE);
                    }
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(PassEvalInfoActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(PassEvalInfoActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private AsyncHttpResponseHandler pagedEvalInfoHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			evalList.onRefreshComplete();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONArray retdata = result.getJSONArray("retdata");
					for (int i = 0; i < retdata.length(); i++)
					{
						JSONObject jsonItem = retdata.getJSONObject(i);
						STDrvEvalInfo evalItem = STDrvEvalInfo.decodeFromJSON(jsonItem);

						for (int j = 0; j < arrEvals.size(); j++)
						{
							if (arrEvals.get(j).uid == evalItem.uid)
							{
								arrEvals.remove(j);
								break;
							}
						}

						arrEvals.add(evalItem);
					}

					pageno = arrEvals.size() / Global.getPageItemCount();

					evalAdapter.notifyDataSetChanged();

                    // show/hide hint
                    if (arrEvals.size() > 0)
                    {
                        lblNoData.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        lblNoData.setVisibility(View.VISIBLE);
                    }
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassEvalInfoActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			evalList.onRefreshComplete();
			Global.showAdvancedToast(PassEvalInfoActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};



	private AsyncHttpResponseHandler latestEvalInfoHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			evalList.onRefreshComplete();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetMsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONArray retdata = result.getJSONArray("retdata");
					for (int i = 0; i < retdata.length(); i++)
					{
						JSONObject jsonItem = retdata.getJSONObject(i);
						STDrvEvalInfo evalItem = STDrvEvalInfo.decodeFromJSON(jsonItem);

						for (int j = 0; j < arrEvals.size(); j++)
						{
							if (arrEvals.get(j).uid == evalItem.uid)
							{
								arrEvals.remove(j);
								break;
							}
						}

						arrEvals.add(0, evalItem);
					}

					pageno = arrEvals.size() / Global.getPageItemCount();

					evalAdapter.notifyDataSetChanged();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetMsg);
				}
				else
				{
					Global.showAdvancedToast(PassEvalInfoActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			evalList.onRefreshComplete();
			Global.showAdvancedToast(PassEvalInfoActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};




	private void getPagedEvals()
	{
		CommManager.getPassengerPagedEvalInfo(Global.loadUserID(getApplicationContext()), passid, pageno, Global.getIMEI(getApplicationContext()), pagedEvalInfoHandler);
	}


	private void getLatestEvals()
	{
		if (arrEvals.isEmpty())
			CommManager.getPassengerPagedEvalInfo(Global.loadUserID(getApplicationContext()), passid, pageno, Global.getIMEI(getApplicationContext()), pagedEvalInfoHandler);
		else
			CommManager.getPassengerLatestEvalInfo(Global.loadUserID(getApplicationContext()), passid, arrEvals.get(0).uid, Global.getIMEI(getApplicationContext()), latestEvalInfoHandler);
	}


	private PullToRefreshBase.OnRefreshListener evalListListener = new PullToRefreshBase.OnRefreshListener() {
		@Override
		public void onRefresh(PullToRefreshBase refreshView) {
			PullToRefreshBase.Mode mode = refreshView.getCurrentMode();
			if (mode == PullToRefreshBase.Mode.PULL_FROM_START)
				getLatestEvals();
			else
				getPagedEvals();
		}
	};


	private class STEvalViewHolder
	{
		TextView lblName = null;
		TextView lblEvalInfo = null;
		TextView lblDate = null;
		TextView lblEval = null;
		ImageButton btnItem = null;
	}


	private class EvalAdapter extends BaseAdapter
	{
		@Override
		public int getCount() {
			return arrEvals.size();
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
			return arrEvals.get(position);
		}

		@Override
		public boolean isEmpty() {
			if (arrEvals == null)
				return true;

			return arrEvals.isEmpty();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			final STDrvEvalInfo evalItem = arrEvals.get(position);
			STEvalViewHolder viewHolder = null;
			if (convertView == null)
			{
				int nWidth = 460, nHeight = 120, nYMargin = 10;
				RelativeLayout itemLayout = new RelativeLayout(evalList.getContext());
				AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ResolutionSet.getBaseWidth(), nHeight + nYMargin * 2);
				itemLayout.setLayoutParams(layoutParams);

				RelativeLayout itemTemplate = (RelativeLayout)getLayoutInflater().inflate(R.layout.view_driver_passengerinfolitem, null);
				RelativeLayout itemView = (RelativeLayout)itemTemplate.findViewById(R.id.parent_layout);
				RelativeLayout.LayoutParams item_layoutParams = new RelativeLayout.LayoutParams(nWidth, nHeight);
				item_layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				itemView.setLayoutParams(item_layoutParams);
				itemLayout.addView(itemView);

				ResolutionSet.instance.iterateChild(itemLayout, mScrSize.x, mScrSize.y);

				convertView = itemLayout;

				viewHolder = new STEvalViewHolder();
				convertView.setTag(viewHolder);
			}
			else
			{
				viewHolder = (STEvalViewHolder)convertView.getTag();
			}

			// Set name
			TextView lblName = null;
			if (viewHolder.lblName == null)
				viewHolder.lblName = (TextView)convertView.findViewById(R.id.lblName);
			lblName = viewHolder.lblName;
			lblName.setText(evalItem.pass_name);

			// Set level
			TextView lblEvalInfo = null;
			if (viewHolder.lblEvalInfo == null)
				viewHolder.lblEvalInfo = (TextView)convertView.findViewById(R.id.lblEvalVal);
			lblEvalInfo = viewHolder.lblEvalInfo;
			lblEvalInfo.setText(evalItem.eval_desc);

			// Set date
			TextView lblDate = null;
			if (viewHolder.lblDate == null)
				viewHolder.lblDate = (TextView)convertView.findViewById(R.id.lblDate);
            lblDate = viewHolder.lblDate;
			lblDate.setText(evalItem.time);

			// Set evaluation
			TextView lblEval = null;
			if (viewHolder.lblEval == null)
				viewHolder.lblEval = (TextView)convertView.findViewById(R.id.lblEval);
			lblEval = viewHolder.lblEval;
			lblEval.setText(evalItem.contents);

			// Background button
			ImageButton btnItem = null;
			if (viewHolder.btnItem == null)
				viewHolder.btnItem = (ImageButton)convertView.findViewById(R.id.btn_item);
			btnItem = viewHolder.btnItem;
			btnItem.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onSelectItem(evalItem);
				}
			});

			return convertView;
		}
	}


	private void onSelectItem(STDrvEvalInfo evalInfo)
	{
		txtName.setText(evalInfo.pass_name);
		txtTime.setText(evalInfo.time);
		txtContent.setText(evalInfo.contents);

		switch(evalInfo.eval)
		{
			case ConstData.EVALUATE_GOOD:
				imgGood.setImageResource(R.drawable.radiobox_roundsel);
				imgNormal.setImageResource(R.drawable.radiobox_roundnormal);
				imgBad.setImageResource(R.drawable.radiobox_roundnormal);
				break;
			case ConstData.EVALUATE_NORMAL:
				imgGood.setImageResource(R.drawable.radiobox_roundnormal);
				imgNormal.setImageResource(R.drawable.radiobox_roundsel);
				imgBad.setImageResource(R.drawable.radiobox_roundnormal);
				break;
			case ConstData.EVALUATE_BAD:
			default:
				imgGood.setImageResource(R.drawable.radiobox_roundnormal);
				imgNormal.setImageResource(R.drawable.radiobox_roundnormal);
				imgBad.setImageResource(R.drawable.radiobox_roundsel);
				break;
		}

		detailLayout.setVisibility(View.VISIBLE);
	}

}
