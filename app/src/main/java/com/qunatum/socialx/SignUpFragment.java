package com.qunatum.socialx;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class SignUpFragment extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private TextView Country_Code;
    private EditText user_name;
    private EditText user_password;
    private EditText user_phn;
    private EditText user_email;
    private ProgressDialog dialog;

    private CheckBox checkBox;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mAuth = FirebaseAuth.getInstance();




        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        checkBox = root.findViewById(R.id.checkBox);
        user_name = root.findViewById(R.id.editNameSignUp);
        user_password = root.findViewById(R.id.editPassSignUp);
        user_phn = root.findViewById(R.id.editNumberSignUp);
        user_email = root.findViewById(R.id.editEmailSignUp);

        TextView signIn = root.findViewById(R.id.signInText);
        signIn.setOnClickListener(view -> ((MainActivity) requireActivity()).selectPage(0));


        Button register = root.findViewById(R.id.signUpBtn);
        register.setOnClickListener(view -> RegisterCurrentUser());
        Country_Code = root.findViewById(R.id.textView8);
        Country_Code.setOnClickListener(view -> {

            PopupMenu popup = new PopupMenu(requireContext(), view);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(item -> {

                switch (item.getTitle().toString())
                {
                    case "+91":
                    {
                        Country_Code.setText(" IN +91 ");
                        Country_Code.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_india, 0, R.drawable.ic_drop, 0);
                        break;
                    }

                    case "+92":
                    {
                        Country_Code.setText(" PA +91 ");
                        Country_Code.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pakistan, 0, R.drawable.ic_drop, 0);

                        break;
                    }
                }
                return true;
            });
            popup.show();
        });


        return root ;
    }

    private void RegisterCurrentUser() {

        String name = user_name.getText().toString().trim();
        String email = user_email.getText().toString().trim();
        String password = user_password.getText().toString();
        String number = user_phn.getText().toString().trim();

        if(TextUtils.isEmpty(name))
        {
            user_name.setError("Name cannot be empty");
            user_name.requestFocus();
        }
        else if(TextUtils.isEmpty(email))
        {
            user_email.setError("Email cannot be empty");
            user_email.requestFocus();
        }
        else if(TextUtils.isEmpty(password) || password.length()<8)
        {
            user_password.setError("Weak Password");
            user_password.requestFocus();
        }
        else if(TextUtils.isEmpty(number))
        {
            user_phn.setError("Phone number cannot be empty");
            user_phn.requestFocus();
        }
        else if((!TextUtils.isDigitsOnly(number)) || number.length() != 10)
        {
            user_phn.setError("Invalid phone number");
            user_phn.requestFocus();
        }
        else if(!email.contains("@"))
        {
            user_email.setError("Invalid Email");
            user_email.requestFocus();
        }
        else if(!checkBox.isChecked())
        {
            checkBox.setError("Please accept the terms and conditions");
            checkBox.requestFocus();
        }
        else
        {
            dialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_SHORT).show();
                            updateUI();
                            //set data in firebase
                        } else {
                            Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    });
        }


    }

    private void updateUI() {
        user_name.setText("");
        user_email.setText("");
        user_password.setText("");
        user_phn.setText("");
    }


}