package com.grayherring.randomradicalreminders.Activitys;

import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.grayherring.randomradicalreminders.R;

/**
 * Created by David on 12/27/2014.
 */
public class BaseActivity extends ActionBarActivity {
    protected AdView mAdView;

    protected void setUpAd() {
        mAdView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setVisibility(View.GONE);
        mAdView.setAdListener(new AdListener() {

            public void onAdClosed() {
                mAdView.setVisibility(View.GONE);
                super.onAdClosed();
            }

            public void onAdFailedToLoad(int errorCode) {
                mAdView.setVisibility(View.GONE);
                super.onAdFailedToLoad(errorCode);
            }

            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            public void onAdOpened() {
                super.onAdOpened();
            }

            public void onAdLoaded() {
              mAdView.setVisibility(View.VISIBLE);
                super.onAdLoaded();
            }
        });

    }
}
