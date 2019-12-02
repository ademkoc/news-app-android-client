package com.ademkoc.haberler.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ademkoc.haberler.MainActivity;
import com.ademkoc.haberler.R;
import com.ademkoc.haberler.WelcomeActivity;
import com.ademkoc.haberler.utils.QueryPreferences;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by Adem on 3.06.2017.
 */

public class SignInFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = SignUpFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 5353;
    private EditText etEposta, etSifre;
    private TextInputLayout tilEposta, tilSifre;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private WelcomeActivity mWelcomeActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        mAuth = FirebaseAuth.getInstance();

        mWelcomeActivity = (WelcomeActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        view.findViewById(R.id.btnUyeOl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForm();
            }
        });

        etEposta = (EditText) view.findViewById(R.id.txtEposta);
        etSifre = (EditText) view.findViewById(R.id.txtSifre);

        tilEposta = (TextInputLayout) view.findViewById(R.id.ilEposta);
        tilSifre = (TextInputLayout) view.findViewById(R.id.ilSifre);

        etEposta.addTextChangedListener(new MyTextWatcher(etEposta));
        etSifre.addTextChangedListener(new MyTextWatcher(etSifre));

        view.findViewById(R.id.btnGoogleSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        return view;
    }


    private void checkForm() {
        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(etEposta.getText().toString(), etSifre.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");

                        QueryPreferences.saveEmail(getContext(), etEposta.getText().toString());

                        Toast.makeText(getContext(), "Hoşgeldiniz", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getActivity(), "Giriş başarısız", Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }

    private boolean validateEmail() {
        String email = etEposta.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            tilEposta.setError(getString(R.string.err_msg_email));
            requestFocus(etEposta);
            return false;
        } else {
            tilEposta.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        if (etSifre.getText().toString().trim().isEmpty()) {
            tilSifre.setError(getString(R.string.err_msg_password));
            requestFocus(etSifre);
            return false;
        } else {
            tilSifre.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.txtEposta:
                    validateEmail();
                    break;
                case R.id.txtSifre:
                    validatePassword();
                    break;
            }
        }
    }

    private void signIn() {
        mWelcomeActivity.showProgressBar();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(getActivity(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.e(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            mWelcomeActivity.saveUserRemoteDB(user);

                        } else {
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(getActivity(), "Giriş yapılamadı", Toast.LENGTH_SHORT).show();
                mWelcomeActivity.dismissProgressBar();
            }
        }
    }
}
