package com.amex.mobileapps;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import com.amex.mobileapps.adapters.CustomerAdapater;
import com.amex.mobileapps.adapters.ItemsAdapater;
import com.amex.mobileapps.model.CustomersModel;
import com.amex.mobileapps.model.IntentData;
import com.amex.mobileapps.model.ItemsModel;

import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        ListView listView = findViewById(R.id.lvItemList);
        SearchView searchView = findViewById(R.id.searchView);

        final List<ContentValues> listItems = ItemsModel.getItems(IntentData.custActive.getAsInteger("fin_price_group_id"));

        ItemsAdapater adapater = new ItemsAdapater(this,R.layout.list_items_item,listItems);
        listView.setAdapter(adapater);

        searchView.setOnQueryTextListener(getSearchListener(adapater));

    }

    private SearchView.OnQueryTextListener getSearchListener(final ItemsAdapater adapater){
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
}
