package com.amex.mobileapps;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amex.mobileapps.adapters.CustomerAdapater;
import com.amex.mobileapps.model.AppDB;
import com.amex.mobileapps.model.CustomersModel;
import com.amex.mobileapps.model.IntentData;
import com.amex.mobileapps.services.UploadDataService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.qsystem.android.GlobalApplication;
import com.qsystem.android.utils.ServiceLocation;

import java.util.List;

public class CustomerRegistration extends AppCompatActivity {
    public CustomerAdapater custAdapter;
    public List<ContentValues> listCustomer;
    public ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);
        listView = findViewById(R.id.lvCustomer);
        SQLiteDatabase db = new AppDB(this).getReadableDatabase();
        listCustomer = CustomersModel.getNewCustomers(db);
        custAdapter = new CustomerAdapater(this,R.layout.list_customer_item,listCustomer);
        listView.setAdapter(custAdapter);
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Action Menu");
        menu.add(0, v.getId(), 0, "Info");
        menu.add(0, v.getId(), 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ContentValues selectedCust = listCustomer.get(menuInfo.position);

        if(item.getTitle().equals("Info")){

            Intent custInfoIntent = new Intent(CustomerRegistration.this,CustInfoActivity.class);
            IntentData.custActive = selectedCust;
            startActivity(custInfoIntent);

            Toast.makeText(this,"Info action",Toast.LENGTH_SHORT).show();

        }else if(item.getTitle().equals("Delete")){

            SQLiteDatabase db = new AppDB(this).getWritableDatabase();
            CustomersModel.deleteById(db,selectedCust.getAsInteger("fin_cust_id"));
            listCustomer.clear();
            listCustomer.addAll(CustomersModel.getNewCustomers(db));
            custAdapter.notifyDataSetChanged();
            db.close();
            Toast.makeText(this,"Delete action " + menuInfo.position ,Toast.LENGTH_SHORT).show();

        }
        return true;
        //return super.onContextItemSelected(item);
    }

    public void saveNewCustomer(View view){
        ServiceLocation serviceLocation = new ServiceLocation(this);
        final EditText txtName = findViewById(R.id.txtName);
        final EditText txtAddress = findViewById(R.id.txtAddress);
        final EditText txtPhone = findViewById(R.id.txtPhone);

        serviceLocation.getCurrentLocation(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                ContentValues customer = new ContentValues();
                customer.put("fst_cust_name",txtName.getText().toString());
                customer.put("fst_cust_address",txtAddress.getText().toString());
                customer.put("fst_cust_phone",txtPhone.getText().toString());
                customer.put("fin_visit_day",0);
                customer.put("fst_cust_location",location.getLatitude() + "," + location.getLongitude());
                customer.put("fin_price_group_id",0);
                customer.put("fbl_is_new",1);
                SQLiteDatabase db = new AppDB(CustomerRegistration.this).getWritableDatabase();
                CustomersModel.insert(db,customer);

                listCustomer.clear();
                listCustomer.addAll(CustomersModel.getNewCustomers(db));
                custAdapter.notifyDataSetChanged();

                Toast.makeText(GlobalApplication.getAppContext(),"Data Saved !",Toast.LENGTH_SHORT).show();
                txtName.setText("");
                txtAddress.setText("");
                txtPhone.setText("");
                txtName.requestFocus();

                UploadDataService.start();


            }
        });


    }
}
