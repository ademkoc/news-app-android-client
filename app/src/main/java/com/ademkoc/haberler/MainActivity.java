package com.ademkoc.haberler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ademkoc.haberler.adapters.NewsAdapter;
import com.ademkoc.haberler.models.Haber;
import com.ademkoc.haberler.rest.ApiClient;
import com.ademkoc.haberler.rest.ApiInterface;
import com.ademkoc.haberler.utils.QueryPreferences;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Adem on 4.06.2017.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;
    private List<Haber> mHaberList;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!startControls())
            return;

        if (mHaberList == null) {
            mHaberList = new ArrayList<>();

            getNewsFromRemoteDB();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewsFromRemoteDB();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    private boolean startControls(){
        Log.e(TAG, "startControls()");
        //Kullanıcı kontrol
        String email = QueryPreferences.getEmail(getApplicationContext());
        if (email == null) {
            Log.e(TAG, "user is null");
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            finish();
            return false;
        }

        //Kategori kontorl
        if (QueryPreferences.getCategoryIds(getBaseContext()) == null) {
            Log.e(TAG, "category isn't selected");
            startActivity(new Intent(MainActivity.this, CategoryActivity.class));
            return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getNewsFromRemoteDB(){
        List<Integer> list = QueryPreferences.getCategoryIds(this);

        ApiInterface apiService = new ApiClient().getClient().create(ApiInterface.class);

        Call<List<Haber>> callNews = apiService.getNews(list);
        callNews.enqueue(new Callback<List<Haber>>() {
            @Override
            public void onResponse(Call<List<Haber>> call, Response<List<Haber>> response) {
                mSwipeRefreshLayout.setRefreshing(false);
                mHaberList = response.body();
                findViewById(R.id.mainAct_btnTekrarDene).setVisibility(View.GONE);
                setRecyclerView();
            }

            @Override
            public void onFailure(Call<List<Haber>> call, Throwable t) {
                Log.e(TAG, "onFailure");
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "Haberler alınamadı", Toast.LENGTH_SHORT).show();
                findViewById(R.id.mainAct_btnTekrarDene).setVisibility(View.VISIBLE);
                t.printStackTrace();
            }
        });
    }

    private void setRecyclerView() {
        mNewsAdapter = new NewsAdapter(this, mHaberList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mNewsAdapter);
    }

    private void signOut() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        QueryPreferences.saveCategoryIds(getApplicationContext(), null);
        QueryPreferences.saveEmail(getApplicationContext(), null);
        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
    }

    public void mainAct_btnTekrarDene_onClick(View view) {
        getNewsFromRemoteDB();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
    }
}
