package com.amex.mobileapps;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.amex.mobileapps.adapters.FreeItemAdapater;
import com.amex.mobileapps.model.IntentData;
import com.amex.mobileapps.model.ItemFreeModel;

import java.util.List;

public class FreeItemListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_item_list);
        ListView lv = findViewById(R.id.lvFreeItem);
        List<ContentValues> listFreeItem = ItemFreeModel.getPromoFreeItem(IntentData.custActive.getAsInteger("fin_price_group_id"));
        FreeItemAdapater freeItemAdapater = new FreeItemAdapater(this,R.layout.list_free_item,listFreeItem);
        lv.setAdapter(freeItemAdapater);

    }
}
