package com.ademkoc.haberler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ademkoc.haberler.fragments.SignInFragment;
import com.ademkoc.haberler.fragments.SignUpFragment;
import com.ademkoc.haberler.fragments.SplashFragment;
import com.ademkoc.haberler.models.Kullanici;
import com.ademkoc.haberler.rest.ApiClient;
import com.ademkoc.haberler.rest.ApiInterface;
import com.ademkoc.haberler.utils.QueryPreferences;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = WelcomeActivity.class.getSimpleName();
    protected ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findViewById(R.id.btnGirisEkrani).setOnClickListener(this);
        findViewById(R.id.btnUyeEkrani).setOnClickListener(this);
        mProgressDialog = new ProgressDialog(this);

        showFragment(getSupportFragmentManager(), new SplashFragment());
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnGirisEkrani:
                showFragment(getSupportFragmentManager(), new SignInFragment());
                break;
            case R.id.btnUyeEkrani:
                showFragment(getSupportFragmentManager(), new SignUpFragment());
                break;
        }

    }

    public void showFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment, fragment);
        fragmentTransaction.commit();
    }

    public void saveUserRemoteDB(final FirebaseUser user) {
        String token = FirebaseInstanceId.getInstance().getToken();

        Kullanici kullanici = new Kullanici(user.getDisplayName(), user.getEmail(), token);

        ApiInterface apiService = new ApiClient().getClient().create(ApiInterface.class);
        Call<Void> postUser = apiService.sendUser(kullanici);

        postUser.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e(TAG, "onResponse: " + response.code());

                dismissProgressBar();
                if (response.isSuccessful()) {
                    QueryPreferences.saveEmail(getApplicationContext(), user.getEmail());
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    WelcomeActivity.this.finish();
                } else {
                    onFailure(call, null);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "onFailure() ");
                dismissProgressBar();
                QueryPreferences.saveEmail(WelcomeActivity.this, null);
                Toast.makeText(getApplicationContext(), "Hata oluştu, lütfen bilgilerinizi kontrol ediniz", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showProgressBar() {
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Giriş yapılıyor...");
        mProgressDialog.show();
    }

    public void dismissProgressBar() {
        mProgressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
    }
}
