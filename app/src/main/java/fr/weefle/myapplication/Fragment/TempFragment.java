package fr.weefle.myapplication.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.weefle.myapplication.R;

public class TempFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_trend, container, false);
        AnyChartView anyChartView = v.findViewById(R.id.any_chart_view);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /*URL url;
        try {
            url = new URL("https://node-red-3-nodered-enabler-prod.eu-b.kmt.orange.com/node-red/api/60a64e580d3494e8489009b7/button");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Basic bm9kZS1yZWQtYXBpOmNyUVIxTlZ2TGJpdUtXN1BzSnR2dHloYlpYVk1rb0dY");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setInstanceFollowRedirects(true);
        String hello = "{\"username\":\"root\",\"password\":\"password\"}";
            byte[] out = hello.getBytes(StandardCharsets.UTF_8);
            con.connect();
            try(OutputStream os = con.getOutputStream()) {
                os.write(out);
            }
            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM) {
                String location = con.getHeaderField("Location");
                URL newUrl = new URL(location);
                con = (HttpURLConnection) newUrl.openConnection();
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        // Create an object to handle the communication with InfluxDB.
        final String serverURL = "http://178.32.129.132:8086", username = "admin", password = "test1234";
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
            String json = result.get(1).toString();
            double lon = 0, lat = 0, temp = 0;
            try {
                JSONObject obj = new JSONObject(json);
                JSONObject loc = obj.getJSONObject("location");
                lon = loc.getDouble("lon");
                JSONObject value = obj.getJSONObject("value");
                temp = value.getDouble("temperature");
                //System.out.println(lon + " : " + lat);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            seriesData.add(new ValueDataEntry(result.get(0).toString(), temp));
        }
        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Température(°C)");
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
        return v;
    }

}