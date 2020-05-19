package fr.weefle.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import fr.weefle.myapplication.Model.Wallet;

public class TransferFragment extends Fragment {

    Spinner spinner;
    EditText balance;
    Button button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_transfer, container, false);

        spinner = rootView.findViewById(R.id.spinner);
        balance = rootView.findViewById(R.id.edit_balance);
        button = rootView.findViewById(R.id.add_balance);

        List<String> list = new ArrayList<String>();

        if (WalletFragment.user.getWallets() != null){
            for (Wallet wallet : WalletFragment.user.getWallets()) {

                list.add(wallet.getName());

            }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

}

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spinner.getSelectedItem() != null && !balance.getText().toString().isEmpty()){

                    Toast.makeText(getContext(), "✔ You have added " + balance.getText().toString() +" € to wallet: " + spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getContext(), "❌ They are empty fields!", Toast.LENGTH_SHORT).show();
                }

            }
        });




        return rootView;
    }

}
