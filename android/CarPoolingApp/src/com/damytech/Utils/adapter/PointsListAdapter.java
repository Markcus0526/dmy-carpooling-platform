package com.damytech.Utils.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.damytech.DataClasses.MiddlePoint;
import com.damytech.Misc.Global;
import com.damytech.PincheApp.R;
import com.damytech.PincheApp.TextAddrSearchActivity;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2014/9/3.
 */
public class PointsListAdapter extends BaseAdapter{
    private static final String NUL_STR = "";
    private static final String TAG = "erik_bug_adapter";
    private static final  int MAX_LENGTH = 4;
    private RecognizerDialog dlgRec = null;
    private LayoutInflater mInflater;
    private Context mContext;
    private ListView mListView;
    private HashMap<Integer, MiddlePoint> pointsRecord = new HashMap<Integer, MiddlePoint>() ;
    private ArrayList<String> points  = new ArrayList<String>();
    private ViewHolder holder = null;
    private static final String TEST_CITY_STR = "北京";

    //others
    private static final int LIST_MAX = 4;

    /*public int getResultsNum(){
        int counter = 0;
        for(int i=0; i<LIST_MAX; i++){
            if(!((points.get(i)).equals("中途点"+(i+1)+"详细地址"))){
                counter++;
            }
        }
        return counter;
    }*/

    public void setList(ArrayList<String> list){
        int i = 0;
        for (String point : list){
            points.set(i,point);
            i++;
        }
    }

    public ArrayList<String> getList(){
        return points;
    }

    public void setData(int position, String location){
        Log.d(TAG,"pos---->"+position);
        points.set(position, location);
    }

    public PointsListAdapter(Context context, ListView listview, HashMap<Integer, MiddlePoint> pointsRecord)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        dlgRec = new RecognizerDialog(context, "appid=50e1b967");
        for(int i=0; i<LIST_MAX; i++){
            //Log.d(TAG, "constructor inner------>");
            points.add("中途点"+(i+1)+"详细地址");
        }
        this.pointsRecord = pointsRecord;
        /*for (String point : points){
            Log.d(TAG, "point----->"+point);
        }*/
    }

    @Override
    public int getCount() {
        if(points.isEmpty()){
            return MAX_LENGTH;
        }else {
            return points.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder{
        public TextView pointTxt;
        public ImageButton voiceInput;
        public ImageButton quitButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.set_points_item, null);
            holder.pointTxt = (TextView)convertView.findViewById(R.id.point_txt);
            holder.voiceInput = (ImageButton)convertView.findViewById(R.id.voice_input);
            holder.quitButton = (ImageButton)convertView.findViewById(R.id.quit_button);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();

            String pointAddress = points.get(position);
            if (pointAddress.length() > 10) {
                holder.pointTxt.setText(pointAddress.substring(0, 10) + "...");
            } else {
                holder.pointTxt.setText(pointAddress);
            }

            holder.pointTxt.setTag(position);
            holder.pointTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "v----->"+ v.getTag());
                    GotoSearchPage(Global.loadCityName(mContext),NUL_STR,Integer.parseInt(v.getTag().toString()));
                }
            });
            holder.voiceInput.setTag(position);
            holder.voiceInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initRec(Global.loadCityName(mContext), Integer.parseInt(v.getTag().toString()));
                }
            });
            holder.quitButton.setTag(position);
            holder.quitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(v);
                }
            });
        }

        return convertView;
    }

    private void deleteItem(View v) {
        //delete UI
        int position = Integer.parseInt(v.getTag().toString());
        points.set(position,"中途点"+(position+1)+"详细地址");
        this.notifyDataSetChanged();
        Log.d(TAG, "position------>"+position);
        pointsRecord.remove(position);
        //delete data

    }

    private void initRec(final String city, final int startEnd) {
        dlgRec.setEngine("sms", null, null);
        dlgRec.setSampleRate(SpeechConfig.RATE.rate16k);
        dlgRec.setListener(new RecognizerDialogListener() {
            private StringBuilder mStr = new StringBuilder();
            @Override
            public void onResults(ArrayList<RecognizerResult> result, boolean b) {
                for (RecognizerResult recognizerResult : result)
                {
                    mStr.append(recognizerResult.text);
                }
            }

            @Override
            public void onEnd(SpeechError speechError) {
                if (speechError != null)        return;

                String strAddr = Global.eatChinesePunctuations(mStr.toString());
                GotoSearchPage(city,strAddr,startEnd);
            }
        });
        dlgRec.show();
    }

    private void GotoSearchPage(String city, String addr, int startEnd) {
        Intent intentStart = new Intent(mContext, TextAddrSearchActivity.class);
        intentStart.putExtra(TextAddrSearchActivity.IN_EXTRA_REGION, city);
        intentStart.putExtra(TextAddrSearchActivity.IN_EXTRA_SEARCHMODE, TextAddrSearchActivity.SPEECH_FIND);
        intentStart.putExtra(TextAddrSearchActivity.IN_EXTRA_CURPOS, addr);

		intentStart.putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_RIGHT());
		((Activity)mContext).getIntent().putExtra(Global.ANIM_DIRECTION(), Global.ANIM_COVER_FROM_LEFT());
		((Activity)mContext).startActivityForResult(intentStart, startEnd);
    }


}
