package fr.weefle.myapplication.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import fr.weefle.myapplication.R;

public class MapsFragment extends Fragment {

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            final String serverURL = "http://93.3.188.171:8086", username = "admin", password = "test";
            final InfluxDB influxDB = InfluxDBFactory.connect(serverURL, username, password);

            String databaseName = "database";
            //influxDB.query(new Query("CREATE DATABASE " + databaseName));
            influxDB.setDatabase(databaseName);

            //influxDB.write(Point.measurement("h2o_feet");

            QueryResult queryResult = influxDB.query(new Query("SELECT * FROM data"));

            System.out.println(queryResult);

            influxDB.close();
            final List<QueryResult.Result> transactions = queryResult.getResults();
            List<List<Object>> objects = transactions.get(0).getSeries().get(0).getValues();
            for(List<Object> result: objects) {
                String json = result.get(1).toString();
                double lon, lat, temp = 0;
                try {
                    JSONObject obj = new JSONObject(json);
                    JSONObject loc = obj.getJSONObject("location");
                    lon = loc.getDouble("lon");
                    lat = loc.getDouble("lat");
                    JSONObject value = obj.getJSONObject("value");
                    temp = value.getDouble("temperature");
                    //System.out.println(lon + " : " + lat);
                    LatLng location = new LatLng(lat, lon);
                    googleMap.addMarker(new MarkerOptions().position(location).title(result.get(0).toString()));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

   @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}