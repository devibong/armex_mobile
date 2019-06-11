package com.amex.mobileapps.model;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qsystem.android.GlobalApplication;

import java.util.ArrayList;
import java.util.List;

public class SettingsModel {
    private static  final String TABLE_NAME = "settings";
    public SQLiteDatabase db;

    public SettingsModel(SQLiteDatabase db){
        this.db = db;
    }

    public static String getSqlCreate() {
        return "CREATE TABLE " + TABLE_NAME +
            "(fin_id INTEGER PRIMARY KEY," +
            "fst_key TEXT," +
            "fst_value TEXT);";
    }

    public static void cleanTable(SQLiteDatabase db){
        String ssql = "DELETE FROM " + TABLE_NAME;
        db.execSQL(ssql);
    }



    public static  String getValue(String key){
        SQLiteDatabase db = new AppDB(GlobalApplication.getAppContext()).getReadableDatabase();
        String ssql = "select * from " + TABLE_NAME + " where fst_key = ?";
        Cursor rs = db.rawQuery(ssql,new String[]{key});
        if(rs.moveToFirst()){
            String value = rs.getString(rs.getColumnIndex("fst_value"));
            db.close();
            return value;
        }
        db.close();
        return null;
    }

    public static  void setValue(String key,String value){
        SQLiteDatabase db = new AppDB(GlobalApplication.getAppContext()).getWritableDatabase();
        setValue(db,key,value);
        db.close();

    }

    public static  void setValue(SQLiteDatabase db,String key,String value){
        String ssql = "select * from " + TABLE_NAME + " where fst_key = ?";
        Cursor rs = db.rawQuery(ssql,new String[]{key});
        ssql = "";
        String[] bindArgs;

        if(rs.moveToFirst()){
            ssql = "UPDATE " + TABLE_NAME + " SET fst_value = ? where fst_key = ?";
            bindArgs = new String[]{value,key};

        }else{
            ssql = "INSERT INTO " + TABLE_NAME + "(fst_key,fst_value) values (?,?)";
            bindArgs = new String[]{key,value};
        }
        db.execSQL(ssql,bindArgs);


    }



}
