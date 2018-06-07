package com.example.vchau.androidmanualnativestatic;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mopub.common.UrlAction;
import com.mopub.common.UrlHandler;
import com.mopub.common.util.Drawables;
import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.BaseNativeAd;
import com.mopub.nativeads.MoPubAdRenderer;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.NativeImageHelper;
import com.mopub.nativeads.StaticNativeAd;
import com.mopub.nativeads.ViewBinder;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    private MoPubNative moPubNative;
    private MoPubNative.MoPubNativeNetworkListener moPubNativeNetworkListener;
    private RelativeLayout nativeAdView;
    private ViewBinder viewBinder;
    private RelativeLayout parentView;
    private AdapterHelper adapterHelper;
    private NativeAd.MoPubNativeEventListener moPubNativeEventListener;
    private MoPubAdRenderer moPubAdRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        nativeAdView = (RelativeLayout) findViewById(R.id.nativeAdView);
        parentView = (RelativeLayout) findViewById(R.id.parentView);

        adapterHelper = new AdapterHelper(this, 0, 3); // When standalone, any range will be fine.

        moPubNativeEventListener = new NativeAd.MoPubNativeEventListener() {

            @Override
            public void onImpression(View view) {
                Log.d(TAG, "onImpression");
                // Impress is recorded - do what is needed AFTER the ad is visibly shown here.
            }

            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick");
                // Click tracking.
            }
        };

        moPubNativeNetworkListener = new MoPubNative.MoPubNativeNetworkListener() {

            @Override
            public void onNativeLoad(final NativeAd nativeAd) {
                Log.d(TAG, "onNativeLoad");

                // Retrieve the pre-built ad view that AdapterHelper prepared for us.
                View v = adapterHelper.getAdView(null, nativeAdView, nativeAd, new ViewBinder.Builder(0).build());

                // Set the native event listeners (onImpression, and onClick).
                nativeAd.setMoPubNativeEventListener(moPubNativeEventListener);

                // Add the ad view to our view hierarchy, defined in the XML (activity_main.xml).
                parentView.addView(v);
            }


            @Override
            public void onNativeFail(NativeErrorCode errorCode) {
                Log.d(TAG, "onNativeFail: " + errorCode.toString());
            }
        };

        //Next, create a ViewBinder object specifying the binding between your layout XML and the ad’s content
        viewBinder = new ViewBinder.Builder(R.layout.native_ad_layout)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        // Sample MoPub native ad unit: 11a17b188668469fb0412708c3d16813
        moPubNative = new MoPubNative(this, "11a17b188668469fb0412708c3d16813", moPubNativeNetworkListener);

        // Create a new renderer for native static ads.
        moPubAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        moPubNative.registerAdRenderer(moPubAdRenderer);

        // (Optional)Specify which native assets you want to use in your ad.
//        EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
//                RequestParameters.NativeAdAsset.TITLE,
//                RequestParameters.NativeAdAsset.TEXT,
//                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT,
//                RequestParameters.NativeAdAsset.MAIN_IMAGE,
//                RequestParameters.NativeAdAsset.ICON_IMAGE,
//                RequestParameters.NativeAdAsset.STAR_RATING);
//
//       requestParameters = new RequestParameters.Builder()
//                .keywords("gender:m,age:27")
//                .location(exampleLocation)
//                .desiredAssets(desiredAssets)
//                .build();

        // Send out the ad request.
        moPubNative.makeRequest();
//        moPubNative.makeRequest(requestParameters);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        moPubNative.destroy();
        moPubNative = null;

        moPubNativeNetworkListener = null;
        moPubNativeEventListener = null;
    }

}
