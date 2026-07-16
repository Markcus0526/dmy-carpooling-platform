package com.damytech.Misc;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.damytech.DataClasses.STAddr;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/1/27.
 */
public class DBDao {
    private Context context;
    private DBHelper helper;
    private static DBDao dao;
    private DBDao(Context context){
        this.context = context;
        helper = new DBHelper(context);
    }
    public static DBDao getDaoInstance(Context context){
        if (dao == null)
            dao = new DBDao(context);

        return dao;
    }

    public void add(String addr, String lat, String lng, String time, String type){
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql1 = "select count(*) from addr where type = ?";
        String sql2 = "select count(*) from addr where addr = ?";
        String sql3 = "update addr set time = ? where addr = ?";
        String sql4 = "select addr from addr order by time";
        String sql5 = "delete from addr where addr = ?";
        String sql = "insert into addr(addr,lat,lng,time,type) values(?,?,?,?,?)";

        Cursor cursor = db.rawQuery(sql2, new String[]{addr});
        int count = 0;
        if(cursor.moveToFirst()){
            count = cursor.getInt(0);
        }
        cursor.close();
        if(count > 0){
            db.execSQL(sql3, new Object[]{time,addr});
        }else{
            Cursor cursor1 = db.rawQuery(sql1, new String[]{type});
            int count1 = 0;
            if(cursor1.moveToFirst()){
                count1 = cursor1.getInt(0);
            }
            cursor1.close();
            if(count1 >= 5){
                Cursor cursor2 = db.rawQuery(sql4, new String[]{});
                String oldaddr = "";
                if(cursor2.moveToFirst()){
                    oldaddr = cursor2.getString(0);
                    db.execSQL(sql5, new Object[]{oldaddr});
                }
                cursor2.close();

            }
            db.execSQL(sql,new Object[]{addr, lat, lng, time, type});
        }

        db.close();
    }
    public void deleteAll(){
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "delete from addr";
        db.execSQL(sql);
        db.close();
    }

    public void deleteOne(String addr){
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "delete from addr where addr = ?";
        db.execSQL(sql,new Object[]{addr});
        db.close();
    }

    public ArrayList getAll(String type){
        ArrayList<STAddr> arr = new ArrayList<STAddr>();
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select addr, lat, lng from addr where type = ? order by time DESC";
        Cursor cursor = db.rawQuery(sql, new String[]{type});
        while (cursor.moveToNext()){
            STAddr sta = new STAddr();
            sta.setAddr(cursor.getString(0));
            sta.setLat(cursor.getString(1));
            sta.setLng(cursor.getString(2));
            arr.add(sta);
        }

        db.close();
        return arr;
    }


}
