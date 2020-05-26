package fr.weefle.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.weefle.myapplication.Adapter.TransactionAdapter;
import fr.weefle.myapplication.Model.Transaction;
import fr.weefle.myapplication.Model.Wallet;
import fr.weefle.myapplication.R;

public class TransactionActivity extends AppCompatActivity {

    private FloatingActionButton button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        button = findViewById(R.id.floatingActionButton);

        //TODO récupérer infos en intent ou arguments du wallet pour avoir les transactions
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final ArrayList<Transaction> transactions = (ArrayList<Transaction>) bundle.getSerializable("transactions");

        Collections.reverse(transactions);

        ListView shopListView = findViewById(R.id.transaction_list_view);
        shopListView.setAdapter(new TransactionAdapter(this, transactions));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(transactions.size()>1) {

                    Intent intent = new Intent(getApplication(), TrendActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("transactions", transactions);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplication(), "❌ You need at least 2 transactions!", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


}
