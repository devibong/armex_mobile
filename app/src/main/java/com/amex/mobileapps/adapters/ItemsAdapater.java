package com.amex.mobileapps.adapters;

import android.app.Activity;
import android.content.ContentValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.amex.mobileapps.R;
import com.qsystem.android.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapater extends ArrayAdapter<ContentValues> {
    Activity context;
    int layout;
    List<ContentValues> listItem;
    List<ContentValues> allListItem = new ArrayList<ContentValues>();

    public ItemsAdapater(Activity context, int layout, List<ContentValues> listItem) {

        super(context, layout, listItem);
        this.context=context;
        this.layout=layout;
        this.listItem =listItem;
        this.allListItem.addAll(listItem);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ItemHolder holder;


        if (convertView == null) {
            LayoutInflater vi=(this.context).getLayoutInflater();
            v = vi.inflate(this.layout, parent,false);
            holder=new ItemHolder();
            holder.itemCodeName = (TextView) v.findViewById(R.id.tv_item);
            holder.itemSatuan1 =(TextView) v.findViewById(R.id.tvsatuan1);
            holder.itemSatuan2 =(TextView) v.findViewById(R.id.tvsatuan2);
            holder.itemSatuan3 =(TextView) v.findViewById(R.id.tvsatuan3);
            holder.itemPrice1 =(TextView) v.findViewById(R.id.tvprice1);
            holder.itemPrice2 =(TextView) v.findViewById(R.id.tvprice2);
            holder.itemPrice3 =(TextView) v.findViewById(R.id.tvprice3);
            holder.itemMemo =(TextView) v.findViewById(R.id.tv_memo);
            v.setTag(holder);
        }else{
            holder=(ItemHolder) v.getTag();
        }

        ContentValues item = listItem.get(position);
        holder.itemCodeName.setText(item.getAsString("fst_item_code") + " - " + item.getAsString("fst_item_name"));
        holder.itemSatuan1.setText(item.getAsString("fst_satuan_1"));
        holder.itemSatuan2.setText(item.getAsString("fst_satuan_2"));
        holder.itemSatuan3.setText(item.getAsString("fst_satuan_3"));

        //Locale localeID = new Locale("in", "ID");
        //NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        //NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(Locale.US);
        //detailHarga.setText(formatRupiah.format((double)hargarumah));
        //holder.itemPrice1.setText(formatRupiah.format(item.getAsDouble("fin_selling_price1")));
        //holder.itemPrice2.setText(formatRupiah.format(item.getAsDouble("fin_selling_price2")));
        //holder.itemPrice3.setText(formatRupiah.format(item.getAsDouble("fin_selling_price3")));

        holder.itemPrice1.setText(Utils.formatNumber(item.getAsDouble("fin_selling_price1")));
        holder.itemPrice2.setText(Utils.formatNumber(item.getAsDouble("fin_selling_price2")));
        holder.itemPrice3.setText(Utils.formatNumber(item.getAsDouble("fin_selling_price3")));
        holder.itemMemo.setText(item.getAsString("fst_memo"));
        return v;
    }

    public void filter(String searchText){
        searchText = searchText.toLowerCase();
        this.listItem.clear();

        if (searchText.length() == 0) {
            this.listItem.addAll(this.allListItem);
        } else {

            for (ContentValues item : this.allListItem){
                if (item.getAsString("fst_item_name").toLowerCase().contains(searchText)){
                    this.listItem.add(item);
                }else if (item.getAsString("fst_item_code").toLowerCase().contains(searchText)){
                    this.listItem.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ItemHolder{
        //ImageView imageView;
        TextView itemCodeName;
        TextView itemSatuan1;
        TextView itemSatuan2;
        TextView itemSatuan3;
        TextView itemPrice1;
        TextView itemPrice2;
        TextView itemPrice3;
        TextView itemMemo;

    }
}
