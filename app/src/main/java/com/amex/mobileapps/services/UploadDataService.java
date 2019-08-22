package com.amex.mobileapps.services;
import android.app.Application;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.amex.mobileapps.Const;
import com.amex.mobileapps.model.AppDB;
import com.amex.mobileapps.model.CheckinLogModel;
import com.amex.mobileapps.model.CustomersModel;
import com.amex.mobileapps.model.SettingsModel;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.qsystem.android.GlobalApplication;
import com.qsystem.android.utils.TakePhoto;
import com.qsystem.android.utils.UploadData;
import com.qsystem.android.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UploadDataService extends IntentService {

    public int requestProcess = 0;
    public UploadDataService() {
        super("UploadDataService");
    }
    public static boolean runService = false;


    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            while(UploadDataService.runService){

                uploadCheckinLogs();
                uploadNewCustomer();

                try{ Thread.sleep(2000);}catch (Exception e){}
                while(requestProcess > 0 ){
                    Log.d("MonitorVolley","Monitor Request Checkin Process :" + String.valueOf(requestProcess) + " Thread still running ");
                    try{ Thread.sleep(1000);}catch (Exception e){}
                }
            }
        }
    }


    private void uploadCheckinLogs(){
        String uRLUploadCheckinLog = SettingsModel.getValue(Const.KEY_HOST) +   Const.URL_UPLOAD_CHECKIN_LOGS;
        Log.d("BackroundUpload","Upload Checkin logs Service " + Utils.getCurrentDatetime());

        //upload checkin log
        SQLiteDatabase dbRead = new AppDB(this).getReadableDatabase();

        List<ContentValues> records =  CheckinLogModel.getRecords(dbRead);
        if(records.size() == 0 ){
            UploadDataService.runService = false;
        }
        for(ContentValues checkInLogs : records){
            Log.d("BackroundUpload","Upload " + checkInLogs.getAsString("fin_id") + ":" + checkInLogs.getAsString("fst_photo_path"));

            UploadData uploadData = new UploadData(getApplicationContext());

            Response.Listener<String> respListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //{"status":"OK","fin_id":"1"}
                    try {
                        Log.d("Upload Response",response);
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("OK")){

                            int fin_id = Integer.parseInt(jsonObject.getString("fin_id"));
                            SQLiteDatabase db = new AppDB(UploadDataService.this).getWritableDatabase();
                            ContentValues checkinLog = CheckinLogModel.getById(db,fin_id);
                            if (checkinLog != null){
                                //delete file
                                try{new File(checkinLog.getAsString("fst_photo_path") ).getAbsoluteFile().delete();}catch (Exception e){}
                                //delete records
                                CheckinLogModel.deleteById(db,fin_id);
                            }
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestProcess-- ;
                    Log.d("RespUpload",response);
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    requestProcess--;
                }
            };

            ContentValues postParams = new ContentValues();
            postParams.put("app_id",SettingsModel.getValue(Const.KEY_APPID));
            postParams.put("fin_id",checkInLogs.getAsString("fin_id"));
            postParams.put("fst_cust_code",checkInLogs.getAsString("fst_cust_code"));
            postParams.put("fdt_checkin_datetime",checkInLogs.getAsString("fdt_checkin_datetime"));
            postParams.put("fdt_checkout_datetime",checkInLogs.getAsString("fdt_checkout_datetime"));
            postParams.put("fst_checkin_location",checkInLogs.getAsString("fst_checkin_location"));

            String imagePath = checkInLogs.getAsString("fst_photo_path");
            //Bitmap image = BitmapFactory.decodeFile(imagePath);
            //image.recycle();
            Bitmap resizeImage = TakePhoto.getResizedBitmap(imagePath,200);
            ContentValues uploadFile = null;
            if (resizeImage != null){
                resizeImage.recycle();
                uploadFile = new ContentValues();
                uploadFile.put("name","photoloc");
                uploadFile.put("filePath",checkInLogs.getAsString("fst_photo_path"));
            }
            uploadData.makeRequest(uRLUploadCheckinLog,respListener,errorListener,postParams,uploadFile);
            requestProcess++;
        }
        dbRead.close();

    }

    private void uploadNewCustomer(){
        String uRLUploadCheckinLog = SettingsModel.getValue(Const.KEY_HOST) +   Const.URL_UPLOAD_NEW_CUSTOMER;

        Log.d("BackroundUpload","Upload New Customer Service " + Utils.getCurrentDatetime());

        //upload checkin log
        SQLiteDatabase dbread = new AppDB(this).getReadableDatabase();

        List<ContentValues> newCusts = CustomersModel.getNewCustomers(dbread);

        if(newCusts.size() != 0 ){
            UploadDataService.runService = true;
        }

        for(ContentValues newCust : newCusts){
            Log.d("BackroundUpload","Upload new customer :" + newCust.getAsString("fst_cust_name"));
            UploadData uploadData = new UploadData(getApplicationContext());
            Response.Listener<String> respListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //{"status":"OK","fin_cust_id":"1"}
                    try {
                        Log.d("Upload Response",response);
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("OK")){
                            int fin_cust_id = Integer.parseInt(jsonObject.getString("fin_cust_id"));
                            SQLiteDatabase db = new AppDB(UploadDataService.this).getWritableDatabase();
                            ContentValues cust = CustomersModel.getById(fin_cust_id);
                            if (cust != null){
                                ContentValues val = new ContentValues();
                                val.put("fin_cust_id",cust.getAsInteger("fin_cust_id"));
                                val.put("fbl_is_new",2);
                                CustomersModel.updateById(db,val);
                            }
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestProcess-- ;
                    Log.d("RespUpload",response);
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   requestProcess--;
                }
            };

            ContentValues postParams = new ContentValues();

            postParams.put("app_id",SettingsModel.getValue(Const.KEY_APPID));
            postParams.put("fin_cust_id",newCust.getAsInteger("fin_cust_id"));
            postParams.put("fst_cust_name",newCust.getAsString("fst_cust_name"));
            postParams.put("fst_cust_address",newCust.getAsString("fst_cust_address"));
            postParams.put("fst_cust_phone",newCust.getAsString("fst_cust_phone"));
            postParams.put("fst_cust_location",newCust.getAsString("fst_cust_location"));
            uploadData.makeRequest(uRLUploadCheckinLog,respListener,errorListener,postParams,null);

            requestProcess++;

        } //end for
        dbread.close();

    }

    public static void start(){
        if (!runService) {
            UploadDataService.runService = true;
            Intent intentUploadData = new Intent(GlobalApplication.getAppContext(), UploadDataService.class);
            GlobalApplication.getAppContext().startService(intentUploadData);
        }

    }
}
