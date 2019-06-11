package com.amex.mobileapps.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qsystem.android.GlobalApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TargetModel {
    private static  final String TABLE_NAME = "tr_target";
    public SQLiteDatabase db;

    public TargetModel(SQLiteDatabase db){
        this.db = db;
    }

    public static String getSqlCreate() {
        return "CREATE TABLE " + TABLE_NAME +
            "(fin_target_id INTEGER PRIMARY KEY," +
            "fst_cust_code TEXT," +
            "fst_item_code TEXT," +
            "fin_qty_target INTEGER," +
            "fst_satuan TEXT," +
            "fdt_date_start TEXT," +
            "fdt_date_end TEXT," +
            "fst_program_code TEXT," +
            "fin_current_qty INTEGER);";
    }


    public static void cleanTable(SQLiteDatabase db){
        String ssql = "DELETE FROM " + TABLE_NAME;
        db.execSQL(ssql);
    }

    public static void insert(SQLiteDatabase db, ContentValues contentValues){
        db.insert(TABLE_NAME,null,contentValues);
    }


    public static List<ContentValues> getTarget(String fst_cust_code){
        SQLiteDatabase db = new AppDB(GlobalApplication.getAppContext()).getReadableDatabase();
        Cursor rs = db.rawQuery("select * from " + TABLE_NAME + " WHERE  fst_cust_code = ?",new String[]{fst_cust_code});
        List<ContentValues> listTarget = new ArrayList<ContentValues>();
        if(rs.moveToFirst()){
            do{
                ContentValues target = cursorToTarget(rs);
                listTarget.add(target);
            }while (rs.moveToNext());
        }
        return listTarget;

    }

    public static boolean deleteById(SQLiteDatabase db,int fin_cust_id){
        db.execSQL("delete from " + TABLE_NAME + " where fin_cust_id = ?",new String[]{String.valueOf(fin_cust_id)} );
        return true;
    }

    public static boolean updateById(SQLiteDatabase db , ContentValues customer){
        db.update(TABLE_NAME,customer,"fin_cust_id = ? ",new String[]{customer.getAsString("fin_cust_id")});
        return true;
    }

    private static ContentValues cursorToTarget(Cursor rs){
        ContentValues target = new ContentValues();
        target.put("fin_target_id",rs.getInt(rs.getColumnIndex("fin_target_id")));
        target.put("fst_cust_code",rs.getString(rs.getColumnIndex("fst_cust_code")));
        target.put("fst_item_code",rs.getString(rs.getColumnIndex("fst_item_code")));
        target.put("fin_qty_target",rs.getInt(rs.getColumnIndex("fin_qty_target")));
        target.put("fst_satuan",rs.getString(rs.getColumnIndex("fst_satuan")));
        target.put("fdt_date_start",rs.getString(rs.getColumnIndex("fdt_date_start")));
        target.put("fdt_date_end",rs.getString(rs.getColumnIndex("fdt_date_end")));
        target.put("fst_program_code",rs.getString(rs.getColumnIndex("fst_program_code")));
        target.put("fin_current_qty",rs.getInt(rs.getColumnIndex("fin_current_qty")));

        return target;
    }


}
