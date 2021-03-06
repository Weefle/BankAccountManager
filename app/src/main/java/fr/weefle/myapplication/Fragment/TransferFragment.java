package fr.weefle.myapplication.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.weefle.myapplication.Model.CurrentUser;
import fr.weefle.myapplication.Model.Transaction;
import fr.weefle.myapplication.Model.Wallet;
import fr.weefle.myapplication.R;

public class TransferFragment extends Fragment {

    Spinner spinner;
    EditText transaction_price;
    EditText transaction_name;
    Button button;
    boolean check;
    Map<String, Wallet> wallets;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_transfer, container, false);

        wallets = new HashMap<>();

        spinner = rootView.findViewById(R.id.spinner);
        transaction_price = rootView.findViewById(R.id.edit_transaction_price);
        button = rootView.findViewById(R.id.add_balance);
        transaction_name = rootView.findViewById(R.id.edit_transaction_name);

        List<String> list = new ArrayList<String>();

        if (CurrentUser.getCurrentUser().getWallets() != null){
            for (Wallet wallet : CurrentUser.getCurrentUser().getWallets()) {

                list.add(wallet.getName());
                wallets.put(wallet.getName(), wallet);

            }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

}

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItem() != null && !transaction_price.getText().toString().isEmpty() && !transaction_name.getText().toString().isEmpty()){

                    check = false;

                    for(Transaction transaction : wallets.get(spinner.getSelectedItem().toString()).getTransactions()){
                        if(transaction.getName().equals(transaction_name.getText().toString().trim())){

                            check = true;
                        }
                    }

                    if(!check) {
                        Transaction transaction = new Transaction(transaction_name.getText().toString(), Double.parseDouble(transaction_price.getText().toString()));
                        Wallet wallet = wallets.get(spinner.getSelectedItem().toString());
                        wallet.addTransaction(transaction);
                        wallets.put(spinner.getSelectedItem().toString(), wallet);
                        ArrayList<Wallet> newWallets = new ArrayList<>();
                        for (Wallet newWallet : wallets.values()){
                            newWallets.add(newWallet);
                        }
                        CurrentUser.getCurrentUser().setWallets(newWallets);
                        double price =  Double.parseDouble(transaction_price.getText().toString());
                        CurrentUser.getCurrentUser().getWallet(spinner.getSelectedItem().toString()).setBalance(CurrentUser.getCurrentUser().getWallet(spinner.getSelectedItem().toString()).getBalance() + price);
                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DocumentReference ref = FirebaseFirestore.getInstance().collection("Users").document(userID);
                        ref.set(CurrentUser.getCurrentUser()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "✔ Successfully added!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getActivity(), "Already exists!", Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(getContext(), "❌ They are empty fields!", Toast.LENGTH_SHORT).show();
                }

            }
        });




        return rootView;
    }

}
