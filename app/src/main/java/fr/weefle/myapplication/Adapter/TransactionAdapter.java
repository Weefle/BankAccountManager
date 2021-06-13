package fr.weefle.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

import fr.weefle.myapplication.Activity.HomeActivity;
import fr.weefle.myapplication.Activity.TransactionActivity;
import fr.weefle.myapplication.Fragment.MapsFragment;
import fr.weefle.myapplication.Model.Data;
import fr.weefle.myapplication.R;

public class TransactionAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Data> datas;
    private LayoutInflater inflater;

    public TransactionAdapter(Context context, ArrayList<Data> datas) {
        this.datas = datas;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.transaction_adapter, null);

        final Data currentTrans = (Data) getItem(position);
        final String transName = "Date: " + currentTrans.getDate() +" Time: " + currentTrans.getTime();
        final double transPrice = currentTrans.getTemp();
        //"Lat: " + currentTrans.getLat() +" Lon: " + currentTrans.getLon()
        final TextView itemNameView = convertView.findViewById(R.id.trans_name);
        itemNameView.setText(transName);
        final TextView itemPriceView = convertView.findViewById(R.id.trans_price);
        itemPriceView.setText("Température: " + transPrice + "°C");

        View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                ((HomeActivity) finalConvertView.getContext()).showFragment(new MapsFragment(currentTrans));


            }
        });

        return convertView;
    }

}
