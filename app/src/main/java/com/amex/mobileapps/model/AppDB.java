package com.amex.mobileapps.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.amex.mobileapps.model.CustomersModel;


public class AppDB extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyApp.db";

    public AppDB(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Database","Create Database");
        db.execSQL(CustomersModel.getSqlCreate());
        db.execSQL(ItemsModel.getSqlCreate());
        db.execSQL(ItemFreeModel.getSqlCreate());
        db.execSQL(CheckinLogModel.getSqlCreate());
        db.execSQL(TargetModel.getSqlCreate());
        db.execSQL(SettingsModel.getSqlCreate());
        SettingsModel.setValue(db,"LastSyncDateTime","2019-01-01 12:00:00");


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SettingsModel.getSqlCreate());
    }

}
