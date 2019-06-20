package com.amex.mobileapps.services;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amex.mobileapps.Const;
import com.amex.mobileapps.R;
import com.amex.mobileapps.model.AppDB;
import com.amex.mobileapps.model.CustomersModel;
import com.amex.mobileapps.model.ItemFreeModel;
import com.amex.mobileapps.model.ItemsModel;
import com.amex.mobileapps.model.SettingsModel;
import com.amex.mobileapps.model.TargetModel;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.qsystem.android.utils.UploadData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DownloadData {

    public static void download_customers(final Activity activity){
        Log.d("SynchronizeDataServer","Start Process Synchronize");
        final ProgressBar progressBar = activity.findViewById(R.id.progressBar2);

        progressBar.setVisibility(View.VISIBLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        final TextView tvProcessStatus = activity.findViewById(R.id.tvProcessStatus);
        tvProcessStatus.setVisibility(View.VISIBLE);
        tvProcessStatus.setText("Download data customer ......");


        String urlString = SettingsModel.getValue(Const.KEY_HOST) +  Const.URL_FEED_CUSTOMER;
        UploadData downloadRequest = new UploadData(activity);
        ContentValues postParam = new ContentValues();
        postParam.put("app_id",SettingsModel.getValue(Const.KEY_APPID));

        downloadRequest.makeRequest(urlString,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {


                try{
                    JSONObject objResp = new JSONObject(response);
                    if (objResp.getString("status").equals("OK")){
                        SQLiteDatabase db = new AppDB(activity).getWritableDatabase();
                        CustomersModel.cleanTable(db);
                        JSONArray arrCustomer = objResp.getJSONArray("data");
                        for(int i = 0;i < arrCustomer.length();i++){
                            JSONObject customer = arrCustomer.getJSONObject(i);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("fin_cust_id",customer.getInt("fin_cust_id"));
                            contentValues.put("fst_cust_code",customer.getString("fst_cust_code"));
                            contentValues.put("fst_cust_name",customer.getString("fst_cust_name"));
                            contentValues.put("fst_cust_address",customer.getString("fst_cust_address"));
                            contentValues.put("fst_cust_phone",customer.getString("fst_cust_phone"));
                            contentValues.put("fin_visit_day",customer.getString("fin_visit_day"));
                            contentValues.put("fst_cust_location",customer.getString("fst_cust_location"));
                            contentValues.put("fin_price_group_id",customer.getString("fin_price_group_id"));
                            contentValues.put("fbl_is_new",0);
                            CustomersModel.insert(db,contentValues);
                        }

                        Date date = Calendar.getInstance().getTime();
                        String pattern = "yyyy-MM-dd HH:mm:ss";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        SettingsModel.setValue("LastSyncDateTime",simpleDateFormat.format(date));

                        db.close();
                        DownloadData.download_items(activity);

                    }else{
                        String message = objResp.getString("message");
                        progressBar.setVisibility(View.GONE);
                        tvProcessStatus.setVisibility(View.GONE);
                        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
                    }


                    //activity.finish();
                    //activity.startActivity(activity.getIntent());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //progressBar.setVisibility(View.GONE);
                //activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                tvProcessStatus.setVisibility(View.GONE);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        },postParam,null);
    }

    public static void download_items(final  Activity activity){
        Log.d("SynchronizeDataServer","Start Download Items");
        final ProgressBar progressBar = activity.findViewById(R.id.progressBar2);
        final TextView tvProcessStatus = activity.findViewById(R.id.tvProcessStatus);
        tvProcessStatus.setText("Download data item ......");

        //progressBar.setVisibility(View.VISIBLE);
        //activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        String urlString = SettingsModel.getValue(Const.KEY_HOST) +  Const.URL_FEED_ITEMS;


        UploadData downloadRequest = new UploadData(activity);
        ContentValues postParam = new ContentValues();
        postParam.put("app_id",SettingsModel.getValue(Const.KEY_APPID));

        downloadRequest.makeRequest(urlString,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                SQLiteDatabase db = new AppDB(activity).getWritableDatabase();
                ItemsModel.cleanTable(db);
                try{
                    JSONObject objResp = new JSONObject(response);
                    JSONArray arrItem = objResp.getJSONArray("data");
                    for(int i = 0;i < arrItem.length();i++){
                        JSONObject item = arrItem.getJSONObject(i);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("fst_item_code",item.getString("fst_item_code"));
                        contentValues.put("fst_item_name",item.getString("fst_item_name"));
                        contentValues.put("fst_satuan_1",item.getString("fst_satuan_1"));
                        contentValues.put("fst_satuan_2",item.getString("fst_satuan_2"));
                        contentValues.put("fst_satuan_3",item.getString("fst_satuan_3"));
                        contentValues.put("fin_conversion_2",item.getDouble("fin_conversion_2"));
                        contentValues.put("fin_conversion_3",item.getDouble("fin_conversion_3"));
                        contentValues.put("fin_selling_price1A",item.getDouble("fin_selling_price1A"));
                        contentValues.put("fin_selling_price2A",item.getDouble("fin_selling_price2A"));
                        contentValues.put("fin_selling_price3A",item.getDouble("fin_selling_price3A"));
                        contentValues.put("fin_selling_price1B",item.getDouble("fin_selling_price1B"));
                        contentValues.put("fin_selling_price2B",item.getDouble("fin_selling_price2B"));
                        contentValues.put("fin_selling_price3B",item.getDouble("fin_selling_price3B"));
                        contentValues.put("fin_selling_price1C",item.getDouble("fin_selling_price1C"));
                        contentValues.put("fin_selling_price2C",item.getDouble("fin_selling_price2C"));
                        contentValues.put("fin_selling_price3C",item.getDouble("fin_selling_price3C"));
                        contentValues.put("fin_selling_price1D",item.getDouble("fin_selling_price1D"));
                        contentValues.put("fin_selling_price2D",item.getDouble("fin_selling_price2D"));
                        contentValues.put("fin_selling_price3D",item.getDouble("fin_selling_price3D"));
                        contentValues.put("fin_selling_price1E",item.getDouble("fin_selling_price1E"));
                        contentValues.put("fin_selling_price2E",item.getDouble("fin_selling_price2E"));
                        contentValues.put("fin_selling_price3E",item.getDouble("fin_selling_price3E"));
                        contentValues.put("fin_selling_price1F",item.getDouble("fin_selling_price1F"));
                        contentValues.put("fin_selling_price2F",item.getDouble("fin_selling_price2F"));
                        contentValues.put("fin_selling_price3F",item.getDouble("fin_selling_price3F"));
                        contentValues.put("fin_selling_price1G",item.getDouble("fin_selling_price1G"));
                        contentValues.put("fin_selling_price2G",item.getDouble("fin_selling_price2G"));
                        contentValues.put("fin_selling_price3G",item.getDouble("fin_selling_price3G"));
                        contentValues.put("fin_selling_price1H",item.getDouble("fin_selling_price1H"));
                        contentValues.put("fin_selling_price2H",item.getDouble("fin_selling_price2H"));
                        contentValues.put("fin_selling_price3H",item.getDouble("fin_selling_price3H"));
                        contentValues.put("fin_selling_price1I",item.getDouble("fin_selling_price1I"));
                        contentValues.put("fin_selling_price2I",item.getDouble("fin_selling_price2I"));
                        contentValues.put("fin_selling_price3I",item.getDouble("fin_selling_price3I"));
                        contentValues.put("fin_selling_price1J",item.getDouble("fin_selling_price1J"));
                        contentValues.put("fin_selling_price2J",item.getDouble("fin_selling_price2J"));
                        contentValues.put("fin_selling_price3J",item.getDouble("fin_selling_price3J"));
                        contentValues.put("fst_memo",item.getString("fst_memo"));

                        ItemsModel.insert(db,contentValues);

                    }

                    Date date = Calendar.getInstance().getTime();
                    String pattern = "yyyy-MM-dd HH:mm:ss";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    SettingsModel.setValue("LastSyncDateTime",simpleDateFormat.format(date));


                    //activity.finish();
                    //activity.startActivity(activity.getIntent());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                db.close();
                DownloadData.download_promo_free_items(activity);

                //progressBar.setVisibility(View.GONE);
                //activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                tvProcessStatus.setVisibility(View.GONE);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        },postParam,null);
    }

    public static void download_promo_free_items(final  Activity activity){
        Log.d("SynchronizeDataServer","Start download Promo free items");
        final ProgressBar progressBar = activity.findViewById(R.id.progressBar2);
        final TextView tvProcessStatus = activity.findViewById(R.id.tvProcessStatus);
        tvProcessStatus.setText("Download promo free item ......");

        //progressBar.setVisibility(View.VISIBLE);
        //activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        String urlString = SettingsModel.getValue(Const.KEY_HOST) +  Const.URL_FEED_PROMO_FREE_ITEM;


        UploadData downloadRequest = new UploadData(activity);
        ContentValues postParam = new ContentValues();
        postParam.put("app_id",SettingsModel.getValue(Const.KEY_APPID));

        downloadRequest.makeRequest(urlString,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                SQLiteDatabase db = new AppDB(activity).getWritableDatabase();
                ItemFreeModel.cleanTable(db);
                try{
                    JSONObject objResp = new JSONObject(response);
                    JSONArray arrItem = objResp.getJSONArray("data");
                    for(int i = 0;i < arrItem.length();i++){
                        JSONObject item = arrItem.getJSONObject(i);
                        ContentValues promo = new ContentValues();

                        promo.put("fin_promo_id",item.getInt("fin_promo_id"));
                        promo.put("fst_item_code_awal",item.getString("fst_item_code_awal"));
                        promo.put("fst_item_code_akhir",item.getString("fst_item_code_akhir"));
                        promo.put("fin_qty",item.getInt("fin_qty"));
                        promo.put("fst_satuan",item.getString("fst_satuan"));
                        promo.put("fst_free_item_code_awal",item.getString("fst_free_item_code_awal"));
                        promo.put("fst_free_item_code_akhir",item.getString("fst_free_item_code_akhir"));
                        promo.put("fin_free_qty",item.getInt("fin_free_qty"));
                        promo.put("fst_free_satuan",item.getString("fst_free_satuan"));
                        promo.put("fdt_date_start",item.getString("fdt_date_start"));
                        promo.put("fdt_date_end",item.getString("fdt_date_end"));
                        promo.put("fin_price_group_id",item.getInt("fin_price_group_id"));
                        promo.put("fbl_multiple_free",item.getInt("fbl_multiple_free"));
                        promo.put("fin_max_free_qty",item.getInt("fin_max_free_qty"));
                        ItemFreeModel.insert(db,promo);
                    }
                    download_target(activity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                db.close();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                tvProcessStatus.setVisibility(View.GONE);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        },postParam,null);
    }

    public static void download_target(final  Activity activity){
        Log.d("SynchronizeDataServer","Start download Target Per Customer");
        final ProgressBar progressBar = activity.findViewById(R.id.progressBar2);
        final TextView tvProcessStatus = activity.findViewById(R.id.tvProcessStatus);
        tvProcessStatus.setText("Download target per customer ......");


        String urlString = SettingsModel.getValue(Const.KEY_HOST) +  Const.URL_FEED_TARGET_PER_CUSTOMER;


        UploadData downloadRequest = new UploadData(activity);
        ContentValues postParam = new ContentValues();
        postParam.put("app_id",SettingsModel.getValue(Const.KEY_APPID));

        downloadRequest.makeRequest(urlString,new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                SQLiteDatabase db = new AppDB(activity).getWritableDatabase();
                TargetModel.cleanTable(db);
                try{
                    JSONObject objResp = new JSONObject(response);
                    JSONArray arrItem = objResp.getJSONArray("data");
                    for(int i = 0;i < arrItem.length();i++){
                        JSONObject item = arrItem.getJSONObject(i);
                        ContentValues target = new ContentValues();

                        target.put("fin_target_id",item.getInt("fin_target_id"));
                        target.put("fst_cust_code",item.getString("fst_cust_code"));
                        target.put("fst_item_code",item.getString("fst_item_code"));
                        target.put("fin_qty_target",item.getInt("fin_target"));
                        target.put("fst_satuan",item.getString("fst_satuan"));
                        target.put("fdt_date_start",item.getString("fdt_date_start"));
                        target.put("fdt_date_end",item.getString("fdt_date_end"));
                        target.put("fst_program_code",item.getString("fst_program_code"));
                        target.put("fin_current_qty",item.getInt("fin_current_qty"));
                        TargetModel.insert(db,target);
                    }

                    Date date = Calendar.getInstance().getTime();
                    String pattern = "yyyy-MM-dd HH:mm:ss";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    SettingsModel.setValue("LastSyncDateTime",simpleDateFormat.format(date));
                    activity.finish();
                    activity.startActivity(activity.getIntent());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                db.close();

                progressBar.setVisibility(View.GONE);
                tvProcessStatus.setVisibility(View.GONE);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                tvProcessStatus.setVisibility(View.GONE);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        },postParam,null);
    }
}
