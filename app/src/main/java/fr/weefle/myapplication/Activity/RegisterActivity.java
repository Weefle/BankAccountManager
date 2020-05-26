package fr.weefle.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import fr.weefle.myapplication.MainActivity;
import fr.weefle.myapplication.R;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextUsername, editTextEmail, editTextPassword, editTextCnfPassword;
    Button buttonRegister;

    TextView textViewLogin;
    //String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextCnfPassword = findViewById(R.id.editTextCnfPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        textViewLogin = findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim();
                String passwordConf = editTextCnfPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || userName.isEmpty() || password.isEmpty() || passwordConf.isEmpty()) {

                    Toast.makeText(RegisterActivity.this, "❌ You missed some fields!", Toast.LENGTH_SHORT).show();
                } else {

                    if (password.equals(passwordConf)) {

                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                               /* userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DocumentReference ref = FirebaseFirestore.getInstance().collection("Users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("userName", userName);
                                user.put("userPassword", password);
                                ref.set(user);*/
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(userName)
                                            .build();

                                    user.updateProfile(profileUpdates);
                                    Toast.makeText(RegisterActivity.this, "✔ Successfully registered!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "❌ Account already exist or you need at least 6 digits!", Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                    } else {
                        Toast.makeText(RegisterActivity.this, "❌ Password is not matching!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
}
