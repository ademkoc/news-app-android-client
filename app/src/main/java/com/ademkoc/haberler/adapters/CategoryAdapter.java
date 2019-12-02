package com.ademkoc.haberler.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ademkoc.haberler.R;
import com.ademkoc.haberler.models.Kategori;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adem on 4.06.2017.
 */

public class CategoryAdapter extends BaseAdapter {

    private static final String TAG = CategoryAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<Kategori> mKategoriList;

    public CategoryAdapter(Activity activity, List<Kategori> kategoriler) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mKategoriList = kategoriler;
    }

    @Override
    public int getCount() {
        return mKategoriList.size();
    }

    @Override
    public Kategori getItem(int position) {
        return mKategoriList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_category, null);

            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.category_title);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.category_image);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.category_checkBox);

            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Kategori k = (Kategori) viewHolder.text.getTag();
                    if (b)
                        k.setSelected(true);
                    else
                        k.setSelected(false);
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Kategori kategori = mKategoriList.get(position);

        if (kategori != null) {
            String kategoriAdi = kategori.getKategoriAdi();

            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(kategoriAdi);

            TextDrawable drawable = TextDrawable.builder().buildRound(kategori.getKategoriAdi().substring(0,1), color);
            viewHolder.imageView.setImageDrawable(drawable);

            viewHolder.text.setText(kategoriAdi);
            viewHolder.text.setTag(kategori);
        }

        return convertView;
    }

    public List<Integer> getSelectedIds() {
        List<Integer> list = new ArrayList<>();
        for (Kategori kat : mKategoriList) {
            if (kat.isSelected())
                list.add(kat.getId());
        }
        return list;
    }

    private static class ViewHolder {
        TextView text;
        CheckBox checkBox;
        ImageView imageView;
    }
}