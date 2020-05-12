package fr.weefle.myapplication;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import fr.weefle.myapplication.Adapter.TransactionAdapter;

public class TransactionActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        //TODO récupérer infos en intent ou arguments du wallet pour avoir les transactions

        ListView shopListView = findViewById(R.id.transaction_list_view);
        shopListView.setAdapter(new TransactionAdapter(this, WalletFragment.transactions));

    }

}
