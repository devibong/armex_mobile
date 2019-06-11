package com.amex.mobileapps;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amex.mobileapps.adapters.CustomerAdapater;
import com.amex.mobileapps.model.AppDB;
import com.amex.mobileapps.model.CustomersModel;
import com.amex.mobileapps.model.IntentData;

import java.util.List;

public class CustomerVisitListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_visit_list);
        ListView lvCustVisit = findViewById(R.id.lvCustVisit);
        SQLiteDatabase db = new AppDB(this).getReadableDatabase();

        final List<ContentValues> listCustomer = CustomersModel.getScheduleCustomer(db);

        CustomerAdapater adapter = new CustomerAdapater(this, R.layout.list_customer_item,listCustomer);
        lvCustVisit.setAdapter(adapter);

        lvCustVisit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContentValues data = listCustomer.get(position);
                Intent custInfoIntent = new Intent(CustomerVisitListActivity.this,CustInfoActivity.class);
                IntentData.custActive = data;
                startActivity(custInfoIntent);
            }
        });

    }
}
