package com.amex.mobileapps;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amex.mobileapps.model.AppDB;
import com.amex.mobileapps.model.CheckinLogModel;
import com.amex.mobileapps.model.IntentData;
import com.amex.mobileapps.services.UploadDataService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.qsystem.android.GlobalApplication;
import com.qsystem.android.utils.ServiceLocation;
import com.qsystem.android.utils.TakePhoto;
import com.qsystem.android.utils.Utils;

public class CustInfoActivity extends AppCompatActivity {

    TakePhoto takePhoto = new TakePhoto(this);
    TextView tvCustName;
    TextView tvCustAddress;
    TextView tvCustId;
    String fstCustCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_info);
        ContentValues data = IntentData.custActive;
        tvCustName = findViewById(R.id.tvCustName);
        tvCustAddress = findViewById(R.id.tvCustAddress);
        tvCustId = findViewById(R.id.tvCustId);
        tvCustId.setText(data.getAsString("fin_cust_id"));
        tvCustName.setText(data.getAsString("fst_cust_name"));
        tvCustAddress.setText(data.getAsString("fst_cust_address"));
        fstCustCode = data.getAsString("fst_cust_code");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Const.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            ImageView ivCheckIn = findViewById(R.id.ivCheckIn);
            if (data != null) {
                Bundle extras = data.getExtras();
                //bitmap thumbnail
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ivCheckIn.setImageBitmap(imageBitmap);
            }else{
                ivCheckIn.setImageBitmap(takePhoto.getBitmapPictureResult(ivCheckIn.getWidth(),ivCheckIn.getHeight()));
            }
        }
    }

    public void takePhoto(View view){

        if (CheckinLogModel.statusClear(this.fstCustCode)) {
            takePhoto.takePicture();
        }else{
            Toast.makeText(this,"Please checkout last customer to continue !",Toast.LENGTH_SHORT).show();
        }
    }

    public void checkIn(View view){
        final String filePath = takePhoto.getFilePath();

        if (filePath.equals("")){
            Toast.makeText(this,"Please take a picture before chekin location !",Toast.LENGTH_SHORT).show();
            return;
        }

        //Get Location
        ServiceLocation serviceLocation = new ServiceLocation(this,ServiceLocation.PRIORITY_HIGH_ACCURACY);
        serviceLocation.updateCurrentLocation();
        serviceLocation.getCurrentLocation(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //add Location to database;
                SQLiteDatabase db = new AppDB(CustInfoActivity.this).getWritableDatabase();
                ContentValues dataLog = new ContentValues();

                //String custId = tvCustId.getText().toString();
                //dataLog.put("fin_cust_id",Integer.parseInt(custId));
                dataLog.put("fst_cust_code",fstCustCode);

                dataLog.put("fdt_checkin_datetime", Utils.getCurrentDatetime());
                dataLog.put("fst_checkin_location", String.valueOf(location.getLatitude()) + "," +  String.valueOf(location.getLongitude()) );
                dataLog.put("fst_photo_path", filePath);
                CheckinLogModel.insert(db,dataLog);
                db.close();
                ImageView ivCheckIn = findViewById(R.id.ivCheckIn);
                ivCheckIn.setImageDrawable(null);

                Toast.makeText(CustInfoActivity.this,"Data Location Saved !",Toast.LENGTH_SHORT).show();

                //UploadDataService.start();

                //String message = "Add checkin fo cust : " + tvCustId.getText().toString()   + " -  " + tvCustName.getText().toString() + " photo :" + filePath  +  ", Location :" + location.getLatitude() + "," + location.getLongitude() ;
                //String message = "Check in location !";
                //Toast.makeText(CustInfoActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void checkOut(View view){
        CheckinLogModel.checkOut();
        UploadDataService.start();
        Toast.makeText(this,"Checkout success !",Toast.LENGTH_SHORT).show();
    }


    public void showPromotion(View view){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.image_click));
        Intent intent = new Intent(this, FreeItemListActivity.class);
        startActivity(intent);
    }

    public void showTarget(View view){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.image_click));
        Intent intent = new Intent(this, TargetListActivity.class);
        startActivity(intent);
    }

    public void showItems(View view){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.image_click));
        Intent intent = new Intent(this, ItemListActivity.class);
        startActivity(intent);
    }

}
