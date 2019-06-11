package com.amex.mobileapps.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.qsystem.android.GlobalApplication;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomersModel {
    private static  final String TABLE_NAME = "tbl_customers";
    public SQLiteDatabase db;

    public CustomersModel(SQLiteDatabase db){
        this.db = db;
    }

    public static String getSqlCreate() {
        return "CREATE TABLE " + TABLE_NAME +
            "(fin_cust_id INTEGER PRIMARY KEY," +
            "fst_cust_code TEXT," +
            "fst_cust_name TEXT," +
            "fst_cust_address TEXT," +
            "fst_cust_phone TEXT," +
            "fst_cust_location TEXT," +
            "fin_visit_day INTEGER," +
            "fin_price_group_id INTEGER," +
            "fbl_is_new INTEGER);";
    }


    public static void cleanTable(SQLiteDatabase db){
        String ssql = "DELETE FROM " + TABLE_NAME;
        db.execSQL(ssql);
    }

    public static void insert(SQLiteDatabase db, ContentValues contentValues){
        db.insert(TABLE_NAME,null,contentValues);
    }


    public static ContentValues getById(int fin_cust_id){
        SQLiteDatabase db = new AppDB(GlobalApplication.getAppContext()).getReadableDatabase();
        String ssql = "select * from " + TABLE_NAME + " where fin_cust_id = ?";
        Cursor rs = db.rawQuery(ssql,new String[]{String.valueOf(fin_cust_id)});

        ContentValues customer = null;
        if(rs.moveToFirst()){
            customer = cursorToCustomer(rs);
        }
        return customer;

    }

    public static ContentValues getByCode(String fst_cust_code){
        SQLiteDatabase db = new AppDB(GlobalApplication.getAppContext()).getReadableDatabase();
        String ssql = "select * from " + TABLE_NAME + " where fst_cust_code = ?";
        Cursor rs = db.rawQuery(ssql,new String[]{String.valueOf(fst_cust_code)});

        ContentValues customer = null;
        if(rs.moveToFirst()){
            customer = cursorToCustomer(rs);
        }
        return customer;

    }

    public static List<ContentValues> getCustomers(SQLiteDatabase db){
        Cursor rs = db.rawQuery("select * from " + TABLE_NAME,null);
        List<ContentValues> listCustomer = new ArrayList<ContentValues>();
        if(rs.moveToFirst()){
            do{
                ContentValues customer = cursorToCustomer(rs);
                listCustomer.add(customer);
            }while (rs.moveToNext());
        }
        return listCustomer;

    }

    public static List<ContentValues> getNewCustomers(SQLiteDatabase db){
        Cursor rs = db.rawQuery("select * from " + TABLE_NAME + " where fbl_is_new = 1",null);
        List<ContentValues> listCustomer = new ArrayList<ContentValues>();
        if(rs.moveToFirst()){
            do{
                ContentValues customer = cursorToCustomer(rs);
                listCustomer.add(customer);
            }while (rs.moveToNext());
        }
        return listCustomer;
    }

    public static int getTotalNewCustomer(){
        SQLiteDatabase db = new AppDB(GlobalApplication.getAppContext()).getReadableDatabase();
        Cursor rs = db.rawQuery("select count(fin_cust_id) as ttl_new_cust from " + TABLE_NAME + " where fbl_is_new = 1",null);
        int totalNewCust =0;
        if(rs.moveToFirst()){
            totalNewCust = rs.getInt(rs.getColumnIndex("ttl_new_cust"));
        }
        db.close();
        return totalNewCust;
    }

    public static  List<ContentValues> getScheduleCustomer(SQLiteDatabase db){
        Calendar c = Calendar.getInstance();
        c.setTime(Calendar.getInstance().getTime());
        //1 = sunday -> 7=saturday
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        //conver to match db
        if (dayOfWeek == 1){
            dayOfWeek = 7;
        }else{
            dayOfWeek = dayOfWeek -1;
        }

        Cursor rs = db.rawQuery("select * from " + TABLE_NAME + " where fin_visit_day = ?",new String[]{String.valueOf(dayOfWeek)});
        List<ContentValues> listCustomer =null;

        if(rs.moveToFirst()){
            listCustomer = new ArrayList<ContentValues>();

            do{
                ContentValues customer = cursorToCustomer(rs);
                listCustomer.add(customer);
            }while (rs.moveToNext());
        }
        return listCustomer;


    }


    public static boolean deleteById(SQLiteDatabase db,int fin_cust_id){
        db.execSQL("delete from " + TABLE_NAME + " where fin_cust_id = ?",new String[]{String.valueOf(fin_cust_id)} );
        return true;
    }

    public static boolean updateById(SQLiteDatabase db , ContentValues customer){
        db.update(TABLE_NAME,customer,"fin_cust_id = ? ",new String[]{customer.getAsString("fin_cust_id")});
        return true;
    }

    private static ContentValues cursorToCustomer(Cursor rs){
        ContentValues customer = new ContentValues();
        customer.put("fin_cust_id",rs.getInt(rs.getColumnIndex("fin_cust_id")));
        customer.put("fst_cust_code",rs.getString(rs.getColumnIndex("fst_cust_code")));
        customer.put("fst_cust_name",rs.getString(rs.getColumnIndex("fst_cust_name")));
        customer.put("fst_cust_address",rs.getString(rs.getColumnIndex("fst_cust_address")));
        customer.put("fst_cust_location",rs.getString(rs.getColumnIndex("fst_cust_location")));
        customer.put("fin_price_group_id",rs.getInt(rs.getColumnIndex("fin_price_group_id")));

        return customer;
    }


}
