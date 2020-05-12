package fr.weefle.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import fr.weefle.myapplication.Adapter.WalletAdapter;
import fr.weefle.myapplication.Model.Transaction;
import fr.weefle.myapplication.Model.Wallet;


public class WalletFragment extends Fragment {

    //TODO the arraylist wil come from the arraylist from user account
    private ArrayList<Wallet> wallets;
    public static ArrayList<Transaction> transactions;

    private Wallet wallet;
    private Transaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_wallet, container, false);
        //TODO make the wallet list

        if(wallets == null) {
            wallets = new ArrayList<>();
            transactions = new ArrayList<>();
            transaction = new Transaction("Bureau Tabac", 20.0);
            transactions.add(transaction);
            wallet = new Wallet("Livret A", 200.0);
            wallet.setTransactions(transactions);
            wallets.add(wallet);
        }

        //TODO use this to reload fragment when user add a wallet (button execution)
        //getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        ListView shopListView = rootView.findViewById(R.id.wallet_list_view);
        shopListView.setAdapter(new WalletAdapter(getActivity(), wallets));

        return rootView;
    }
}
