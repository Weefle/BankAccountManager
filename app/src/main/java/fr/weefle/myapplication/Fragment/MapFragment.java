package fr.weefle.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.weefle.myapplication.MainActivity;
import fr.weefle.myapplication.Model.Data;
import fr.weefle.myapplication.R;

public class MapFragment extends Fragment {

    private FloatingActionButton button;

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


            for(Data data : MainActivity.datas) {
                LatLng location = new LatLng(data.getLat(), data.getLon());
                googleMap.addMarker(new MarkerOptions().position(location).title("Date: " + data.getDate() + " Time: " + data.getTime()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));


            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        button = v.findViewById(R.id.floatingActionButton);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                URL url;
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
                }

            }
        });
        return v;
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