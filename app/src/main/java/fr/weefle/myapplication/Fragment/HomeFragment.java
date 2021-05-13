package fr.weefle.myapplication.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import fr.weefle.myapplication.MainActivity;
import fr.weefle.myapplication.Model.CurrentUser;
import fr.weefle.myapplication.R;

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

        textViewName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        textViewEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentUser.setCurrentUser(null);
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

                if (!editTextPasswordPopup.getText().toString().isEmpty() && !editTextConfPasswordPopup.getText().toString().isEmpty()) {

                if (editTextConfPasswordPopup.getText().toString().equals(editTextPasswordPopup.getText().toString()) ) {
                    FirebaseAuth.getInstance().getCurrentUser().updatePassword(editTextConfPasswordPopup.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getActivity(), "✔ Password correctly changed!", Toast.LENGTH_SHORT).show();
                                /*CurrentUser.setCurrentUser(null);
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();*/
                            }else{
                                Toast.makeText(getActivity(), "❌ Couldn't change password, you need at least 6 digits!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else{
                    Toast.makeText(getActivity(), "❌ Password is not the same!", Toast.LENGTH_SHORT).show();
                }

                } else {
                    Toast.makeText(getActivity(), "❌ Missing fields!", Toast.LENGTH_SHORT).show();
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
        final EditText editTextName = view.findViewById(R.id.editTextName);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        editTextEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        editTextName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        Button saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editTextEmail.getText().toString().isEmpty() && !editTextName.getText().toString().isEmpty()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(editTextName.getText().toString())
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "✔ Name correctly changed!", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().getCurrentUser().updateEmail(editTextEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getActivity(), "✔ Email correctly changed!", Toast.LENGTH_SHORT).show();
                                                    getFragmentManager()
                                                            .beginTransaction()
                                                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right)
                                                            .replace(R.id.fragment_container, new HomeFragment())
                                                            .commitNow();

                                /*CurrentUser.setCurrentUser(null);
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();*/
                                                }else{
                                                    Toast.makeText(getActivity(), "❌ Couldn't change email!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else{
                                        Toast.makeText(getActivity(), "❌ Couldn't change name!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });



                } else {
                    Toast.makeText(getActivity(), "❌ Missing fields!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        });
    }

}
