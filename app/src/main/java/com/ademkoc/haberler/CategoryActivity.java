package com.ademkoc.haberler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.ademkoc.haberler.adapters.CategoryAdapter;
import com.ademkoc.haberler.models.Kategori;
import com.ademkoc.haberler.rest.ApiClient;
import com.ademkoc.haberler.rest.ApiInterface;
import com.ademkoc.haberler.utils.QueryPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Adem on 4.06.2017.
 */

public class CategoryActivity extends AppCompatActivity {

    private static final String TAG = CategoryActivity.class.getSimpleName();
    private ListView mListCategory;
    private List<Kategori> mKategoriList;
    private CategoryAdapter mCategoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mListCategory = (ListView) findViewById(R.id.catAc_listCategory);

        if (mKategoriList == null) {
            mKategoriList = new ArrayList<>();
            getKategorilerFromRemoteDB();
        }

        //list item'a tıklanınca cb'ı işaretlemek için
        mListCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox ch = (CheckBox) view.findViewById(R.id.category_checkBox);
                if (ch.isChecked())
                    ch.setChecked(false);
                else
                    ch.setChecked(true);

            }
        });
    }

    public void catAc_btnOK_onClick(View v) {
        if (mCategoryAdapter.getSelectedIds().size() < 1) {
            Toast.makeText(getApplicationContext(), "Lütfen seçim yapınız", Toast.LENGTH_SHORT).show();
            return;
        }
        QueryPreferences.saveCategoryIds(getApplicationContext(), mCategoryAdapter.getSelectedIds());
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        this.finish();
    }

    public void getKategorilerFromRemoteDB() {
        ApiInterface apiService = new ApiClient().getClient().create(ApiInterface.class);
        Call<List<Kategori>> callCategory = apiService.getCategorys();

        callCategory.enqueue(new Callback<List<Kategori>>() {
            @Override
            public void onResponse(Call<List<Kategori>> call, Response<List<Kategori>> response) {
                mKategoriList = response.body();
                setListView();
                findViewById(R.id.catAc_btnYenile).setVisibility(View.GONE);
                mListCategory.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<Kategori>> call, Throwable t) {
                Log.e(TAG, t.toString());
                Toast.makeText(getApplicationContext(), "Kategoriler alınamadı", Toast.LENGTH_SHORT).show();
                mListCategory.setVisibility(View.GONE);
                findViewById(R.id.catAc_btnYenile).setVisibility(View.VISIBLE);
            }
        });

    }

    private void setListView() {
        mCategoryAdapter = new CategoryAdapter(this, mKategoriList);
        mListCategory.setAdapter(mCategoryAdapter);
    }

    public void btnYenile_onClick(View v) {
        getKategorilerFromRemoteDB();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
    }
}
