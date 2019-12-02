package com.ademkoc.haberler.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by Adem on 4.06.2017.
 */

public class QueryPreferences {

    public static final String PREF_CATEGORY_IDS = "is_category_ids";
    public static final String PREF_EMAIL = "email";

    public static void saveCategoryIds(Context context, List<Integer> list) {
        Set<String> idsSet = null;

        if(list != null) {
            idsSet = new HashSet<>();
            for (Integer i : list) {
                idsSet.add(Integer.toString(i));
            }
        }

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(PREF_CATEGORY_IDS, idsSet)
                .apply();
    }

    public static List<Integer> getCategoryIds(Context context) {
        Set<String> set = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(PREF_CATEGORY_IDS, null);
        if (set == null)
            return null;
        List<Integer> list = new ArrayList<>();
        for (String s : set) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }

    public static void saveEmail(Context context, String email) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_EMAIL, email)
                .apply();
    }

    public static String getEmail(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_EMAIL, null);
    }
}
