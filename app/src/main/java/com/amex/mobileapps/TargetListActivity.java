package com.amex.mobileapps;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.amex.mobileapps.adapters.TargetAdapater;
import com.amex.mobileapps.model.IntentData;
import com.amex.mobileapps.model.TargetModel;

import java.lang.reflect.Array;
import java.util.List;

public class TargetListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_list);
        ListView lv = findViewById(R.id.lvTarget);
        List<ContentValues> listTarget =TargetModel.getTarget(IntentData.custActive.getAsString("fst_cust_code"));
        TargetAdapater adapter = new TargetAdapater(this,R.layout.list_target_item,listTarget);
        lv.setAdapter(adapter);
    }
}
