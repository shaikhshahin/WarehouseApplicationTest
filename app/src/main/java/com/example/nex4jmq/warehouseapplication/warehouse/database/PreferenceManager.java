package com.example.nex4jmq.warehouseapplication.warehouse.database;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    protected final static int DEFAULT = 0;
    private String name = "";

    public String readSharedPreference(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        return name = sharedPreferences.getString(key,value);
    }

    public void writeSharedPreference(Context context,String key,String value ){

        SharedPreferences sharedPreferences = context.getSharedPreferences(value, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
}

