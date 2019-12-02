package com.ademkoc.haberler.fragments;

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

import com.ademkoc.haberler.R;
import com.ademkoc.haberler.WelcomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Adem on 3.06.2017.
 */

public class SignUpFragment extends Fragment {

    private static final String TAG = SignUpFragment.class.getSimpleName();
    private FirebaseAuth mAuth;
    private EditText etIsim, etEposta, etSifreTekrar, etSifre;
    private TextInputLayout tilIsim, tilEposta, tilSifreTekrar, tilSifre;
    private WelcomeActivity mWelcomeActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        mWelcomeActivity = (WelcomeActivity) getActivity();

        view.findViewById(R.id.btnUyeOl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForm();
            }
        });

        etIsim = (EditText)view.findViewById(R.id.etIsim);
        etEposta = (EditText)view.findViewById(R.id.etEposta);
        etSifreTekrar = (EditText)view.findViewById(R.id.etSifreTekrar);
        etSifre = (EditText)view.findViewById(R.id.etSifre);

        tilIsim = (TextInputLayout)view.findViewById(R.id.tilIsim);
        tilEposta = (TextInputLayout)view.findViewById(R.id.tilEposta);
        tilSifreTekrar = (TextInputLayout)view.findViewById(R.id.tilSifreTekrar);
        tilSifre = (TextInputLayout)view.findViewById(R.id.tilSifre);

        etIsim.addTextChangedListener(new MyTextWatcher(etIsim));
        etEposta.addTextChangedListener(new MyTextWatcher(etEposta));
        etSifre.addTextChangedListener(new MyTextWatcher(etSifre));
        etSifreTekrar.addTextChangedListener(new MyTextWatcher(etSifreTekrar));

        return view;
    }

    private void checkForm() {

        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        if (!validateSecPassword()) {
            return;
        }

        mWelcomeActivity.showProgressBar();

        mAuth.createUserWithEmailAndPassword(etEposta.getText().toString(), etSifre.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.e(TAG, "createUserWithEmail:success");

                    FirebaseUser user = mAuth.getCurrentUser();

                    WelcomeActivity activity = (WelcomeActivity)getActivity();
                    activity.saveUserRemoteDB(user);
                } else {
                    mWelcomeActivity.dismissProgressBar();
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePassword() {
        if (etSifre.getText().toString().trim().isEmpty()) {
            tilSifre.setError(getString(R.string.err_msg_password));
            requestFocus(etSifre);
            return false;
        } else if (etSifre.getText().length() < 6) {
            tilSifre.setError("En az 6 karakter giriniz!");
            requestFocus(etSifre);
            return false;
        } else {
            tilSifre.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateSecPassword() {
        if (etSifreTekrar.getText().toString().trim().isEmpty()) {
            tilSifreTekrar.setError(getString(R.string.err_msg_password));
            requestFocus(etSifreTekrar);
            return false;
        } else if (!etSifreTekrar.getText().toString().contentEquals(etSifre.getText().toString())) {
            tilSifreTekrar.setError(getString(R.string.err_msg_match_password));
            requestFocus(etSifreTekrar);
            return false;
        } else {
            tilSifreTekrar.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateName() {
        if (etIsim.getText().toString().trim().isEmpty()) {
            tilIsim.setError(getString(R.string.err_msg_name));
            requestFocus(etIsim);
            return false;
        } else {
            tilIsim.setErrorEnabled(false);
        }
        return true;
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
                case R.id.etIsim:
                    validateName();
                    break;
                case R.id.etEposta:
                    validateEmail();
                    break;
                case R.id.etSifre:
                    validatePassword();
                    break;
                case R.id.etSifreTekrar:
                    validateSecPassword();
                    break;
            }
        }
    }
}