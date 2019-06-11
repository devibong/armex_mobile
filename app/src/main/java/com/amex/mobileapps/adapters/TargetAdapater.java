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

public class TargetAdapater extends ArrayAdapter<ContentValues> {
    Activity context;
    int layout;
    List<ContentValues> listTarget;
    List<ContentValues> allListTarget = new ArrayList<ContentValues>();

    public TargetAdapater(Activity context, int layout, List<ContentValues> listTarget) {

        super(context, layout, listTarget);
        this.context=context;
        this.layout=layout;
        this.listTarget =listTarget;
        this.allListTarget.addAll(listTarget);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TargetHolder holder;


        if (convertView == null) {
            LayoutInflater vi=(this.context).getLayoutInflater();
            v = vi.inflate(this.layout, parent,false);

            holder=new TargetHolder();

            //holder.imageView=(ImageView) v.findViewById(R.id.image);
            holder.tvProgramDate = (TextView) v.findViewById(R.id.tv_program_date);
            holder.tv_program_code =(TextView) v.findViewById(R.id.tv_program_code);
            holder.tv_item =(TextView) v.findViewById(R.id.tv_item);
            holder.tv_satuan =(TextView) v.findViewById(R.id.tv_satuan);
            holder.tv_info_achievement =(TextView) v.findViewById(R.id.tv_info_achievement);
            v.setTag(holder);
        }else{
            holder=(TargetHolder) v.getTag();
        }
        ContentValues target = listTarget.get(position);

        String programDate = Utils.formatDate(target.getAsString("fdt_date_start"),"dd MMMM yyyy") + " - " + Utils.formatDate(target.getAsString("fdt_date_end"),"dd MMMM yyyy");
        holder.tvProgramDate.setText(target.getAsString("programDate"));
        holder.tv_program_code.setText(target.getAsString("fst_program_code"));
        holder.tv_item.setText(target.getAsString("fst_item_code"));
        holder.tv_satuan.setText(target.getAsString("fst_satuan"));
        String infoAchievement = "Qty achievement/target : " + target.getAsString("fin_qty_target") + "/" + target.getAsString("fin_current_qty");
        holder.tv_info_achievement.setText(infoAchievement);
        return v;
    }



    static class TargetHolder{
        //ImageView imageView;
        TextView tvProgramDate;
        TextView tv_program_code;
        TextView tv_item;
        TextView tv_satuan;
        TextView tv_info_achievement;

    }
}
