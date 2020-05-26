package fr.weefle.myapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import fr.weefle.myapplication.Model.Wallet;
import fr.weefle.myapplication.R;
import fr.weefle.myapplication.Activity.TransactionActivity;
import fr.weefle.myapplication.Fragment.WalletFragment;

public class WalletAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Wallet> wallets;
    private LayoutInflater inflater;
    private Boolean check;

    public WalletAdapter(Context context, ArrayList<Wallet> wallets) {
        this.wallets = wallets;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;


    @Override
    public int getCount() {
        return wallets.size();
    }

    @Override
    public Object getItem(int position) {
        return wallets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.wallet_adapter, null);

        final Wallet currentWallet = (Wallet) getItem(position);
        final String walletName = currentWallet.getName();
        final Double walletBalance =  (double) Math.round(currentWallet.getBalance() * 100) / 100;
        final TextView itemNameView = convertView.findViewById(R.id.wallet_name);
        itemNameView.setText(walletName);
        final TextView itemPriceView = convertView.findViewById(R.id.wallet_balance);
        itemPriceView.setText("Balance: " + walletBalance.toString() + "$");
        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //TODO open a new activity
                if (!currentWallet.getTransactions().isEmpty()) {
                    Intent intent = new Intent(context, TransactionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("transactions", currentWallet.getTransactions());
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            else{
                    Toast.makeText(context, "❌ There isn't any transaction yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
                                               public boolean onLongClick(View v) {

                                                   editWalletDetails(walletName, walletBalance, position);
                                                   return true;
                                               }
                                           });

        return convertView;
    }


    public void editWalletDetails(final String name, Double balance, final int position) {
        alertDialogBuilder = new AlertDialog.Builder(context);
        inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.popup_wallet, null);
        final EditText editTextname = view.findViewById(R.id.editTextname);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        editTextname.setText(name);



        final Button saveButtonWallet = view.findViewById(R.id.saveButtonWallet);
        Button deleteButtonWallet = view.findViewById(R.id.deleteButtonWallet);

        deleteButtonWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    WalletFragment.user.getWallets().remove(position);
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentReference ref = FirebaseFirestore.getInstance().collection("Users").document(userID);
                    ref.set(WalletFragment.user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(context, "✔ Successfully deleted!", Toast.LENGTH_SHORT).show();
                            }else{
                            Toast.makeText(context, "❌ Error with database!", Toast.LENGTH_SHORT).show();
                        }

                        }
                    });

                dialog.dismiss();

           }
        });

        saveButtonWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editTextname.getText().toString().isEmpty()) {
                    //db.updateWallet(wallet);
                    WalletFragment.user.getWallets().get(position).setName(editTextname.getText().toString());
                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentReference ref = FirebaseFirestore.getInstance().collection("Users").document(userID);
                    ref.set(WalletFragment.user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(context, "✔ Successfully changed!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "❌ Error with database!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                } else {
                    Toast.makeText(context, "❌ Missing details!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });
    }
}
