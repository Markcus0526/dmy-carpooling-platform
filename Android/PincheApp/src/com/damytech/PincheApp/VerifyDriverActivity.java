package com.damytech.PincheApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import com.damytech.DataClasses.ConstData;
import com.damytech.DataClasses.STBrand;
import com.damytech.DataClasses.STColor;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.Misc.CommManager;
import com.damytech.Misc.Global;
import com.damytech.Utils.HorizontalPager;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-8
 * Time: 上午4:01
 * To change this template use File | Settings | File Templates.
 */
public class VerifyDriverActivity extends SuperActivity
{
	ImageButton btn_back = null;
	private Button btnUpload = null;
	private ImageView imgIDFore = null, imgIDBack = null;
	private ImageButton btnIDFore = null, btnIDBack = null;
	private ImageView imgCar = null;
	private Button btnCar = null;
	private ImageView imgLicenseFore = null, imgLicenseBack = null;
	private ImageButton btnLicenseFore = null, btnLicenseBack = null;

	ImageView imgBrand = null; TextView lblBrand = null;
	ImageView imgCarType = null; TextView lblCarType = null;
	ImageView imgColor = null; TextView lblColor = null;

	private Bitmap bmpIDForeImage = null, bmpIDBackImage = null;
	private Bitmap bmpCarImage = null;
	private Bitmap bmpLicenseForeImage = null, bmpLicenseBackImage = null;
	private String strBrand = "", strType = "", strColor = "";

	private HorizontalPager hor_pager = null;
	private RelativeLayout rlStage1, rlStage2, rlStage3;

	private int REQCODE_IDFOREIMAGE = 1;
	private int REQCODE_IDBACKIMAGE = 2;
	private int REQCODE_CARIMAGE = 3;
	private int REQCODE_LICENSEFOREIMAGE = 4;
	private int REQCODE_LICENSEBACKIMAGE = 5;

	private int nCurrentScreen = 0;

	final int ADAPTER_BRAND = 0;
	final int ADAPTER_TYPE = 1;
	final int ADAPTER_COLOR = 2;
	int nCurAdapter = ADAPTER_BRAND;
	int nBrandID = 0;
	ArrayList<STColor> arrColor = new ArrayList<STColor>();
	ArrayList<STBrand> arrBrand = new ArrayList<STBrand>();

