package fr.weefle.myapplication.Adapter;

;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;


import java.util.ArrayList;

import fr.weefle.myapplication.Model.Transaction;
import fr.weefle.myapplication.R;

public class TransactionAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Transaction> transactions;
    private LayoutInflater inflater;

    public TransactionAdapter(Context context, ArrayList<Transaction> transactions) {
        this.transactions = transactions;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return transactions.size();
    }

    @Override
    public Object getItem(int position) {
        return transactions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.transaction_adapter, null);

        final Transaction currentTrans = (Transaction) getItem(position);
        final String transName = currentTrans.getName();
        final Double transPrice = (double) Math.round(currentTrans.getPrice() * 100) / 100;
        final TextView itemNameView = convertView.findViewById(R.id.trans_name);
        itemNameView.setText(transName);
        final TextView itemPriceView = convertView.findViewById(R.id.trans_price);
        itemPriceView.setText("Amount: " + transPrice.toString() + "$");

        return convertView;
    }

}
