package fr.weefle.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

import fr.weefle.myapplication.Adapter.TransactionAdapter;
import fr.weefle.myapplication.Model.Transaction;
import fr.weefle.myapplication.Model.Wallet;

public class TransactionActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        //TODO récupérer infos en intent ou arguments du wallet pour avoir les transactions
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ArrayList<Transaction> transactions = (ArrayList<Transaction>) bundle.getSerializable("transactions");



        ListView shopListView = findViewById(R.id.transaction_list_view);
        shopListView.setAdapter(new TransactionAdapter(this, transactions));

    }

}
