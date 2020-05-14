package fr.weefle.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.weefle.myapplication.Adapter.WalletAdapter;
import fr.weefle.myapplication.Model.Transaction;
import fr.weefle.myapplication.Model.User;
import fr.weefle.myapplication.Model.Wallet;


public class WalletFragment extends Fragment {

    //TODO the arraylist wil come from the arraylist from user account
    //private ArrayList<Wallet> wallets;
    //public static ArrayList<Transaction> transactions;

   // private Wallet wallet;
    //private Transaction transaction;
    private Button addWallet;
    private EditText editWallet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_wallet, container, false);

        addWallet = rootView.findViewById(R.id.add_wallet);
        editWallet = rootView.findViewById(R.id.edit_wallet);

        addWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String walletName = editWallet.getText().toString();
                Wallet wallet = new Wallet(walletName, 0.0);
                Transaction transaction = new Transaction("test", 0.0);
                wallet.addTransaction(transaction);
                User user = new User(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                user.addWallet(wallet);
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DocumentReference ref = FirebaseFirestore.getInstance().collection("Users").document(userID);
                                ref.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){
                                            /*startActivity(new Intent(getActivity(), HomeActivity.class));
                                            getActivity().finish();*/
                                            Toast.makeText(getContext(), "✔ Successfully added!", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

            }
        });

        //TODO make the wallet list


         String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference ref = FirebaseFirestore.getInstance().collection("Users").document(userID);
        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(documentSnapshot.toObject(User.class) != null) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user.getWallets() != null) {
                        ArrayList<Wallet> wallets = user.getWallets();
                        //Toast.makeText(getContext(), user.getWallets().iterator().next().getTransactions().iterator().next().getName(), Toast.LENGTH_SHORT).show();
                        ListView shopListView = rootView.findViewById(R.id.wallet_list_view);
                        shopListView.setAdapter(new WalletAdapter(getActivity(), wallets));
                    }
                }

            }
        });

        /*if(wallets == null) {
            wallets = new ArrayList<>();
            transactions = new ArrayList<>();
            transaction = new Transaction("Bureau Tabac", 20.0);
            transactions.add(transaction);
            wallet = new Wallet("Livret A", 200.0);
            wallet.setTransactions(transactions);
            wallets.add(wallet);
        }*/

        //TODO use this to reload fragment when user add a wallet (button execution)
        //getFragmentManager().beginTransaction().detach(this).attach(this).commit();



        return rootView;
    }
}
