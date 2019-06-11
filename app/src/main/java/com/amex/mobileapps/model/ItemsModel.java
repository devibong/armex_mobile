package com.amex.mobileapps.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qsystem.android.GlobalApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ItemsModel {
    private static  final String TABLE_NAME = "tbl_items";
    public SQLiteDatabase db;

    public ItemsModel(SQLiteDatabase db){
        this.db = db;
    }

    public static String getSqlCreate() {
        return "CREATE TABLE " + TABLE_NAME +
            "(fst_item_code TEXT PRIMARY KEY," +
            "fst_item_name TEXT," +
            "fst_satuan_1 TEXT," +
            "fst_satuan_2 TEXT," +
            "fst_satuan_3 TEXT," +
            "fin_conversion_2 REAL," +
            "fin_conversion_3 REAL,"+
            "fin_selling_price1A REAL," +
            "fin_selling_price2A REAL," +
            "fin_selling_price3A REAL," +
            "fin_selling_price1B REAL," +
            "fin_selling_price2B REAL," +
            "fin_selling_price3B REAL," +
            "fin_selling_price1C REAL," +
            "fin_selling_price2C REAL," +
            "fin_selling_price3C REAL," +
            "fin_selling_price1D REAL," +
            "fin_selling_price2D REAL," +
            "fin_selling_price3D REAL," +
            "fin_selling_price1E REAL," +
            "fin_selling_price2E REAL," +
            "fin_selling_price3E REAL," +
            "fin_selling_price1F REAL," +
            "fin_selling_price2F REAL," +
            "fin_selling_price3F REAL," +
            "fin_selling_price1G REAL," +
            "fin_selling_price2G REAL," +
            "fin_selling_price3G REAL," +
            "fin_selling_price1H REAL," +
            "fin_selling_price2H REAL," +
            "fin_selling_price3H REAL," +
            "fin_selling_price1I REAL," +
            "fin_selling_price2I REAL," +
            "fin_selling_price3I REAL," +
            "fin_selling_price1J REAL," +
            "fin_selling_price2J REAL," +
            "fin_selling_price3J REAL," +
            "fst_memo TEXT);";
    }


    public static void cleanTable(SQLiteDatabase db){
        String ssql = "DELETE FROM " + TABLE_NAME;
        db.execSQL(ssql);
    }

    public static void insert(SQLiteDatabase db, ContentValues contentValues){
        db.insert(TABLE_NAME,null,contentValues);
    }


    public static ContentValues getByCodeString(String fst_item_code,int fin_price_group_id){
        SQLiteDatabase db = new AppDB(GlobalApplication.getAppContext()).getReadableDatabase();
        String ssql = "select * from " + TABLE_NAME + " where fst_item_code = ?";
        Cursor rs = db.rawQuery(ssql,new String[]{String.valueOf(fst_item_code)});

        ContentValues item = null;
        if(rs.moveToFirst()){
            item = cursorToItem(rs,fin_price_group_id);
        }
        return item;

    }

    public static List<ContentValues> getItems(int fin_price_group_id){
        SQLiteDatabase db = new AppDB(GlobalApplication.getAppContext()).getReadableDatabase();
        Cursor rs = db.rawQuery("select * from " + TABLE_NAME,null);
        List<ContentValues> listItem = new ArrayList<ContentValues>();
        if(rs.moveToFirst()){
            do{
                ContentValues item = cursorToItem(rs,fin_price_group_id);
                listItem.add(item);
            }while (rs.moveToNext());
        }
        return listItem;

    }


    public static boolean deleteById(SQLiteDatabase db,int fin_cust_id){
        db.execSQL("delete from " + TABLE_NAME + " where fin_cust_id = ?",new String[]{String.valueOf(fin_cust_id)} );
        return true;
    }

    public static boolean updateById(SQLiteDatabase db , ContentValues customer){
        db.update(TABLE_NAME,customer,"fin_cust_id = ? ",new String[]{customer.getAsString("fin_cust_id")});
        return true;
    }

    private static ContentValues cursorToItem(Cursor rs,int fin_price_group_id){
        ContentValues contentValues = new ContentValues();
        //customer.put("fin_cust_id",rs.getInt(rs.getColumnIndex("fin_cust_id")));
        //customer.put("fst_cust_name",rs.getString(rs.getColumnIndex("fst_cust_name")));
        //customer.put("fst_cust_address",rs.getString(rs.getColumnIndex("fst_cust_address")));
        //customer.put("fst_cust_location",rs.getString(rs.getColumnIndex("fst_cust_location")));

        contentValues.put("fst_item_code",rs.getString(rs.getColumnIndex("fst_item_code")));
        contentValues.put("fst_item_name",rs.getString(rs.getColumnIndex("fst_item_name")));
        contentValues.put("fst_satuan_1",rs.getString(rs.getColumnIndex("fst_satuan_1")));
        contentValues.put("fst_satuan_2",rs.getString(rs.getColumnIndex("fst_satuan_2")));
        contentValues.put("fst_satuan_3",rs.getString(rs.getColumnIndex("fst_satuan_3")));
        contentValues.put("fin_conversion_2",rs.getDouble(rs.getColumnIndex("fin_conversion_2")));
        contentValues.put("fin_conversion_3",rs.getDouble(rs.getColumnIndex("fin_conversion_3")));

        String priceGroup = "A";
        switch (fin_price_group_id){
            case 1:
                priceGroup = "A";
                break;
            case 2:
                priceGroup = "B";
                break;
            case 3:
                priceGroup = "C";
                break;
            case 4:
                priceGroup = "D";
                break;
            case 5:
                priceGroup = "E";
                break;
            case 6:
                priceGroup = "F";
                break;
            case 7:
                priceGroup = "G";
                break;
            case 8:
                priceGroup = "H";
                break;
            case 9:
                priceGroup = "I";
                break;
            case 10:
                priceGroup = "J";
                break;
            default:
                priceGroup = "A";
                break;
        }

        contentValues.put("fin_selling_price1",rs.getDouble(rs.getColumnIndex("fin_selling_price1" + priceGroup)));
        contentValues.put("fin_selling_price2",rs.getDouble(rs.getColumnIndex("fin_selling_price2" + priceGroup)));
        contentValues.put("fin_selling_price3",rs.getDouble(rs.getColumnIndex("fin_selling_price3" + priceGroup)));
        contentValues.put("fst_memo",rs.getString(rs.getColumnIndex("fst_memo")));

        return contentValues;
    }


}
