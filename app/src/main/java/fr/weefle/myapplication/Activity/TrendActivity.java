package fr.weefle.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.weefle.myapplication.Model.Transaction;
import fr.weefle.myapplication.R;

public class TrendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_trend);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // Create an object to handle the communication with InfluxDB.
// (best practice tip: reuse the 'influxDB' instance when possible)
        final String serverURL = "http://86.75.114.160:8086", username = "admin", password = "test";
        final InfluxDB influxDB = InfluxDBFactory.connect(serverURL, username, password);

        String databaseName = "database";
        //influxDB.query(new Query("CREATE DATABASE " + databaseName));
        influxDB.setDatabase(databaseName);

        //influxDB.write(Point.measurement("h2o_feet");

        QueryResult queryResult = influxDB.query(new Query("SELECT * FROM data"));

        System.out.println(queryResult);

        influxDB.close();

        final List<QueryResult.Result> transactions = queryResult.getResults();

        //final ArrayList<Transaction> transactions = (ArrayList<Transaction>) bundle.getSerializable("transactions");
        //Collections.reverse(transactions);
        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);
        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        //cartesian.title("Wallet Trend");
        cartesian.yAxis(0);//.title("Transaction Price, $");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
        List<DataEntry> seriesData = new ArrayList<>();
        /*for(Transaction transaction : transactions){
            seriesData.add(new ValueDataEntry(transaction.getName(), transaction.getPrice()));
        }*/
        List<List<Object>> objects = transactions.get(0).getSeries().get(0).getValues();
        for(List<Object> result: objects){
            seriesData.add(new ValueDataEntry(result.get(0).toString(), Float.parseFloat(result.get(1).toString())));
        }
        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Amount($)");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
    }
}
