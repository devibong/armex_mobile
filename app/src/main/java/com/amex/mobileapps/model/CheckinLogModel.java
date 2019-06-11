package com.amex.mobileapps.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qsystem.android.GlobalApplication;

import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class CheckinLogModel {
    private static  final String TABLE_NAME = "tbl_checkinlog";
    public SQLiteDatabase db;

    public CheckinLogModel(SQLiteDatabase db){
        this.db = db;
    }

    public static String getSqlCreate() {
        return "CREATE TABLE " + TABLE_NAME +
            "(fin_id INTEGER PRIMARY KEY," +
            "fst_cust_code TEXT," +
            "fdt_checkin_datetime TEXT," +
            "fst_checkin_location TEXT," +
            "fst_photo_path TEXT);";
    }


    public static void cleanTable(SQLiteDatabase db){
        String ssql = "DELETE FROM " + TABLE_NAME;
        db.execSQL(ssql);
    }

    public static void insert(SQLiteDatabase db, ContentValues contentValues){

        db.insert(TABLE_NAME,null,contentValues);

    }

    public static  ContentValues getById(SQLiteDatabase db, int fin_id){

        String ssql = "select * from " + TABLE_NAME + " where fin_id = ?";

        Cursor rs = db.rawQuery(ssql,new String[]{String.valueOf(fin_id)});
        ContentValues checkinLog = null;
        if(rs.moveToFirst()){
            checkinLog= new ContentValues();
            checkinLog.put("fin_id",rs.getInt(rs.getColumnIndex("fin_id")));
            checkinLog.put("fst_cust_code",rs.getString(rs.getColumnIndex("fst_cust_code")));
            checkinLog.put("fdt_checkin_datetime",rs.getString(rs.getColumnIndex("fdt_checkin_datetime")));
            checkinLog.put("fst_checkin_location",rs.getString(rs.getColumnIndex("fst_checkin_location")));
            checkinLog.put("fst_photo_path",rs.getString(rs.getColumnIndex("fst_photo_path")));
        }
        return checkinLog;
    }

    public static  void deleteById(SQLiteDatabase db, int fin_id){
        String ssql = "delete from " + TABLE_NAME + " where fin_id = ?";
        //Cursor rs = db.rawQuery(ssql,new String[]{String.valueOf(fin_id)});
        db.execSQL(ssql,new Object[]{fin_id});

    }



    public static List<ContentValues> getRecords(SQLiteDatabase db){
        Cursor rs = db.rawQuery("select * from " + TABLE_NAME + " order by fin_id",null);
        List<ContentValues> listCheckin = new ArrayList<ContentValues>();
        if(rs.moveToFirst()){
            do{
                ContentValues checkinLog = new ContentValues();
                checkinLog.put("fin_id",rs.getInt(rs.getColumnIndex("fin_id")));
                checkinLog.put("fst_cust_code",rs.getString(rs.getColumnIndex("fst_cust_code")));
                checkinLog.put("fdt_checkin_datetime",rs.getString(rs.getColumnIndex("fdt_checkin_datetime")));
                checkinLog.put("fst_checkin_location",rs.getString(rs.getColumnIndex("fst_checkin_location")));
                checkinLog.put("fst_photo_path",rs.getString(rs.getColumnIndex("fst_photo_path")));

                listCheckin.add(checkinLog);
            }while (rs.moveToNext());
        }
        return listCheckin;

    }

    public static int getTotalPending(){
        SQLiteDatabase db = new AppDB(GlobalApplication.getAppContext()).getReadableDatabase();
        Cursor rs = db.rawQuery("select count(*) as ttl_row from " + TABLE_NAME,null );
        rs.moveToFirst();

        if (rs.getInt(rs.getColumnIndex("ttl_row")) == NULL){
            return 0;
        }else {
            return rs.getInt(rs.getColumnIndex("ttl_row"));
        }
    }


}
