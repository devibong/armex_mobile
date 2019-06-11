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

public class FreeItemAdapater extends ArrayAdapter<ContentValues> {
    Activity context;
    int layout;
    List<ContentValues> listFreeItem;
    List<ContentValues> allListFreeItem = new ArrayList<ContentValues>();

    public FreeItemAdapater(Activity context, int layout, List<ContentValues> listFreeItem) {

        super(context, layout, listFreeItem);
        this.context=context;
        this.layout=layout;
        this.listFreeItem =listFreeItem;
        this.allListFreeItem.addAll(listFreeItem);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        FreeItemHolder holder;


        if (convertView == null) {
            LayoutInflater vi=(this.context).getLayoutInflater();
            v = vi.inflate(this.layout, parent,false);

            holder = new FreeItemHolder();
            //holder.imageView=(ImageView) v.findViewById(R.id.image);
            //holder.nameView=(TextView) v.findViewById(R.id.tv_promodate);
            //holder.addressView=(TextView) v.findViewById(R.id.tv_info_promo);
            //holder.ageView=(TextView) v.findViewById(R.id.age);
            //holder.sexView=(TextView) v.findViewById(R.id.sex);
            holder.tvPromoDate = (TextView) v.findViewById(R.id.tv_promodate);
            holder.tvItemAwal = (TextView) v.findViewById(R.id.tv_item_awal);
            holder.tvItemAkhir = (TextView) v.findViewById(R.id.tv_item_akhir);
            holder.tvInfoPromoItem = (TextView) v.findViewById(R.id.tv_info_promo_item);
            holder.tvItemFreeAwal = (TextView) v.findViewById(R.id.tv_item_free_awal);
            holder.tvItemFreeAkhir = (TextView) v.findViewById(R.id.tv_item_free_akhir);
            holder.tvInfoFreeItem = (TextView) v.findViewById(R.id.tv_info_free_item);
            holder.tvInfoPromo = (TextView) v.findViewById(R.id.tv_info_promo);

            v.setTag(holder);
        }else{
            holder=(FreeItemHolder) v.getTag();
        }

        ContentValues freeItem = listFreeItem.get(position);
        String promoDate = Utils.formatDate(freeItem.getAsString("fdt_date_start") , "dd MMMM yyyy") + " - " + Utils.formatDate(freeItem.getAsString("fdt_date_end") , "dd MMMM yyyy");
        holder.tvPromoDate.setText(promoDate);

        holder.tvItemAwal.setText(freeItem.getAsString("fst_item_code_awal"));
        holder.tvItemAkhir.setText(freeItem.getAsString("fst_item_code_akhir"));
        String infoPromoItem = "Satuan : " + freeItem.getAsString("fst_satuan") + " - Qty :" + freeItem.getAsString("fin_qty");
        holder.tvInfoPromoItem.setText(infoPromoItem);

        holder.tvItemFreeAwal.setText(freeItem.getAsString("fst_free_item_code_awal"));
        holder.tvItemFreeAkhir.setText(freeItem.getAsString("fst_free_item_code_akhir"));
        String infoFreeItem = "Satuan : " + freeItem.getAsString("fst_free_satuan") + " - Qty :" + freeItem.getAsString("fin_free_qty");
        holder.tvInfoFreeItem.setText(infoFreeItem);

        String infoPromo = "";
        if (freeItem.getAsInteger("fbl_multiple_free") == 1){
            infoPromo = "Multiple";
        }else{
            infoPromo = "Not Multiple";
        }
        infoPromo += " - Qty max :" + freeItem.getAsString("fin_max_free_qty")  ;
        holder.tvInfoPromo.setText(infoPromo);
        return v;
    }

    public void filter(String searchText){
        searchText = searchText.toLowerCase();
        this.listFreeItem.clear();

        if (searchText.length() == 0) {
            this.listFreeItem.addAll(this.allListFreeItem);
        } else {

            for (ContentValues freeItem : this.allListFreeItem){
                if (freeItem.getAsString("fst_cust_name").toLowerCase().contains(searchText)){
                    this.listFreeItem.add(freeItem);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class FreeItemHolder{
        //ImageView imageView;
        TextView tvPromoDate;
        TextView tvItemAwal;
        TextView tvItemAkhir;
        TextView tvInfoPromoItem;
        TextView tvItemFreeAwal;
        TextView tvItemFreeAkhir;
        TextView tvInfoFreeItem;
        TextView tvInfoPromo;
    }
}
