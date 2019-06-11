package com.amex.mobileapps;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amex.mobileapps.model.AppDB;
import com.amex.mobileapps.R;
import com.amex.mobileapps.model.SettingsModel;
import com.amex.mobileapps.services.UploadDataService;
import com.qsystem.android.GlobalApplication;

public class SplashActivity extends AppCompatActivity {
    private int waktu_loading = 4000;
    private ProgressBar progressBar;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progressBar);
        //progressBar.setMin(0);
        progressBar.setMax(4);

        AppDB appDB = new AppDB(this);
        SQLiteDatabase db =  appDB.getWritableDatabase();
        Log.d("Databases","getWritableDatabase");
        db.close();


        /*
        new Thread(new Runnable(){
            @Override
            public void run() {
                for (i = 0; i<10;i++){
                    Log.d("CekI",Integer.toString(i));
                    progressBar.setProgress(i);
                }
                Intent mainActivity = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        }).start();
        */

        String appid = SettingsModel.getValue("appid");



        if (appid == null ){

            new Thread(new Runnable(){

                @Override
                public void run() {
                    for(i = 0 ; i < 4 ;i++){
                        progressBar.setProgress(i);
                        try{Thread.sleep(100);}catch (Exception e){}
                    }

                    Intent settingActivity = new Intent(SplashActivity.this, SettingActivity.class);
                    startActivity(settingActivity);
                    finish();

                }
            }).start();

        }else{
            new Thread(new Runnable(){

                @Override
                public void run() {

                    for(i = 0 ; i < 4 ;i++){
                        progressBar.setProgress(i);
                        try{Thread.sleep(100);}catch (Exception e){}
                    }
                    Intent mainActivity = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainActivity);
                    finish();

                }
            }).start();


            //Intent intentUploadData = new Intent(GlobalApplication.getAppContext(), UploadDataService.class);
            //startService(intentUploadData);
            UploadDataService.start();

        }
        //Toast.makeText(this, "Start Upload data", Toast.LENGTH_LONG).show();
        //Log.d("UploadDataService", "Start Upload data");





        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainActivity = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        },4000);
        */
    }
}
