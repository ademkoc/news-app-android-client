package com.ademkoc.haberler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ademkoc.haberler.models.Haber;
import com.squareup.picasso.Picasso;

/**
 * Created by Adem on 10.06.2017.
 */

public class NewsDetailActivity extends AppCompatActivity {

    private static final String TAG = NewsDetailActivity.class.getSimpleName();
    private TextView tvHaberBaslik, tvKaynak, tvTarih, tvHaberMetni;
    private ImageView imgHaber;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Intent i = getIntent();
        Haber haber = i.getParcelableExtra("haber");

        tvHaberBaslik = (TextView) findViewById(R.id.news_tvHaberBaslik);
        tvKaynak = (TextView) findViewById(R.id.news_tvKaynak);
        tvTarih = (TextView) findViewById(R.id.news_tvTarih);
        tvHaberMetni = (TextView) findViewById(R.id.news_tvHaberMetni);
        imgHaber = (ImageView) findViewById(R.id.news_newsImage);

        if (haber != null) {
            tvHaberBaslik.setText(haber.getHaberBaslik());
            tvKaynak.setText(haber.getKaynak().getKaynakAdi());
            tvTarih.setText(haber.getTarih().toString());
            tvHaberMetni.setText(haber.getHaberMetni());

            Picasso.with(getApplicationContext()).load(haber.getImgUrl()).into(imgHaber);

        } else {
            Log.e(TAG, "haber is null");
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy()");
        super.onDestroy();
    }
}
