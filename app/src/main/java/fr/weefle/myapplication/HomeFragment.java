package fr.weefle.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import fr.weefle.myapplication.Data.DatabaseHelper;
import fr.weefle.myapplication.Model.User;

public class HomeFragment extends Fragment {

    TextView textViewUserName, textViewName, textViewPassword, textViewEmail, textViewLogout;

    Button btnEditDetails, btnChangePassword;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    String textViewUsernameString;
    String textViewNameString;
    String textViewEmailString;
    String textViewPasswordString;
    int textViewID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        textViewUserName = rootView.findViewById(R.id.textViewUserName);
        textViewName = rootView.findViewById(R.id.textViewName);
        textViewPassword = rootView.findViewById(R.id.textViewPassword);
        textViewEmail = rootView.findViewById(R.id.textViewEmail);
        textViewLogout = rootView.findViewById(R.id.textViewLogout);

        final Bundle b = getActivity().getIntent().getExtras();


        textViewID = Integer.parseInt(b.getString("textViewId"));
        textViewUsernameString = b.getString("textViewUsername");
        textViewNameString = b.getString("textViewUsername");
        textViewEmailString = b.getString("textViewEmail");
        textViewPasswordString = b.getString("textViewPassword");


        Log.d("Data", String.valueOf(textViewID));
        Log.d("Data", "User name: " + textViewUsernameString);
        Log.d("Data", "name: " + textViewNameString);
        Log.d("Data", "Email: " + textViewEmailString);
        Log.d("Data", "password: " + textViewPasswordString);

        textViewUserName.setText("Hello " + textViewUsernameString);
        textViewName.setText(textViewNameString);
        textViewPassword.setText(textViewPasswordString);
        textViewEmail.setText(textViewEmailString);

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        btnEditDetails = rootView.findViewById(R.id.btnEditDetails);
        btnEditDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserDetails();
            }
        });

        btnChangePassword = rootView.findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        return rootView;
    }

    public void changePassword() {
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.popup_password, null);
        final EditText editTextPasswordPopup = view.findViewById(R.id.editTextPasswordPopup);
        final EditText editTextConfPasswordPopup = view.findViewById(R.id.editTextConfPasswordPopup);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        final User user = new User();
        user.setId(textViewID);
        user.setUserName(textViewNameString);
        user.setEmail(textViewEmailString);
        user.setPassword(textViewPasswordString);


        final DatabaseHelper db = new DatabaseHelper(getActivity());

        Button saveButtonPassword = view.findViewById(R.id.saveButtonPassword);

        saveButtonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("check", editTextPasswordPopup.getText().toString());
                Log.d("check", editTextConfPasswordPopup.getText().toString());

                if (editTextConfPasswordPopup.getText().toString().equals(editTextPasswordPopup.getText().toString()) ) {
                    user.setPassword(editTextConfPasswordPopup.getText().toString());
                    db.updateUser(user);
                    Toast.makeText(getActivity(), "✔ Password correctly changed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else{
                    Toast.makeText(getActivity(), "❌ Password is not the same", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });
    }


    public void editUserDetails() {
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.popup, null);
        final EditText editTextUsername = view.findViewById(R.id.editTextUsername);
        final EditText editTextEmail = view.findViewById(R.id.editTextEmail);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        final User user = new User();
        user.setId(textViewID);
        user.setUserName(textViewNameString);
        user.setEmail(textViewEmailString);
        user.setPassword(textViewPasswordString);

        editTextUsername.setText(user.getUserName());
        editTextEmail.setText(user.getEmail());

        final DatabaseHelper db = new DatabaseHelper(getActivity());

        Button saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUserName(editTextUsername.getText().toString());
                user.setEmail(editTextEmail.getText().toString());

                if (!editTextUsername.getText().toString().isEmpty()
                        && !editTextEmail.getText().toString().isEmpty()) {
                    db.updateUser(user);
                    Toast.makeText(getActivity(), "✔ Details correctly changed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
                    Toast.makeText(getActivity(), "❌ Missing details", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });
    }

}
