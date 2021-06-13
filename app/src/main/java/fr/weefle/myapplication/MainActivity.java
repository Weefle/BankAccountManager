package fr.weefle.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.weefle.myapplication.Activity.HomeActivity;
import fr.weefle.myapplication.Activity.RegisterActivity;
import fr.weefle.myapplication.Activity.TrendActivity;
import fr.weefle.myapplication.Model.Data;

public class MainActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    TextView textViewRegister, textViewReset;
    public static ArrayList<Data> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Create an object to handle the communication with InfluxDB.
            final String serverURL = "http://178.32.129.132:8086", username = "admin", password = "test1234";
            final InfluxDB influxDB = InfluxDBFactory.connect(serverURL, username, password);

            String databaseName = "database";

            influxDB.setDatabase(databaseName);

            QueryResult queryResult = influxDB.query(new Query("SELECT * FROM data"));

            System.out.println(queryResult);

            influxDB.close();

            final List<QueryResult.Result> results = queryResult.getResults();


            List<List<Object>> objects = results.get(0).getSeries().get(0).getValues();
            for(List<Object> result: objects) {
                String json = result.get(1).toString();
                double temp = 0, lat = 0, lon = 0;
                try {
                    JSONObject obj = new JSONObject(json);
                    JSONObject value = obj.getJSONObject("value");
                    temp = value.getDouble("temperature");
                    JSONObject loc = obj.getJSONObject("location");
                    lon = loc.getDouble("lon");
                    lat = loc.getDouble("lat");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String[] split = result.get(0).toString().split("T");
                String date = split[0];
                String time = split[1].substring(0, 8);
                datas.add(new Data(date, time, temp, lat, lon));
            }
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        textViewRegister = findViewById(R.id.textViewRegister);
        textViewReset = findViewById(R.id.textViewReset);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                startActivity(new Intent(MainActivity.this, TrendActivity.class));
                finish();
            }
        });

        textViewReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextEmail.getText().toString().contains("@")) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(editTextEmail.getText().toString());
                    Toast.makeText(MainActivity.this, "✔ A verification email has been sent to your email address!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "❌ Please enter a correct email address!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {

                    Toast.makeText(MainActivity.this, "❌ You missed some fields!", Toast.LENGTH_SHORT).show();
                } else{

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "✔ Successfully logged in!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "❌ Wrong password or email!", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
            }

            }
        });

    }
}
