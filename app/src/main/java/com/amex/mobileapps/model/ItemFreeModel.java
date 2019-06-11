package com.amex.mobileapps.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qsystem.android.GlobalApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ItemFreeModel {
    private static  final String TABLE_NAME = "tbl_free_item";
    public SQLiteDatabase db;

    public ItemFreeModel(SQLiteDatabase db){
        this.db = db;
    }

    public static String getSqlCreate() {
        return "CREATE TABLE " + TABLE_NAME +
            "(fin_promo_id INTEGER PRIMARY KEY," +
            "fst_item_code_awal TEXT," +
            "fst_item_code_akhir TEXT," +
            "fin_qty INTEGER," +
            "fst_satuan TEXT," +
            "fst_free_item_code_awal TEXT," +
            "fst_free_item_code_akhir TEXT," +
            "fin_free_qty INTEGER," +
            "fst_free_satuan TEXT," +
            "fdt_date_start TEXT," +
            "fdt_date_end TEXT," +
            "fin_price_group_id INTEGER," +
            "fbl_multiple_free INTEGER," +
            "fin_max_free_qty INTEGER);";
    }


    public static void cleanTable(SQLiteDatabase db){
        String ssql = "DELETE FROM " + TABLE_NAME;
        db.execSQL(ssql);
    }

    public static void insert(SQLiteDatabase db, ContentValues contentValues){
        db.insert(TABLE_NAME,null,contentValues);
    }


    public static ContentValues getById(int fin_promo_id){
        SQLiteDatabase db = new AppDB(GlobalApplication.getAppContext()).getReadableDatabase();
        String ssql = "select * from " + TABLE_NAME + " where fin_promo_id = ?";
        Cursor rs = db.rawQuery(ssql,new String[]{String.valueOf(fin_promo_id)});
        ContentValues promo = null;
        if(rs.moveToFirst()){
            promo = cursorToFreePromo(rs);
        }
        return promo;

    }

    public static List<ContentValues> getPromoFreeItem(int fin_price_group_id){

        SQLiteDatabase db = new AppDB(GlobalApplication.getAppContext()).getReadableDatabase();

        Cursor rs = db.rawQuery("select * from " + TABLE_NAME + " WHERE fin_price_group_id = ? order by fdt_date_start" ,new String[]{String.valueOf(fin_price_group_id)});

        List<ContentValues> listFreeItem = new ArrayList<ContentValues>();
        if(rs.moveToFirst()){
            do{
                ContentValues freeItem = cursorToFreePromo(rs);
                listFreeItem.add(freeItem);
            }while (rs.moveToNext());
        }
        return listFreeItem;

    }


    public static boolean deleteById(SQLiteDatabase db,int fin_cust_id){
        db.execSQL("delete from " + TABLE_NAME + " where fin_cust_id = ?",new String[]{String.valueOf(fin_cust_id)} );
        return true;
    }

    public static boolean updateById(SQLiteDatabase db , ContentValues customer){
        db.update(TABLE_NAME,customer,"fin_cust_id = ? ",new String[]{customer.getAsString("fin_cust_id")});
        return true;
    }

    private static ContentValues cursorToFreePromo(Cursor rs){
        ContentValues promo = new ContentValues();

        promo.put("fin_promo_id",rs.getInt(rs.getColumnIndex("fin_promo_id")));
        promo.put("fst_item_code_awal",rs.getString(rs.getColumnIndex("fst_item_code_awal")));
        promo.put("fst_item_code_akhir",rs.getString(rs.getColumnIndex("fst_item_code_akhir")));
        promo.put("fin_qty",rs.getInt(rs.getColumnIndex("fin_qty")));
        promo.put("fst_satuan",rs.getString(rs.getColumnIndex("fst_satuan")));
        promo.put("fst_free_item_code_awal",rs.getString(rs.getColumnIndex("fst_free_item_code_awal")));
        promo.put("fst_free_item_code_akhir",rs.getString(rs.getColumnIndex("fst_free_item_code_akhir")));
        promo.put("fin_free_qty",rs.getInt(rs.getColumnIndex("fin_free_qty")));
        promo.put("fst_free_satuan",rs.getString(rs.getColumnIndex("fst_free_satuan")));
        promo.put("fdt_date_start",rs.getString(rs.getColumnIndex("fdt_date_start")));
        promo.put("fdt_date_end",rs.getString(rs.getColumnIndex("fdt_date_end")));
        promo.put("fin_price_group_id",rs.getInt(rs.getColumnIndex("fin_price_group_id")));
        promo.put("fbl_multiple_free",rs.getInt(rs.getColumnIndex("fbl_multiple_free")));
        promo.put("fin_max_free_qty",rs.getInt(rs.getColumnIndex("fin_max_free_qty")));
        return promo;
    }


}