	RelativeLayout listLayout = null;
	ListView adapterListView = null;
	ItemAdapter adapter = null;
	TextView txtListTitle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_verifydriver);

		initControls();
		initResolution();
	}

	private void initControls()
	{
		txtListTitle = (TextView)findViewById(R.id.txt_listtitle);

		hor_pager = (HorizontalPager) findViewById(R.id.viewStages);
		hor_pager.setVisibility(View.VISIBLE);
		hor_pager.setHorizontalScrollBarEnabled(false);
		hor_pager.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		hor_pager.setOnScreenSwitchListener(new HorizontalPager.OnScreenSwitchListener() {
			@Override
			public void onScreenSwitched(int screen) {
				if (nCurrentScreen == 2)
				{
					btnUpload.setText(getString(R.string.STR_PERSONINFO_SUBMIT));
				}
			}
		});

		rlStage1 = (RelativeLayout) findViewById(R.id.rlStage1);
		rlStage1.setVisibility(View.VISIBLE);
		rlStage2 = (RelativeLayout) findViewById(R.id.rlStage2);
		rlStage2.setVisibility(View.VISIBLE);
		rlStage3 = (RelativeLayout) findViewById(R.id.rlStage3);
		rlStage3.setVisibility(View.VISIBLE);

		ViewGroup parentView = null;
		parentView = (ViewGroup) rlStage1.getParent();
		parentView.removeView(rlStage1);
		parentView = (ViewGroup) rlStage2.getParent();
		parentView.removeView(rlStage2);
		parentView = (ViewGroup) rlStage3.getParent();
		parentView.removeView(rlStage3);

		hor_pager.addView(rlStage1);
		hor_pager.addView(rlStage2);
		hor_pager.addView(rlStage3);

		imgIDFore = (ImageView)findViewById(R.id.img_idcard_fore);
		imgIDBack = (ImageView)findViewById(R.id.img_idcard_back);
		btnIDFore = (ImageButton)findViewById(R.id.btn_idcard_foreimage);
		btnIDBack = (ImageButton)findViewById(R.id.btn_idcard_backimage);
		imgCar = (ImageView) findViewById(R.id.imgCarPhoto);
		btnCar = (Button) findViewById(R.id.btnCarPhoto);
		imgLicenseFore = (ImageView)findViewById(R.id.img_licensecard_fore);
		imgLicenseBack = (ImageView)findViewById(R.id.img_licensecard_back);
		btnLicenseFore = (ImageButton)findViewById(R.id.btn_licensecard_foreimage);
		btnLicenseBack = (ImageButton)findViewById(R.id.btn_licensecard_backimage);

		lblBrand = (TextView) findViewById(R.id.lblBrand);
		lblBrand.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nCurAdapter = ADAPTER_BRAND;
				txtListTitle.setText(getResources().getString(R.string.chexing));
				adapter.notifyDataSetChanged();
				listLayout.setVisibility(View.VISIBLE);
				adapterListView.setSelectionAfterHeaderView();
			}
		});
		imgBrand = (ImageView) findViewById(R.id.imgBrand);
		imgBrand.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nCurAdapter = ADAPTER_BRAND;
				txtListTitle.setText(getResources().getString(R.string.chexing));
				adapter.notifyDataSetChanged();
				listLayout.setVisibility(View.VISIBLE);
				adapterListView.setSelectionAfterHeaderView();
			}
		});

		lblCarType = (TextView) findViewById(R.id.lblCarType);
		lblCarType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nCurAdapter = ADAPTER_TYPE;
				txtListTitle.setText(getResources().getString(R.string.chepaixing));
				adapter.notifyDataSetChanged();
				listLayout.setVisibility(View.VISIBLE);
				adapterListView.setSelectionAfterHeaderView();
			}
		});
		imgCarType = (ImageView) findViewById(R.id.imgCarType);
		imgCarType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nCurAdapter = ADAPTER_TYPE;
				txtListTitle.setText(getResources().getString(R.string.chepaixing));
				adapter.notifyDataSetChanged();
				listLayout.setVisibility(View.VISIBLE);
				adapterListView.setSelectionAfterHeaderView();
			}
		});

		lblColor = (TextView) findViewById(R.id.lblColor);
		lblColor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nCurAdapter = ADAPTER_COLOR;
				txtListTitle.setText(getResources().getString(R.string.yanse));
				adapter.notifyDataSetChanged();
				listLayout.setVisibility(View.VISIBLE);
				adapterListView.setSelectionAfterHeaderView();
			}
		});
		imgColor = (ImageView) findViewById(R.id.imgColor);
		imgColor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nCurAdapter = ADAPTER_COLOR;
				txtListTitle.setText(getResources().getString(R.string.yanse));
				adapter.notifyDataSetChanged();
				listLayout.setVisibility(View.VISIBLE);
				adapterListView.setSelectionAfterHeaderView();
			}
		});

		listLayout = (RelativeLayout)findViewById(R.id.rlListData);
		listLayout.setClickable(true);
		listLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listLayout.setVisibility(View.GONE);
			}
		});
		listLayout.setVisibility(View.INVISIBLE);

		adapterListView = (ListView)findViewById(R.id.listData);
		adapter = new ItemAdapter();
		adapterListView.setAdapter(adapter);
		adapterListView.setDivider(new ColorDrawable(Color.WHITE));
		adapterListView.setCacheColorHint(Color.WHITE);

		btnUpload = (Button)findViewById(R.id.btn_next);

		btnIDFore.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onSelectIDForeImage();
			}
		});
		btnIDBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onSelectIDBackImage();
			}
		});
		btnCar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSelectCarImage();
			}
		});
		btnLicenseFore.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onSelectLicenseForeImage();
			}
		});
		btnLicenseBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				onSelectLicenseBackImage();
			}
		});
		btnUpload.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				if (hor_pager != null)
				{
					if (nCurrentScreen == 0)
					{
						if (bmpIDForeImage == null)
						{
							Global.showAdvancedToast(VerifyDriverActivity.this, getString(R.string.STR_VERIFYDRIVER_IDFOREIMAGE_EMPTY), Gravity.CENTER);
							return;
						}
						if (bmpIDBackImage == null)
						{
							Global.showAdvancedToast(VerifyDriverActivity.this, getString(R.string.STR_VERIFYDRIVER_IDBACKIMAGE_EMPTY), Gravity.CENTER);
							return;
						}

						nCurrentScreen++;
						hor_pager.setCurrentScreen(nCurrentScreen, true);

						startProgress();
						CommManager.getBrandsAndColors(brandcolor_handler);

						return;
					}
					else if (nCurrentScreen == 1)
					{
//						if (bmpCarImage == null)
//						{
//							Global.showAdvancedToast(VerifyDriverActivity.this, getString(R.string.STR_VERIFYDRIVER_CARIMAGE_EMPTY), Gravity.CENTER);
//							return;
//						}
						if (strBrand.length() == 0)
						{
							Global.showAdvancedToast(VerifyDriverActivity.this, getString(R.string.STR_SELECT_BRAND), Gravity.CENTER);
							return;
						}
						if (strType.length() == 0)
						{
							Global.showAdvancedToast(VerifyDriverActivity.this, getString(R.string.STR_SELECT_TYPE), Gravity.CENTER);
							return;
						}
						if (strColor.length() == 0)
						{
							Global.showAdvancedToast(VerifyDriverActivity.this, getString(R.string.STR_SELECT_COLOR), Gravity.CENTER);
							return;
						}
						nCurrentScreen++;
						hor_pager.setCurrentScreen(nCurrentScreen, true);

						return;
					}
					else if (nCurrentScreen == 2)
					{
						if (bmpLicenseForeImage == null)
						{
							Global.showAdvancedToast(VerifyDriverActivity.this, getString(R.string.STR_VERIFYDRIVER_LICENSEFOREIMAGE_EMPTY), Gravity.CENTER);
							return;
						}

						if (bmpLicenseBackImage == null)
						{
							Global.showAdvancedToast(VerifyDriverActivity.this, getString(R.string.STR_VERIFYDRIVER_LICENSEBACKIMAGE_EMPTY), Gravity.CENTER);
							return;
						}
					}
				}

				onClickUpload();
			}
		});

		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {

				if (nCurrentScreen > 0)
				{
					nCurrentScreen--;
					hor_pager.setCurrentScreen(nCurrentScreen, true);
				}
				else
				{
					finishWithAnimation();
				}

//				if (listLayout.getVisibility() == View.VISIBLE)
//				{
//					listLayout.setVisibility(View.GONE);
//					return true;
//				}
//				if (hor_pager.getCurrentScreen() > 0)
//				{
//					hor_pager.setCurrentScreen(hor_pager.getCurrentScreen() - 1, true);
//					return true;
//				}

			}
		});

		setResult(RESULT_CANCELED);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (listLayout.getVisibility() == View.VISIBLE)
			{
				listLayout.setVisibility(View.GONE);
				return true;
			}

			if (nCurrentScreen > 0)
			{
				nCurrentScreen--;
				hor_pager.setCurrentScreen(nCurrentScreen, true);
                return true;
			}
			else
			{
				finishWithAnimation();
			}
		}

		return super.onKeyDown(keyCode, event);
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

	private void onSelectIDForeImage()
	{
		Intent intent = new Intent(VerifyDriverActivity.this, SelectPhotoActivity.class);
		VerifyDriverActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
		startActivityForResult(intent, REQCODE_IDFOREIMAGE);
	}

	private void onSelectIDBackImage()
	{
		Intent intent = new Intent(VerifyDriverActivity.this, SelectPhotoActivity.class);
		VerifyDriverActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
		startActivityForResult(intent, REQCODE_IDBACKIMAGE);
	}

	private void onSelectCarImage()
	{
		Intent intent = new Intent(VerifyDriverActivity.this, SelectPhotoActivity.class);
		VerifyDriverActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
		startActivityForResult(intent, REQCODE_CARIMAGE);
	}

	private void onSelectLicenseForeImage()
	{
		Intent intent = new Intent(VerifyDriverActivity.this, SelectPhotoActivity.class);
		VerifyDriverActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
		startActivityForResult(intent, REQCODE_LICENSEFOREIMAGE);
	}

	private void onSelectLicenseBackImage()
	{
		Intent intent = new Intent(VerifyDriverActivity.this, SelectPhotoActivity.class);
		VerifyDriverActivity.this.getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_NONE());
		startActivityForResult(intent, REQCODE_LICENSEBACKIMAGE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);	//To change body of overridden methods use File | Settings | File Templates.

		if (resultCode != RESULT_OK)
			return;

		if (requestCode == REQCODE_IDFOREIMAGE)
			updateUserImage(data, 0, true);
		else if (requestCode == REQCODE_IDBACKIMAGE)
			updateUserImage(data, 0, false);
		else if (requestCode == REQCODE_CARIMAGE)
			updateUserImage(data, 1, true);
		else if (requestCode == REQCODE_LICENSEFOREIMAGE)
			updateUserImage(data, 2, true);
		else if (requestCode == REQCODE_LICENSEBACKIMAGE)
			updateUserImage(data, 2, false);
	}

	/* Image mamagement methods */
	private void updateUserImage(Intent data, int nType, boolean isFore)
	{
		if (data.getIntExtra(SelectPhotoActivity.szRetCode, -999) == SelectPhotoActivity.nRetSuccess)
		{
			String szPath = "";
			Object objPath = data.getExtras().get(SelectPhotoActivity.szRetPath);

			if (objPath != null)
				szPath = (String)objPath;

			if (szPath != null && !szPath.equals(""))
				updateUserImageWithPath(szPath, nType, isFore);
		}
	}

	private void updateUserImageWithPath(String szPath, int nType, boolean isFore)
	{
		startProgress();

		try {
			/* Update user photo info view */
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(szPath, options);

			if (bitmap != null)
			{
				int nWidth = bitmap.getWidth(), nHeight = bitmap.getHeight();
				int nScaledWidth = 0, nScaledHeight = 0;
				if (nWidth > nHeight)
				{
					nScaledWidth = SelectPhotoActivity.IMAGE_WIDTH;
					nScaledHeight = nScaledWidth * nHeight / nWidth;
				}
				else
				{
					nScaledHeight = SelectPhotoActivity.IMAGE_HEIGHT;
					nScaledWidth = nScaledHeight * nWidth / nHeight;
				}

				switch (nType)
				{
					case 0:
						if (isFore)
						{
							bmpIDForeImage = Bitmap.createScaledBitmap(bitmap, nScaledWidth, nScaledHeight, false);
							imgIDFore.setImageBitmap(bmpIDForeImage);
						}
						else
						{
							bmpIDBackImage = Bitmap.createScaledBitmap(bitmap, nScaledWidth, nScaledHeight, false);
							imgIDBack.setImageBitmap(bmpIDBackImage);
						}
						break;
					case 1:
						bmpCarImage = Bitmap.createScaledBitmap(bitmap, nScaledWidth, nScaledHeight, false);
						imgCar.setImageBitmap(bmpCarImage);
						break;
					case 2:
						if (isFore)
						{
							bmpLicenseForeImage = Bitmap.createScaledBitmap(bitmap, nScaledWidth, nScaledHeight, false);
							imgLicenseFore.setImageBitmap(bmpLicenseForeImage);
						}
						else
						{
							bmpLicenseBackImage = Bitmap.createScaledBitmap(bitmap, nScaledWidth, nScaledHeight, false);
							imgLicenseBack.setImageBitmap(bmpLicenseBackImage);
						}
						break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		stopProgress();
	}

//	private void updateUserImageWithUri(Uri uri, int nType, boolean isFore)
//	{
//		BufferedInputStream bis = null;
//		InputStream is = null;
//		Bitmap bmp = null;
//		URLConnection conn = null;
//
//		startProgress();
//
//		try {
//			/* Update user photo info view */
//			String szUrl = uri.toString();
//
//			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//
//			is = getContentResolver().openInputStream(uri);
//			bmp = BitmapFactory.decodeStream(is, null, options);
//
//			int nWidth = bmp.getWidth(), nHeight = bmp.getHeight();
//			int nScaledWidth = 0, nScaledHeight = 0;
//			if (nWidth > nHeight)
//			{
//				nScaledWidth = SelectPhotoActivity.IMAGE_WIDTH;
//				nScaledHeight = nScaledWidth * nHeight / nWidth;
//			}
//			else
//			{
//				nScaledHeight = SelectPhotoActivity.IMAGE_HEIGHT;
//				nScaledWidth = nScaledHeight * nWidth / nHeight;
//			}
//
//			switch (nType)
//			{
//				case 0:
//					if (isFore)
//					{
//						bmpIDForeImage = Bitmap.createScaledBitmap(bmp, nScaledWidth, nScaledHeight, false);
//						imgIDFore.setImageBitmap(bmpIDForeImage);
//					}
//					else
//					{
//						bmpIDBackImage = Bitmap.createScaledBitmap(bmp, nScaledWidth, nScaledHeight, false);
//						imgIDBack.setImageBitmap(bmpIDBackImage);
//					}
//					break;
//				case 1:
//					bmpCarImage = Bitmap.createScaledBitmap(bmp, nScaledWidth, nScaledHeight, false);
//					imgCar.setImageBitmap(bmpCarImage);
//					break;
//				case 2:
//					if (isFore)
//					{
//						bmpLicenseForeImage = Bitmap.createScaledBitmap(bmp, nScaledWidth, nScaledHeight, false);
//						imgLicenseFore.setImageBitmap(bmpLicenseForeImage);
//					}
//					else
//					{
//						bmpLicenseBackImage = Bitmap.createScaledBitmap(bmp, nScaledWidth, nScaledHeight, false);
//						imgLicenseBack.setImageBitmap(bmpLicenseBackImage);
//					}
//					break;
//			}
//		}
//		catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		finally {
//			stopProgress();
//			if (bis != null) {
//				try {
//					bis.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//
//			if (is != null) {
//				try {
//					is.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
	/************************************************************************************************/

	private void onClickUpload()
	{
		startProgress();
		CommManager.verifyDriver(Global.loadUserID(getApplicationContext()),
				Global.encodeWithBase64(bmpLicenseForeImage),
				Global.encodeWithBase64(bmpLicenseBackImage),
				strBrand,
				strType,
				strColor,
				Global.encodeWithBase64(bmpCarImage),
				Global.encodeWithBase64(bmpIDForeImage),
				Global.encodeWithBase64(bmpIDBackImage),
				Global.getIMEI(getApplicationContext()),
				upload_handler);
	}

	private AsyncHttpResponseHandler upload_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					Global.showAdvancedToast(VerifyDriverActivity.this, getResources().getString(R.string.STR_VERIFYDRIVER_APPLY_SUCCESS), Gravity.CENTER);
					setResult(RESULT_OK);
					finishWithAnimation();
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(VerifyDriverActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(VerifyDriverActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	private AsyncHttpResponseHandler brandcolor_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content) {
			super.onSuccess(content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();

			try {
				JSONObject jsonObj = new JSONObject(content);
				JSONObject result = jsonObj.getJSONObject("result");

				int nRetcode = result.getInt("retcode");
				String szRetmsg = result.getString("retmsg");

				if (nRetcode == ConstData.ERR_CODE_NONE)
				{
					JSONObject object = result.getJSONObject("retdata");
					JSONArray jsonArrayBrand = object.getJSONArray("brands");
					for (int i = 0; i < jsonArrayBrand.length(); i++)
					{
						STBrand newItem = new STBrand();
						JSONObject objItem = jsonArrayBrand.getJSONObject(i);
						newItem.uid = objItem.getLong("id");
						newItem.name = objItem.getString("name");
						JSONArray arr = objItem.getJSONArray("types");
						for (int j = 0; j < arr.length(); j++)
						{
							STBrand.STStyle newType = new STBrand.STStyle();
							JSONObject objType = arr.getJSONObject(j);
							newType.uid = objType.getLong("id");
							newType.name = objType.getString("name");
							newType.style = objType.getInt("style");

							newItem.arrTypes.add(newType);
						}

						arrBrand.add(newItem);
					}

					JSONArray jsonArrayColor = object.getJSONArray("colors");
					for (int i = 0; i < jsonArrayColor.length(); i++)
					{
						STColor newItem = new STColor();
						JSONObject objItem = jsonArrayColor.getJSONObject(i);
						newItem.uid = objItem.getLong("id");
						newItem.name = objItem.getString("name");
						newItem.code = objItem.getString("code");

						arrColor.add(newItem);
					}

					ShowBrand(0);
					ShowType(0);
					ShowColor(0);
				}
				else if (nRetcode == ConstData.ERR_CODE_DEVNOTMATCH)
				{
					logout(szRetmsg);
				}
				else
				{
					Global.showAdvancedToast(VerifyDriverActivity.this, szRetmsg, Gravity.CENTER);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			super.onFailure(error, content);	//To change body of overridden methods use File | Settings | File Templates.
			stopProgress();
			Global.showAdvancedToast(VerifyDriverActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};

	public class ItemAdapter extends BaseAdapter
	{
		@Override
		public int getCount() {
			if (nCurAdapter == ADAPTER_BRAND)
			{
				if (arrBrand != null)
					return arrBrand.size();
				else
					return 0;
			}
			else if (nCurAdapter == ADAPTER_TYPE)
			{
				if (arrBrand == null || arrBrand.size() <= nBrandID)
					return 0;

				if (arrBrand.get(nBrandID).arrTypes != null)
					return arrBrand.get(nBrandID).arrTypes.size();
				else
					return 0;
			}
			else if (nCurAdapter == ADAPTER_COLOR)
			{
				if (arrColor != null)
					return arrColor.size();
				else
					return 0;
			}

			return 0;
		}

		@Override
		public Object getItem(int position) {
			if (nCurAdapter == ADAPTER_BRAND)
				return arrBrand.get(position);
			else if (nCurAdapter == ADAPTER_TYPE)
				return arrBrand.get(nBrandID).arrTypes.get(position);
			else if (nCurAdapter == ADAPTER_COLOR)
				return arrColor.get(position);

			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public boolean isEmpty() {
			return getCount() == 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			STBrand brandInfo = null;
			STBrand typeInfo = null;
			STColor colorInfo = null;

			if (nCurAdapter == ADAPTER_BRAND)
				brandInfo = arrBrand.get(position);
			else if (nCurAdapter == ADAPTER_TYPE)
				typeInfo = arrBrand.get(nBrandID);
			else if (nCurAdapter == ADAPTER_COLOR)
				colorInfo = arrColor.get(position);
			else
				return null;

			if (convertView == null)
			{
				convertView = new RelativeLayout(parent.getContext());
				AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, 60);
				convertView.setLayoutParams(layoutParams);
				convertView.setBackgroundColor(Color.GRAY);

				TextView txtItem = new TextView(convertView.getContext());
				txtItem.setTextSize(TypedValue.COMPLEX_UNIT_PX, 21);
				txtItem.setTextColor(getResources().getColor(R.color.GRAY_COLOR));
				txtItem.setBackgroundResource(R.drawable.rectgraywhite_frame);
				txtItem.setPadding(0, 0, 0, 0);
				txtItem.setGravity(Gravity.CENTER);
				AbsListView.LayoutParams txtLayoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.FILL_PARENT);
				txtItem.setLayoutParams(txtLayoutParams);
				if (brandInfo != null)
					txtItem.setText(brandInfo.name);
				else if (typeInfo != null)
					txtItem.setText(typeInfo.arrTypes.get(position).name);
				else if (colorInfo != null)
					txtItem.setText(colorInfo.name);

				if (brandInfo != null)
				{
					txtItem.setTag(position);
					txtItem.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Integer nID = (Integer)v.getTag();
							if (nID != null)
							{
								ShowBrand(nID.intValue());
							}
						}
					});
				}
				else if (typeInfo != null)
				{
					txtItem.setTag(position);
					txtItem.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Integer nID = (Integer)v.getTag();
							if (nID != null)
							{
								ShowType(nID.intValue());
							}
						}
					});
				}
				else if (colorInfo != null)
				{
					txtItem.setTag(position);
					txtItem.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Integer nID = (Integer)v.getTag();
							if (nID != null)
							{
								ShowColor(nID.intValue());
							}
						}
					});
				}

				((RelativeLayout)convertView).addView(txtItem);
				Point ptScreen = Global.getScreenSize(VerifyDriverActivity.this);
				ResolutionSet.instance.iterateChild(convertView, ptScreen.x, ptScreen.y);

				STViewHolder viewHolder = new STViewHolder();
				viewHolder.txtItem = txtItem;

				convertView.setTag(viewHolder);
			}
			else
			{
				STViewHolder viewHolder = (STViewHolder)convertView.getTag();
				if (brandInfo != null)
				{
					viewHolder.txtItem.setText(brandInfo.name);
					viewHolder.txtItem.setTag(position);
					viewHolder.txtItem.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Integer nUid = (Integer)v.getTag();
							if (nUid != null)
							{
								ShowBrand(nUid.intValue());
							}
						}
					});
				}
				else if (typeInfo != null)
				{
					viewHolder.txtItem.setText(typeInfo.arrTypes.get(position).name);
					viewHolder.txtItem.setTag(position);
					viewHolder.txtItem.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Integer nUid = (Integer)v.getTag();
							if (nUid != null)
								ShowType(nUid.intValue());
						}
					});
				}
				else if (colorInfo != null)
				{
					viewHolder.txtItem.setText(colorInfo.name);
					viewHolder.txtItem.setTag(position);
					viewHolder.txtItem.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Integer nUid = (Integer)v.getTag();
							if (nUid != null)
								ShowColor(nUid.intValue());
						}
					});
				}
			}

			return convertView;
		}
	}

	public class STViewHolder
	{
		public TextView txtItem = null;
	}

	public void ShowColor(int nNo)
	{
		lblColor.setText(arrColor.get(nNo).name);
		listLayout.setVisibility(View.INVISIBLE);
		strColor = arrColor.get(nNo).name;

		return;
	}

	public void ShowBrand(int nNo)
	{
		lblBrand.setText(arrBrand.get(nNo).name);
		listLayout.setVisibility(View.INVISIBLE);
		strBrand = arrBrand.get(nNo).name;

		nBrandID = nNo;
		nCurAdapter = ADAPTER_TYPE;
		adapter.notifyDataSetChanged();
		ShowType(0);

		return;
	}

	public void ShowType(int nNo)
	{
		lblCarType.setText(arrBrand.get(nBrandID).arrTypes.get(nNo).name);
		strType = arrBrand.get(nBrandID).arrTypes.get(nNo).name;
		listLayout.setVisibility(View.INVISIBLE);
	}
}
