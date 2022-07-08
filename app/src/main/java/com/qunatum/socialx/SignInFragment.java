package com.qunatum.socialx;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Collections;
import java.util.Objects;


public class SignInFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
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

    private FirebaseAuth mAuth;
    private EditText user_password;
    private EditText user_email;
    private static final int RC_SIGN_IN = 9001;
    private  CallbackManager callbackManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mAuth =  FirebaseAuth.getInstance();


        TextView sign_up = root.findViewById(R.id.signUpText);
        sign_up.setOnClickListener(view -> ((MainActivity) requireActivity()).selectPage(1));

        ImageView google_login = root.findViewById(R.id.googleLoginBtn);
        google_login.setOnClickListener(view -> LoginWithGoogle());

        ImageView facebook_login = root.findViewById(R.id.fbLoginBtn);
        facebook_login.setOnClickListener(view -> LoginWithFacebook());

        user_email = root.findViewById(R.id.editEmailSignIN);
        user_password = root.findViewById(R.id.editPassSignIn);
        Button login = root.findViewById(R.id.signInBtn);

        login.setOnClickListener(view -> LoginWithEmail(user_email.getText().toString(),user_password.getText().toString()));
        return root;

    }



    private void LoginWithEmail(String email, String password) {

         if(TextUtils.isEmpty(email))
        {
            user_email.setError("Email cannot be empty");
            user_email.requestFocus();
        }
        else if(TextUtils.isEmpty(password))
        {
            user_password.setError("Password cannot be empty");
            user_password.requestFocus();

        }
         else if(!email.contains("@"))
         {
             user_email.setError("Invalid Email");
             user_email.requestFocus();
         }
        else
        {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(requireContext(),Home.class);
                            startActivity(intent);
                            requireActivity().finish();

                        } else {
                            Toast.makeText(getActivity(),Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }


    private void LoginWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

                Toast.makeText(requireActivity(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        }else
            callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(requireContext(),Home.class);
                        startActivity(intent);
                        requireActivity().finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getActivity(),Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void LoginWithFacebook() {


        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Collections.singletonList("public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(@NonNull FacebookException exception) {

                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(requireContext(),Home.class);
                        startActivity(intent);
                        requireActivity().finish();

                    } else {
                        Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}