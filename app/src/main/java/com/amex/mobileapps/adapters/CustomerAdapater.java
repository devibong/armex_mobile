package com.amex.mobileapps.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.amex.mobileapps.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapater extends ArrayAdapter<ContentValues> {
    Activity context;
    int layout;
    List<ContentValues> listCustomer;
    List<ContentValues> allListCustomer = new ArrayList<ContentValues>();

    public CustomerAdapater(Activity context, int layout, List<ContentValues> listCustomer) {

        super(context, layout, listCustomer);
        this.context=context;
        this.layout=layout;
        this.listCustomer =listCustomer;
        if (listCustomer != null){
            this.allListCustomer.addAll(listCustomer);
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CustomerHolder holder;


        if (convertView == null) {
            LayoutInflater vi=(this.context).getLayoutInflater();
            v = vi.inflate(this.layout, parent,false);

            holder=new CustomerHolder();

            //holder.imageView=(ImageView) v.findViewById(R.id.image);
            holder.nameView=(TextView) v.findViewById(R.id.tv_name);
            holder.addressView=(TextView) v.findViewById(R.id.tv_address);
            //holder.ageView=(TextView) v.findViewById(R.id.age);
            //holder.sexView=(TextView) v.findViewById(R.id.sex);

            v.setTag(holder);
        }else{
            holder=(CustomerHolder) v.getTag();
        }
        ContentValues customer = listCustomer.get(position);
        //holder.imageView.setImageResource(customer.getAsInteger("image"));
        holder.nameView.setText(customer.getAsString("fst_cust_name"));
        holder.addressView.setText(customer.getAsString("fst_cust_address"));
        //holder.ageView.setText(customer.getAsInteger("age").toString());
        //holder.sexView.setText(customer.getAsString("sex"));
        return v;
    }

    public void filter(String searchText){
        searchText = searchText.toLowerCase();
        this.listCustomer.clear();

        if (searchText.length() == 0) {
            this.listCustomer.addAll(this.allListCustomer);
        } else {

            for (ContentValues customer : this.allListCustomer){
                if (customer.getAsString("fst_cust_name").toLowerCase().contains(searchText)){
                    this.listCustomer.add(customer);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class CustomerHolder{
        //ImageView imageView;
        TextView nameView;
        TextView addressView;
    }
}
