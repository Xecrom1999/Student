package com.example.user.student;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by gamrian on 03/10/2016.
 */

public class Helper {

    public static Context mContext;
    final static int ACTIVITIES_PER_AD = 7;

    public static void showKeyboard(Context ctx, EditText view) {
        view.requestFocus();
        view.setSelection(view.length());
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void hideKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void setupAd(final Context ctx) {

        final InterstitialAd mInterstitialAd;

        final SharedPreferences preferences = ctx.getSharedPreferences("adCount", Context.MODE_PRIVATE);
        final int count = preferences.getInt("count", -1);

        preferences.edit().putInt("count", count + 1).commit();

        mInterstitialAd = new InterstitialAd(ctx);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        requestNewInterstitial(mInterstitialAd);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial(mInterstitialAd);
            }
            @Override
            public void onAdLoaded() {
                if (count % ACTIVITIES_PER_AD == 0 && mContext != ctx) {
                    mInterstitialAd.show();
                    mContext = ctx;
                }
            }
            @Override
            public void onAdFailedToLoad(int var1) {
                Log.d("MYLOG", var1+"");
            }
        });
    }

    private static void requestNewInterstitial(InterstitialAd mInterstitialAd) {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }
}
