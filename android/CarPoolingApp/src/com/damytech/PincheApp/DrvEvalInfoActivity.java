package com.damytech.PincheApp;

import android.content.Context;
import android.content.Intent;
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
import com.damytech.DataClasses.STPassEvalInfo;
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
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-9-19
 * Time: 下午6:47
 * To change this template use File | Settings | File Templates.
 */
public class DrvEvalInfoActivity extends SuperActivity
{
	private ImageButton btn_back = null;

	// Car and driver information
	private SmartImageView imgPhoto = null;
	private ImageView imgGender = null;
	private TextView txtAge = null;
	private TextView txtName = null;

	private SmartImageView carImg = null;
    private String carImgUrl = null;

	private TextView txtCarCareer = null;
	private TextView txtEvalGood = null;
	private TextView txtCarpoolCnt = null;

	private ImageView imgCarStyle = null;
	private ImageView imgCarBrand = null;
	private TextView txtCarBrand = null;
	private TextView txtCarType = null;
	private TextView txtCarColor = null;

    private TextView lblNoData = null;
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private PullToRefreshListView evalList = null;
	private EvalAdapter evalAdapter = null;
	private int pageno = 0;

	private ArrayList<STPassEvalInfo> arrEvals = new ArrayList<STPassEvalInfo>();
	private long driverid = 0;

	private RelativeLayout detailLayout = null;
	private TextView txtPassName = null, txtTime = null, txtContent = null;

