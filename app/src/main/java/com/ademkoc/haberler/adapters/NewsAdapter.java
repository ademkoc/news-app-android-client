package com.ademkoc.haberler.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ademkoc.haberler.NewsDetailActivity;
import com.ademkoc.haberler.R;
import com.ademkoc.haberler.models.Haber;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Adem on 8.06.2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private static final String TAG = NewsAdapter.class.getSimpleName();
    private Context mContext;
    private List<Haber> mNewsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, category, hint;
        public ImageView source, thumbnail, overflow;
        public Haber haber;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.card_tvHaberBaslik);
            hint = (TextView) view.findViewById(R.id.card_newsHint);
            category = (TextView) view.findViewById(R.id.card_category);
            thumbnail = (ImageView) view.findViewById(R.id.card_newsImage);
            source = (ImageView) view.findViewById(R.id.card_source_img);
            overflow = (ImageView) view.findViewById(R.id.card_overflow);

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, NewsDetailActivity.class);
                    i.putExtra("haber", haber);
                    mContext.startActivity(i);
                }
            });
        }
    }

    public NewsAdapter(Context context, List<Haber> newsList) {
        mContext = context;
        mNewsList = newsList;
    }

    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NewsAdapter.MyViewHolder holder, int position) {
        try{
            holder.haber = mNewsList.get(position);
            holder.title.setText(holder.haber.getHaberBaslik());
            holder.category.setText(holder.haber.getKategori().getKategoriAdi());
            holder.hint.setText(mContext.getString(R.string.news, holder.haber.getHaberMetni().substring(0, 127)));

            Picasso.with(mContext).load(holder.haber.getImgUrl()).into(holder.thumbnail);
            Picasso.with(mContext).load(holder.haber.getKaynak().getImgUrl()).into(holder.source);

            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(holder.overflow);
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: "+e.toString());
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.news_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_save:
                        Toast.makeText(mContext, "Kaydet", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.action_share:
                        Toast.makeText(mContext, "Paylaş", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
}
