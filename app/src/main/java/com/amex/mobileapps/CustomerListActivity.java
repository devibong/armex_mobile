package com.amex.mobileapps;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.amex.mobileapps.R;
import com.amex.mobileapps.adapters.CustomerAdapater;
import com.amex.mobileapps.model.AppDB;
import com.amex.mobileapps.model.CustomersModel;
import com.amex.mobileapps.model.IntentData;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerListActivity extends AppCompatActivity {
    private IntentIntegrator intentIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        ListView listView = findViewById(R.id.custListView);

        SearchView searchView = findViewById(R.id.searchView);

        SQLiteDatabase db = new AppDB(this).getReadableDatabase();
        final List<ContentValues> listCustomer = CustomersModel.getCustomers(db);
        CustomerAdapater adapater =new CustomerAdapater(this,R.layout.list_customer_item,listCustomer);
        listView.setAdapter(adapater);
        searchView.setOnQueryTextListener(getSearchListener(adapater));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContentValues data = listCustomer.get(position);
                Intent custInfoIntent = new Intent(CustomerListActivity.this,CustInfoActivity.class);
                IntentData.custActive = data;
                startActivity(custInfoIntent);
            }
        });
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
                    String strResult = result.getContents();
                    //strResult = "{\"fst_cust_code\":1001}";

                    JSONObject object = new JSONObject(strResult);
                    //ContentValues customer = CustomersModel.getById(object.getInt("fin_cust_id"));
                    ContentValues customer = CustomersModel.getByCode(object.getString("fst_cust_code"));

                    if (customer != null ){
                        Intent custInfoIntent = new Intent(CustomerListActivity.this,CustInfoActivity.class);
                        IntentData.custActive = customer;
                        startActivity(custInfoIntent);
                    }else{
                        Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // jika format encoded tidak sesuai maka hasil
                    // ditampilkan ke toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private SearchView.OnQueryTextListener getSearchListener(final CustomerAdapater adapater){
        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapater.filter(newText);
                return false;
            }
        };

        return listener;
    }

    public void scanBarcode(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.image_click));

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Const.PERMISSIONS_REQUEST_CAMERA);

        } else {
            intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setPrompt("Please scan customer's QR Code");
            intentIntegrator.initiateScan();

            /*
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Test Confirmation");
            builder.setMessage("Test Confirmation, Continue ?");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intentIntegrator = new IntentIntegrator(MainActivity.this);
                    intentIntegrator.initiateScan();
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            */

        }


    }

    public void newCustomer(View view){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.image_click));
        Intent intent = new Intent(this,CustomerRegistration.class);
        startActivity(intent);
    }

    public void scheduleList(View view){
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.image_click));
        Intent intent = new Intent(this,CustomerVisitListActivity.class);
        startActivity(intent);

    }

}
