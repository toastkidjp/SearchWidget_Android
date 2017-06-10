package jp.toastkid.search_widget.advertisement;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import jp.toastkid.search_widget.BuildConfig;
import jp.toastkid.search_widget.R;

/**
 * @author toastkidjp
 */
class TestAdInitializer implements AdInitializer {

    /**
     * @param context need ApplicationContext
     */
    TestAdInitializer(@NonNull final Context context) {
        if (!BuildConfig.DEBUG) {
            throw new IllegalStateException();
        }
        final String appAdId = context.getString(R.string.production_app_admob_id);
        MobileAds.initialize(context, appAdId);
    }

    /**
     * Do AdRequest.
     */
    @Override
    public void invoke(@NonNull final AdView adView) {
        if (!BuildConfig.DEBUG) {
            throw new IllegalStateException();
        }
        final AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("ACCB0CA3D05AE25EB35C22A303702020")
                .build();
        adView.loadAd(request);
    }
}
