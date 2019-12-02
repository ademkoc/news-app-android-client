package com.ademkoc.haberler.services;

import android.util.Log;

import com.ademkoc.haberler.models.Kullanici;
import com.ademkoc.haberler.rest.ApiClient;
import com.ademkoc.haberler.rest.ApiInterface;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Adem on 12.06.2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements Callback<Void> {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token){
        Log.e(TAG, "sendRegistrationToServer()");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            return;

        Log.e(TAG, "firebase user not null");
        Kullanici kullanici = new Kullanici(user.getDisplayName(), user.getEmail(), token);

        ApiInterface apiService = new ApiClient().getClient().create(ApiInterface.class);
        Call<Void> call = apiService.sendUser(kullanici);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        Log.e(TAG, String.valueOf(response.code()));
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        Log.e(TAG, "onFailure");
    }
}