package com.amex.mobileapps;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amex.mobileapps.model.CheckinLogModel;
import com.amex.mobileapps.model.CustomersModel;
import com.amex.mobileapps.model.SettingsModel;
import com.amex.mobileapps.services.DownloadData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qsystem.android.utils.MCrypt;
import com.qsystem.android.utils.ServiceLocation;
import com.qsystem.android.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";



    private CircleImageView imgvSyncData;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgvSyncData = findViewById(R.id.imgvSyncData);
        imgvSyncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronizeData(v);
            }
        });
        progressBar = findViewById(R.id.progressBar2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshInfoDesktop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("OnRequestPermissionRslt","RequestCode : " + requestCode);

        switch (requestCode) {
            case Const.PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            txtLocation.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
                        }
                    });
                    */
                } else {
                    Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("OnActivityResult","RequestCode : " + requestCode);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();
            } else {
                // jika qrcode berisi data
                try {
                    // converting the data json
                    JSONObject object = new JSONObject(result.getContents());
                    // atur nilai ke textviews
                    //textViewNama.setText(object.getString("nama"));
                    //textViewTinggi.setText(object.getString("tinggi"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    // jika format encoded tidak sesuai maka hasil
                    // ditampilkan ke toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    public void showListCustomer(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.image_click));

        Intent intent = new Intent(this, CustomerListActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText4);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }






    public void synchronizeData(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.image_click));
        //new UploadDataService().synchronizeDataServer(progressBar,this);
        DownloadData.download_customers(this);
        //DownloadData.download_items(progressBar,this);
        //DownloadData.download_promo_free_items(progressBar,this);


        //finish();
        //startActivity(getIntent());



        /*
        new AsyncTask<Object,Object,Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                //super.onPostExecute(o);
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(MainActivity.this,"Synchronize Data Finished! ",Toast.LENGTH_SHORT).show();

            }
        }.execute();
        */

        Toast.makeText(this, "Synchronize Data!", Toast.LENGTH_SHORT).show();
    }

    public void showLocation(View view) {
        //Uri locUri = Uri.parse("geo:37.7749,-122.4194?q=" + Uri.encode("MyLocation"));
        //Uri locUri = Uri.parse("geo:-6.1824826,106.6727031?q=" + Uri.encode("Homien"));
        final ServiceLocation serviceLocation = new ServiceLocation(this);

        //serviceLocation.updateCurrentLocation();

        serviceLocation.getCurrentLocation(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Logic to handle location object
                    Double lat = location.getLatitude();
                    Double lon = location.getLongitude();
                    EditText editText = findViewById(R.id.editText);
                    String message = "Latitude : " + Double.toString(lat) + ",Longitude : " + Double.toString(lon) + " -  Accuracy : " + Float.toString(location.getAccuracy()) + " meters";
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    editText.setText(message);

                    //Uri locUri = Uri.parse("geo:<-6.1824826>,<106.6727031>?q=<-6.1824826>,<106.6727031>(Test)");

                    Uri locUri = Uri.parse("geo:<" + Double.toString(lat) + ">,<" + Double.toString(lon) + "?q=<" + Double.toString(lat)  + ">,<" + Double.toString(lon) + ">(Test)");

                    Intent mapIntent = new Intent(Intent.ACTION_VIEW,locUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null){
                        startActivity(mapIntent);
                    }
                }else{
                    //serviceLocation.updateCurrentLocation();
                    Toast.makeText(MainActivity.this, "Location not available, please try again ! ", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void testEncryp(View view){

        /* di javanya */
        /*
        try {
            String key = "bastian1bastian1";
            String strIv  ="1231231231231232";
            String message = "asdkljasdjk ada sdkajksld aksjdfa ds;fasdf asdfjkasdfj   asdfklja sdfja sdfj asdfj kasdfj asd f asdf af as df klasdjflasdjklflaksdlfj;as df asdf asd;fklj asdfkj asd;fklj asdfkj asdfj asdfj ;asdfj asdjkf;asdkjf";
            //byte[] bytes = Base64.decodeBase64(iv);
            Cipher ciper = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"),"AES");
            //IvParameterSpec iv = new IvParameterSpec(ivBytes,0, ciper.getBlockSize());
            IvParameterSpec iv = new IvParameterSpec(strIv.getBytes(),0, ciper.getBlockSize());
            //ciper.init(Cipher.DECRYPT_MODE, keySpec, iv);
            ciper.init(Cipher.ENCRYPT_MODE,keySpec,iv);

            byte[] cipherText = ciper.doFinal(message.getBytes("UTF-8"));
            String strCipher = Base64.encodeToString(cipherText,0);
            Log.d("TEST ENCY",strCipher);


        }catch (Exception e){
            e.printStackTrace();

        }

        */
        /* php nya
        $data = "testing 123456";
        $method = "AES-128-CBC";
        $key = "bastian1bastian1";
        $iv= "1231231231231232";
        echo openssl_decrypt ( $encry,$method ,$key,0,$iv);
        * */
        refreshInfoDesktop();


    }

    private static byte[] array_concat(final byte[] a, final byte[] b) {
        final byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }


    public void refreshInfoDesktop(){
        //Last data Download
        TextView tvLastSyncDateTime = findViewById(R.id.tvLastSyncDateTime);
        tvLastSyncDateTime.setText(Utils.formatDateTime(SettingsModel.getValue("LastSyncDateTime"),"dd MMM yyyy HH:mm:ss"));

        //Total data Login
        TextView tvPendingLogin = findViewById(R.id.tvPendingLogin);
        tvPendingLogin.setText(String.valueOf(CheckinLogModel.getTotalPending()));

        //Total New Customer
        TextView tvPendingNewCust = findViewById(R.id.tvPendingNewCust);
        tvPendingNewCust.setText(String.valueOf(CustomersModel.getTotalNewCustomer()));
    }


}
