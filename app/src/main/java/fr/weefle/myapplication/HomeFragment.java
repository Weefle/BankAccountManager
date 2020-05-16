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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class HomeFragment extends Fragment {

    TextView textViewName, textViewEmail, textViewLogout;

    Button btnEditDetails, btnChangePassword;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;
    //String userID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        textViewName = rootView.findViewById(R.id.textViewName);
        textViewEmail = rootView.findViewById(R.id.textViewEmail);
        textViewLogout = rootView.findViewById(R.id.textViewLogout);

       /* userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference ref = FirebaseFirestore.getInstance().collection("Users").document(userID);
        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                textViewName.setText(documentSnapshot.getString("userName"));

            }
        });*/
        textViewName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        textViewEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WalletFragment.user = null;
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
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

        Button saveButtonPassword = view.findViewById(R.id.saveButtonPassword);

        saveButtonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("check", editTextPasswordPopup.getText().toString());
                Log.d("check", editTextConfPasswordPopup.getText().toString());

                if (editTextConfPasswordPopup.getText().toString().equals(editTextPasswordPopup.getText().toString()) ) {
                    FirebaseAuth.getInstance().getCurrentUser().updatePassword(editTextConfPasswordPopup.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                WalletFragment.user = null;
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(getActivity(), "✔ Password correctly changed!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            }else{
                                Toast.makeText(getActivity(), "❌ Couldn't change password, you need at least 6 digits!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else{
                    Toast.makeText(getActivity(), "❌ Password is not the same!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });
    }


    public void editUserDetails() {
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        inflater = LayoutInflater.from(getActivity());
        final View view = inflater.inflate(R.layout.popup, null);
        final EditText editTextEmail = view.findViewById(R.id.editTextEmail);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        editTextEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        Button saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editTextEmail.getText().toString().isEmpty()) {
                    FirebaseAuth.getInstance().getCurrentUser().updateEmail(editTextEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                WalletFragment.user = null;
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(getActivity(), "✔ Details correctly changed!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            }else{
                                Toast.makeText(getActivity(), "❌ Couldn't change details!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "❌ Missing details!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });
    }

}
