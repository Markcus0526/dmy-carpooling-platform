package com.damytech.PincheApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import com.damytech.DataClasses.STAddr;
import com.damytech.DataClasses.STPlaceSearchItem;
import com.damytech.DataClasses.STSuggestionItem;
import com.damytech.HttpConn.AsyncHttpClient;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.Misc.DBDao;
import com.damytech.Misc.Global;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshBase;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshListView;
import com.damytech.Utils.ResolutionSet;
import com.damytech.Utils.mutil.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TextAddrSearchActivity extends SuperActivity
{
	private final String baidu_suggestion_url = "http://api.map.baidu.com/place/v2/suggestion?";
	private final String baidu_placesearch_url = "http://api.map.baidu.com/place/search?";

	public static final int TEXT_FIND = 0;
	public static final int SPEECH_FIND = 1;
	private int nFindMode = 0;

	private ImageButton btnBack = null;
	private PullToRefreshListView listView = null;
	private ImageView imgSearch = null;
	private ImageView imgClose = null;
	private EditText txtSearch = null;

	private String szCity = "";

	private ArrayList<STSuggestionItem> arrSuggestions = new ArrayList<STSuggestionItem>();
	private int sug_index = 0;

	private ArrayList<STAddrItem> arrData = new ArrayList<STAddrItem>();
	private ArrayList<STAddrItem> arrViewData = new ArrayList<STAddrItem>();
	private final int PAGEITEM_COUNT = 10;
	private AddrItemAdapter adapter = null;

	private String szKeyword = "";

	public static String IN_EXTRA_REGION				= "City";
	public static String IN_EXTRA_SEARCHMODE			= "Mode";
	public static String IN_EXTRA_CURPOS				= "Pos";

	public static String OUT_EXTRA_PLACENAME			= "Name";
	public static String OUT_EXTRA_LATITUDE			 = "lat";
	public static String OUT_EXTRA_LONGITUDE			= "lng";
    private Button btSumbit;
    private Button btClearAddr;
    private int startend = 0;
    private DBDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_textaddrsearch);

		szCity = getIntent().getStringExtra(IN_EXTRA_REGION);
		nFindMode = getIntent().getIntExtra(IN_EXTRA_SEARCHMODE, TEXT_FIND);
		szKeyword = getIntent().getStringExtra(IN_EXTRA_CURPOS);
        startend = getIntent().getIntExtra("startend",0);
        dao = DBDao.getDaoInstance(this);

		initControls();
		initResolution();

		if (nFindMode == SPEECH_FIND && !szKeyword.equals(""))
		{
			txtSearch.setText(szKeyword);
			callSuggestionService(szKeyword, szCity);
		}else{
            if(1 == startend){
                ArrayList<STAddr> arr = dao.getAll("1");
                arrViewData.clear();
                if(arr.size() > 0)
                    btClearAddr.setVisibility(View.VISIBLE);
                for(int i = 0; i < arr.size(); i++){
                    STAddrItem item = new STAddrItem();
                    item.build = arr.get(i).getAddr();
                    item.lat = Double.parseDouble(arr.get(i).getLat());
                    item.lng = Double.parseDouble(arr.get(i).getLng());
                    item.city = szCity;
                    arrViewData.add(item);
                }
                adapter.notifyDataSetChanged();
            }

        }
	}

	private void initControls()
	{
		btnBack = (ImageButton)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickBack();
			}
		});
        btSumbit = (Button)findViewById(R.id.bt_address_sumbit);
        btClearAddr = (Button)findViewById(R.id.bt_clearaddr);
		txtSearch = (EditText) findViewById(R.id.txtFindVal);
		txtSearch.addTextChangedListener(txtWatcher);
		txtSearch.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    onClickSearch();
					return true;
				}

				return false;
			}
		});

		imgSearch = (ImageView) findViewById(R.id.imgSearch);
		imgSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

                onClickSearch();
			}
		});

		imgClose = (ImageView) findViewById(R.id.imgClose);
		imgClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickClose();
			}
		});

		adapter = new AddrItemAdapter(TextAddrSearchActivity.this, arrViewData);
		listView = (PullToRefreshListView)findViewById(R.id.listview);
		{
			listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
			listView.setOnRefreshListener(list_refresh_listener);
			listView.setAdapter(adapter);
			listView.getRefreshableView().setDivider(new ColorDrawable(Color.parseColor("#FFF1F1F1")));
			listView.getRefreshableView().setCacheColorHint(Color.parseColor("#FFF1F1F1"));
		}

        btClearAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClearDialog();
            }
        });

        btSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitAddress();
            }
        });

	}

    private void showClearDialog(){
        CommonAlertDialog dialog = new CommonAlertDialog.Builder(this)
                .message("您确定要清除历史目的地吗？")
                .type(CommonAlertDialog.DIALOGTYPE_CONFIRM)
                .positiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btClearAddr.setVisibility(View.GONE);
                        dao.deleteAll();
                        arrViewData.clear();
                        adapter.notifyDataSetChanged();
                    }
                })
                .build();
        dialog.show();
    }

    private void submitAddress(){
        String address = txtSearch.getText().toString().trim();
        if(address != null && !"".equals(address)){
            Intent intent = new Intent();
            intent.putExtra(OUT_EXTRA_PLACENAME, szCity+address);
            intent.putExtra(OUT_EXTRA_LONGITUDE, 0.0f);
            intent.putExtra(OUT_EXTRA_LATITUDE, 0.0f);

            TextAddrSearchActivity.this.setResult(RESULT_OK, intent);
            TextAddrSearchActivity.this.finish();
        }else{
            Global.showAdvancedToast(this,"请输入地址",Gravity.CENTER);
        }

    }
	private void callPlaceSearchService(String keyword, String city, JsonHttpResponseHandler handler)
	{
		try
		{
			String szUrl = baidu_placesearch_url
					+ "q=" + keyword + "&"
					+ "region=" + city + "&"
					+ "output=json&ak=";
            Utils.mLogError("callPlaceSearchService url:"+szUrl);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(4000);
			client.get(szUrl, handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	private void callSuggestionService(String keyword, String city)
	{
		//startProgress();

		try
		{
			String szUrl = baidu_suggestion_url
					+ "ak=" + Global.loadBaiduApiKey(getApplicationContext()) + "&"
					+ "output=json&page_size=10&page_num=0&scope=1&"
					+ "query=" + keyword + "&"
					+ "region=" + city;
            Utils.mLogError("callSuggestionService url:"+szUrl);
			AsyncHttpClient client = new AsyncHttpClient();
			client.setTimeout(4000);
			client.get(szUrl, suggestion_handler);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void onClickSearch()
	{
		szKeyword = txtSearch.getText().toString();
		if (szKeyword.length() == 0)
		{
			Global.showAdvancedToast(TextAddrSearchActivity.this, getString(R.string.STR_INSERT_FINDSTRING), Gravity.CENTER);
			return;
		}
		else
		{
            startProgress();
            btClearAddr.setVisibility(View.GONE);
			arrData.clear();
			arrViewData.clear();
			adapter.notifyDataSetChanged();

			callSuggestionService(szKeyword, szCity);
		}
	}

	private void onClickClose()
	{
		szKeyword = "";
		txtSearch.setText(szKeyword);
		imgClose.setVisibility(View.INVISIBLE);
        if(1 == startend){
            ArrayList<STAddr> arr = dao.getAll("1");
            arrViewData.clear();
            if(arr.size() > 0)
                btClearAddr.setVisibility(View.VISIBLE);
            for(int i = 0; i < arr.size(); i++){
                STAddrItem item = new STAddrItem();
                item.build = arr.get(i).getAddr();
                item.lat = Double.parseDouble(arr.get(i).getLat());
                item.lng = Double.parseDouble(arr.get(i).getLng());
                item.city = szCity;
                arrViewData.add(item);
            }
            adapter.notifyDataSetChanged();
        }
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


	public class STAddrItem
	{
		public String build = "";
		public String city = "";
		public String street = "";
		public double lng = 0.0f;
		public double lat = 0.0f;
	}


	private class STItemViewHolder
	{
		TextView lblBuild = null;
		TextView lblStreet = null;
		ImageButton btnItem = null;
	}

	public class AddrItemAdapter extends ArrayAdapter<STAddrItem>
	{
		public AddrItemAdapter(Context ctx, ArrayList<STAddrItem> list) {
			super(ctx, 0, list);
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			STAddrItem addrItem = arrViewData.get(position);
			View v = convertView;
			STItemViewHolder holder = null;
			if (v == null)
			{
				v = getLayoutInflater().inflate(R.layout.view_driver_textaddrsearchitem, null);

				ListView.LayoutParams layoutParams = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
				v.setLayoutParams(layoutParams);

				Point point = Global.getScreenSize(TextAddrSearchActivity.this);
				ResolutionSet.instance.iterateChild(v, point.x, point.y);

				holder = new STItemViewHolder();
				v.setTag(holder);
			}
			else
			{
				holder = (STItemViewHolder)v.getTag();
			}

			TextView lblBuild = null;
			if (holder.lblBuild == null)
				holder.lblBuild = (TextView) v.findViewById(R.id.lblBuild);
			lblBuild = holder.lblBuild;
			lblBuild.setText(addrItem.build);

			TextView lblStreet = null;
			if (holder.lblStreet == null)
				holder.lblStreet = (TextView) v.findViewById(R.id.lblStreet);
			lblStreet = holder.lblStreet;
			lblStreet.setText(addrItem.city + " " + addrItem.street);

			ImageButton btnItem = null;
			if (holder.btnItem == null)
				holder.btnItem = (ImageButton)v.findViewById(R.id.btn_item);
			btnItem = holder.btnItem;
			btnItem.setTag(addrItem);
			btnItem.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					STAddrItem selItem = (STAddrItem)view.getTag();

					if (selItem.lat == 0 && selItem.lng == 0) {
						startProgress();
						callPlaceSearchService(selItem.build, selItem.city, select_item_handler);
					} else {
						Intent intent = new Intent();
						intent.putExtra(OUT_EXTRA_PLACENAME, selItem.build);
						intent.putExtra(OUT_EXTRA_LONGITUDE, selItem.lng);
						intent.putExtra(OUT_EXTRA_LATITUDE, selItem.lat);
						TextAddrSearchActivity.this.setResult(RESULT_OK, intent);
						TextAddrSearchActivity.this.finish();
					}
				}
			});

			return v;
		}
	}


	private JsonHttpResponseHandler suggestion_handler = new JsonHttpResponseHandler()
	{
		@Override
		public void onSuccess(JSONObject jsonData)
		{
			try {
                Utils.mLogError("suggestion_handler:"+jsonData);
				int nStatus = jsonData.getInt("status");
				if (nStatus == 0)
				{
					arrData.clear();
					arrSuggestions.clear();
					JSONArray array = jsonData.getJSONArray("result");
					for (int i = 0; i < array.length(); i++)
					{
						JSONObject jsonItem = array.getJSONObject(i);
						STSuggestionItem sug_item = STSuggestionItem.decodeFromJSON(jsonItem);
						arrSuggestions.add(sug_item);
					}

					boolean needCallPlaceApi = false;
					for (int i = 0; i < arrSuggestions.size(); i++)
					{
						STSuggestionItem sug_item = arrSuggestions.get(i);
						if (!sug_item.district.equals(""))
						{
							STAddrItem item = new STAddrItem();
							item.build = sug_item.name;
							item.city = sug_item.city;
							item.street = sug_item.district;
							item.lat = 0;
							item.lng = 0;
							arrData.add(item);
						}
						else
						{
							needCallPlaceApi = true;
							sug_index = i;
							callPlaceSearchService(sug_item.name, szCity, place_list_handler);
							break;
						}
					}

					if (!needCallPlaceApi)
					{
						stopProgress();

						arrViewData.clear();
                        adapter.notifyDataSetChanged();
						for (int i = 0; i < PAGEITEM_COUNT; i++) {
							if (arrData.size() <= i)
								break;
							arrViewData.add(arrData.get(i));
						}

						adapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
					}
				}
                else
                {
//                    stopProgress();
                    newSearch();
                }
			} catch (JSONException e) {
				e.printStackTrace();
                stopProgress();
			}
		}

		@Override
		public void onFailure(Throwable ex, String exception)
		{
			super.onFailure(ex, exception);
			stopProgress();
			Global.showAdvancedToast(TextAddrSearchActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private TextWatcher txtWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}

		@Override
		public void afterTextChanged(Editable s) {
			szKeyword = s.toString();
			if (szKeyword.length() == 0)
				imgClose.setVisibility(View.INVISIBLE);
			else {
                btClearAddr.setVisibility(View.GONE);
                imgClose.setVisibility(View.VISIBLE);
                arrData.clear();
                arrViewData.clear();
                adapter.notifyDataSetChanged();
                callSuggestionService(szKeyword, szCity);
            }
		}
	};
    private void newSearch(){
        String addr = txtSearch.getText().toString().trim();
        callPlaceSearchService(addr, szCity, secondSearchHandler);
    }
    private JsonHttpResponseHandler secondSearchHandler = new JsonHttpResponseHandler(){
        @Override
        public void onSuccess(JSONObject response) {
            super.onSuccess(response);
            stopProgress();
            Utils.mLogError("secondSearchHandler:"+response);
            try{
                arrData.clear();
                arrSuggestions.clear();
                arrViewData.clear();
                adapter.notifyDataSetChanged();
                JSONArray array = response.getJSONArray("results");
                for (int i = 0; i < array.length(); i++)
                {
                    JSONObject jsonItem = array.getJSONObject(i);
                    STAddrItem item = new STAddrItem();
                    item.build = jsonItem.getString("name");
                    item.city = szCity;
                    item.street = jsonItem.getString("address");
                    JSONObject loc = jsonItem.getJSONObject("location");
                    item.lat = loc.getDouble("lat");
                    item.lng = loc.getDouble("lng");
                    arrViewData.add(item);
                }
                 adapter.notifyDataSetChanged();
                 listView.onRefreshComplete();


            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

	private JsonHttpResponseHandler select_item_handler = new JsonHttpResponseHandler()
	{
		@Override
		public void onSuccess(JSONObject response) {
			super.onSuccess(response);
			stopProgress();
            Utils.mLogError("select_item_handler:"+response);
			try {
				String status = response.getString("status");

				if (status.equals("OK")) {
					JSONArray arrItems = response.getJSONArray("results");

					if (arrItems.length() == 0) {
						Global.showAdvancedToast(TextAddrSearchActivity.this, "没有地点信息", Gravity.CENTER);
					} else {
						STPlaceSearchItem first_item = STPlaceSearchItem.decodeFromJSON(arrItems.getJSONObject(0));

						Intent intent = new Intent();
						intent.putExtra(OUT_EXTRA_PLACENAME, first_item.name);
						intent.putExtra(OUT_EXTRA_LONGITUDE, first_item.lng);
						intent.putExtra(OUT_EXTRA_LATITUDE, first_item.lat);

						TextAddrSearchActivity.this.setResult(RESULT_OK, intent);
						TextAddrSearchActivity.this.finish();
					}
				} else {
					// Abnormal situation
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			super.onFailure(e, errorResponse);
			stopProgress();
			Global.showAdvancedToast(TextAddrSearchActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private JsonHttpResponseHandler place_list_handler = new JsonHttpResponseHandler()
	{
		@Override
		public void onSuccess(JSONObject response) {
			super.onSuccess(response);
            Utils.mLogError("place_list_handler:"+response);
			try {
				String status = response.getString("status");

				if (status.equals("OK")) {
					JSONArray arrItems = response.getJSONArray("results");

					for (int i = 0; i < arrItems.length(); i++)
					{
						STPlaceSearchItem place_item = STPlaceSearchItem.decodeFromJSON(arrItems.getJSONObject(i));

						if (place_item.address.equals(""))
							continue;

						STAddrItem item = new STAddrItem();
						item.build = place_item.name;
						item.city = place_item.address;
						item.street = place_item.address;
						item.lat = place_item.lat;
						item.lng = place_item.lng;

						arrData.add(item);
					}

					boolean needCallPlaceApi = false;
					for (int i = sug_index + 1; i < arrSuggestions.size(); i++)
					{
						STSuggestionItem sug_item = arrSuggestions.get(i);
						if (!sug_item.district.equals(""))
						{
							STAddrItem item = new STAddrItem();
							item.build = sug_item.name;
							item.city = sug_item.city;
							item.street = sug_item.district;
							item.lat = 0;
							item.lng = 0;
							arrData.add(item);
						}
						else
						{
							needCallPlaceApi = true;
							sug_index = i;
							callPlaceSearchService(sug_item.name, szCity, place_list_handler);
							break;
						}
					}

					if (!needCallPlaceApi)
					{
						stopProgress();

						arrViewData.clear();
						for (int i = 0; i < PAGEITEM_COUNT; i++) {
							if (arrData.size() <= i)
								break;
							arrViewData.add(arrData.get(i));
						}

						adapter.notifyDataSetChanged();
					}
				} else {
					// Abnormal situation
					stopProgress();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				stopProgress();
			}
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse) {
			super.onFailure(e, errorResponse);
			stopProgress();
			Global.showAdvancedToast(TextAddrSearchActivity.this, getResources().getString(R.string.STR_CONN_ERROR), Gravity.CENTER);
		}
	};


	private PullToRefreshBase.OnRefreshListener list_refresh_listener = new PullToRefreshBase.OnRefreshListener() {
		@Override
		public void onRefresh(PullToRefreshBase refreshView) {
			if (arrViewData.size() >= arrData.size()) {
				TimerTask timerTask = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								listView.onRefreshComplete();
							}
						});
					}
				};

				Timer timer = new Timer();
				timer.schedule(timerTask, 500);
			} else {
				TimerTask timerTask = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								int nStartIndex = arrViewData.size();
								for (int i = 0; i < PAGEITEM_COUNT; i++)
								{
									if (arrData.size() <= i + nStartIndex)
										break;

									arrViewData.add(arrData.get(i + nStartIndex));
								}

								adapter.notifyDataSetChanged();

								listView.onRefreshComplete();
							}
						});
					}
				};

				Timer timer = new Timer();
				timer.schedule(timerTask, 500);
			}
		}
	};


}
