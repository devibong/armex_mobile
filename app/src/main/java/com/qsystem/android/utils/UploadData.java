package com.qsystem.android.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.amex.mobileapps.Const;
import com.amex.mobileapps.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadData {

    //Activity activity;

    public final Context context;
    public UploadData(Context context){
        this.context = context;
    }


    public  void makeRequest(String url, Response.Listener<String> responseListener, ContentValues postParams,ContentValues uploadFile ){
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response",error.getMessage());
            }
        };

        this.makeRequest(url,responseListener,errorListener,postParams,uploadFile);
    }

    public  void makeRequest(String url, Response.Listener<String> responseListener,Response.ErrorListener errorListener, ContentValues postParams,ContentValues uploadFile ){

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, url, responseListener,errorListener );

        if (postParams != null) {
            for (String key : postParams.keySet()) {
                smr.addStringParam(key, postParams.getAsString(key));
            }
        }

        if (uploadFile != null){
            smr.addFile(uploadFile.getAsString("name"),uploadFile.getAsString("filePath"));
        }

        RetryPolicy retryPolicy = new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 3;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
                    Log.d("RequestHttp","Timeout / No Connection :" + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    //Error indicating that there was an Authentication Failure while performing the request
                    Log.d("RequestHttp","Auth Failed :" + error.getMessage());
                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Log.d("RequestHttp","Server Error :" + error.getMessage());
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Log.d("RequestHttp","Network Error :" + error.getMessage());
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Log.d("RequestHttp","Parse Error :" + error.getMessage());
                }
            }
        };


        //smr.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        smr.setRetryPolicy(retryPolicy);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        mRequestQueue.add(smr);
        mRequestQueue.start();

    }


}
