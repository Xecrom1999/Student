package com.example.user.student;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gamrian on 15/11/2016.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final SharedPreferences preferences = getSharedPreferences("adCount", Context.MODE_PRIVATE);
        final int count = preferences.getInt("count", -1);

        if (count % 7 == 0)
            preferences.edit().putInt("count", count - 2).commit();
    }
}
