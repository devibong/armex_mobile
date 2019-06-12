package com.amex.mobileapps;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amex.mobileapps.model.SettingsModel;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.qsystem.android.utils.UploadData;

import org.json.JSONObject;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }


    public void validateSetting(View v){
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        UploadData postReq = new UploadData(this);
        final String txtHostAddress = ((EditText) findViewById(R.id.txtHostAddress)).getText().toString();
        final String txtAppId = ((EditText) findViewById(R.id.txtAppId)).getText().toString();


        String urlCheckAppid = txtHostAddress + "check_appid";
        ContentValues postParam = new ContentValues();
        postParam.put("app_id", txtAppId);

        postReq.makeRequest(urlCheckAppid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Host address valid
                try {
                    JSONObject objResp = new JSONObject(response);
                    if (objResp.getString("status").equals("OK")){
                        // Save to database
                        SettingsModel.setValue(Const.KEY_APPID,txtAppId);
                        SettingsModel.setValue(Const.KEY_HOST,txtHostAddress);


                        Intent mainActivity = new Intent(SettingActivity.this, MainActivity.class);
                        startActivity(mainActivity);
                        finish();
                    }else{
                        Toast.makeText(SettingActivity.this,"Invalid Application ID !",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(SettingActivity.this,"Invalid Application ID !",Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Host Address Invalid
                Toast.makeText(SettingActivity.this,"Invalid Host Address !",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        },postParam, null);


    }
}