	private ImageView imgGood = null, imgGoodIcon = null;
	private ImageView imgNormal = null, imgNormalIcon = null;
	private ImageView imgBad = null, imgBadIcon = null;
	private TextView lblGood = null, lblNormal = null, lblBad = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_pass_driverinfo);

		initControls();
		initResolution();
	}


	private void initControls()
	{
		driverid = getIntent().getLongExtra("driverid", 0);

		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});

		imgPhoto = (SmartImageView) findViewById(R.id.img_photo);
		imgPhoto.isCircular = true;
		imgPhoto.setImage(new SmartImage() {
			@Override
			public Bitmap getBitmap(Context context) {
				return BitmapFactory.decodeResource(getResources(), R.drawable.icon_appprice_over);
			}
		});

		carImg = (SmartImageView)findViewById(R.id.img_car);
		carImg.isCircular = true;
		carImg.setImage(new SmartImage() {
			@Override
			public Bitmap getBitmap(Context context) {
				return BitmapFactory.decodeResource(getResources(), R.drawable.icon_appprice_over);
			}
		});
        carImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrvEvalInfoActivity.this, DisplayCarImgActivity.class);
                intent.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
                intent.putExtra("img_url", carImgUrl);
                DrvEvalInfoActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
                startActivity(intent);
            }
        });

		imgGender = (ImageView)findViewById(R.id.imgSex);
		txtAge = (TextView)findViewById(R.id.lblAge);
		txtName = (TextView)findViewById(R.id.driver_name);
		txtCarCareer = (TextView)findViewById(R.id.driver_duration);
		txtEvalGood = (TextView)findViewById(R.id.driver_evaluation);
		txtCarpoolCnt = (TextView)findViewById(R.id.service_time_detail);
		imgCarStyle = (ImageView)findViewById(R.id.car_type);
		imgCarBrand = (ImageView)findViewById(R.id.car_logo);
		txtCarBrand = (TextView)findViewById(R.id.txt_carlogo);
		txtCarBrand.setVisibility(View.GONE);
		txtCarType = (TextView)findViewById(R.id.car_name);
		txtCarColor = (TextView)findViewById(R.id.car_color);

		evalList = (PullToRefreshListView)findViewById(R.id.viewData);
		evalAdapter = new EvalAdapter(DrvEvalInfoActivity.this, arrEvals);
		{
			evalList.setMode(PullToRefreshBase.Mode.BOTH);
			evalList.setOnRefreshListener(evalListListener);
			evalList.setAdapter(evalAdapter);
			evalList.getRefreshableView().setDivider(new ColorDrawable(Color.parseColor("#FFF1F1F1")));
			evalList.getRefreshableView().setCacheColorHint(Color.parseColor("#FFF1F1F1"));
		}


		detailLayout = (RelativeLayout)findViewById(R.id.detailed_layout);
		detailLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				detailLayout.setVisibility(View.GONE);
			}
		});
		detailLayout.setVisibility(View.GONE);

		txtPassName = (TextView)findViewById(R.id.txt_name);
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
		CommManager.getDriverInfo(Global.loadUserID(getApplicationContext()), driverid, Global.getIMEI(getApplicationContext()), driverinfo_handler);
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


	private AsyncHttpResponseHandler driverinfo_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject result = new JSONObject(content).getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONObject retdata = result.getJSONObject("retdata");

					long id = retdata.getLong("id");
					String name = retdata.getString("name");
					String img = retdata.getString("img");
					int gender = retdata.getInt("gender");
					int age = retdata.getInt("age");
					int drv_career = retdata.getInt("drv_career");
					String drv_career_desc = retdata.getString("drv_career_desc");
					int goodeval_rate = retdata.getInt("goodeval_rate");
					String goodeval_rate_desc = retdata.getString("goodeval_rate_desc");
					int carpool_count = retdata.getInt("carpool_count");
					String carpool_count_desc = retdata.getString("carpool_count_desc");

					JSONObject carinfo = retdata.getJSONObject("carinfo");
                    carImgUrl = carinfo.getString("carimg");
					String brand = carinfo.getString("brand");
					String type = carinfo.getString("type");
					int style = carinfo.getInt("style");
					String color = carinfo.getString("color");

					imgPhoto.setImageUrl(img, R.drawable.icon_appprice_over);
					txtName.setText(name);
					if (gender == ConstData.GENDER_MALE) {
						imgGender.setImageResource(R.drawable.bk_manmark);
						txtAge.setTextColor(getResources().getColor(R.color.TITLE_BACKCOLOR));
					} else {
						imgGender.setImageResource(R.drawable.bk_womanmark);
						txtAge.setTextColor(getResources().getColor(R.color.LIGHT_YELLOW_COLOR));
					}
					txtAge.setText("" + age);

					carImg.setImageUrl(carImgUrl, R.drawable.icon_appprice_over);
					txtCarCareer.setText(drv_career_desc);
					txtEvalGood.setText(getResources().getString(R.string.STR_HAOPINGLV) + " " + goodeval_rate_desc);
					txtCarpoolCnt.setText(carpool_count_desc);

					if (style == ConstData.CARSTYLE_JINGJIXING)
						imgCarStyle.setImageResource(R.drawable.safecar_sel);
					else if (style == ConstData.CARSTYLE_SHUSHIXING)
						imgCarStyle.setImageResource(R.drawable.second_car_green);
					else if (style == ConstData.CARSTYLE_HAOHUAXING)
						imgCarStyle.setImageResource(R.drawable.third_car_green);
					else if (style == ConstData.CARSTYLE_SHANGWUXING)
						imgCarStyle.setImageResource(R.drawable.luxurycar_sel);

					int carbrand = Global.getBrandImgFromName(brand);
					if (carbrand > 0)
					{
						imgCarBrand.setVisibility(View.VISIBLE);
						imgCarBrand.setImageResource(carbrand);
						txtCarBrand.setVisibility(View.GONE);
					}
					else
					{
						imgCarBrand.setVisibility(View.GONE);
						txtCarBrand.setVisibility(View.VISIBLE);
						txtCarBrand.setText(brand);
					}

					txtCarType.setText(type);
					txtCarColor.setText(color);

					// Evaluation array
					JSONArray arrJSONEvals = retdata.getJSONArray("eval");
					for (int i = 0; i < arrJSONEvals.length(); i++)
					{
						JSONObject jsonItem = arrJSONEvals.getJSONObject(i);
						STPassEvalInfo evalItem = STPassEvalInfo.decodeFromJSON(jsonItem);
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
					Global.showAdvancedToast(DrvEvalInfoActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(DrvEvalInfoActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private AsyncHttpResponseHandler pagedEvalInfoHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
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
						STPassEvalInfo evalItem = STPassEvalInfo.decodeFromJSON(jsonItem);

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
					Global.showAdvancedToast(DrvEvalInfoActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			evalList.onRefreshComplete();
			Global.showAdvancedToast(DrvEvalInfoActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};



	private AsyncHttpResponseHandler latestEvalInfoHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
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
						STPassEvalInfo evalItem = STPassEvalInfo.decodeFromJSON(jsonItem);

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
					Global.showAdvancedToast(DrvEvalInfoActivity.this, szRetMsg, Gravity.CENTER);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			evalList.onRefreshComplete();
			Global.showAdvancedToast(DrvEvalInfoActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};




	private void getPagedEvals()
	{
		CommManager.getDriverPagedEvalInfo(Global.loadUserID(getApplicationContext()), driverid, pageno, Global.getIMEI(getApplicationContext()), pagedEvalInfoHandler);
	}


	private void getLatestEvals()
	{
		if (arrEvals.isEmpty())
			CommManager.getDriverPagedEvalInfo(Global.loadUserID(getApplicationContext()), driverid, pageno, Global.getIMEI(getApplicationContext()), pagedEvalInfoHandler);
		else
			CommManager.getDriverLatestEvalInfo(Global.loadUserID(getApplicationContext()), driverid, arrEvals.get(0).uid, Global.getIMEI(getApplicationContext()), latestEvalInfoHandler);
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


	private class EvalAdapter extends ArrayAdapter<STPassEvalInfo>
	{
		public EvalAdapter(Context context, List<STPassEvalInfo> objects) {
			super(context, 0, objects);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			final STPassEvalInfo evalItem = arrEvals.get(position);

			STEvalViewHolder holder = null;
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

				holder = new STEvalViewHolder();
				convertView.setTag(holder);
			}
			else
			{
				holder = (STEvalViewHolder)convertView.getTag();
			}

			// Set name
			TextView lblName = null;
			if (holder.lblName == null)
				holder.lblName = (TextView)convertView.findViewById(R.id.lblName);
			lblName = holder.lblName;
			lblName.setText(evalItem.driver_name);

			// Set level
			TextView lblEvalInfo = null;
			if (holder.lblEvalInfo == null)
				holder.lblEvalInfo = (TextView)convertView.findViewById(R.id.lblEvalVal);
			lblEvalInfo = holder.lblEvalInfo;
			lblEvalInfo.setText(evalItem.eval_desc);

			// Set date
			TextView lblDate = null;
			if (holder.lblDate == null)
				holder.lblDate = (TextView)convertView.findViewById(R.id.lblDate);
			lblDate = holder.lblDate;
			lblDate.setText(evalItem.time);

			// Set evaluation
			TextView lblEval = null;
			if (holder.lblEval == null)
				holder.lblEval = (TextView)convertView.findViewById(R.id.lblEval);
			lblEval = holder.lblEval;
			lblEval.setText(evalItem.contents);

			// Background button
			ImageButton btnItem = null;
			if (holder.btnItem == null)
				holder.btnItem = (ImageButton)convertView.findViewById(R.id.btn_item);
			btnItem = holder.btnItem;
			btnItem.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onSelectItem(evalItem);
				}
			});

			return convertView;
		}
	}


	private class STEvalViewHolder
	{
		TextView lblName = null;
		TextView lblEvalInfo = null;
		TextView lblDate = null;
		TextView lblEval = null;
		ImageButton btnItem = null;
	}


	private void onSelectItem(STPassEvalInfo evalInfo)
	{
		txtPassName.setText(evalInfo.driver_name);
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
