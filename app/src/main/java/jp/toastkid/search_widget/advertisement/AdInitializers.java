package jp.toastkid.search_widget.advertisement;

import android.content.Context;
import android.support.annotation.NonNull;

import jp.toastkid.search_widget.BuildConfig;

/**
 * @author toastkidjp
 */
public class AdInitializers {

    public static AdInitializer find(@NonNull final Context context) {
        if (BuildConfig.DEBUG) {
            return new TestAdInitializer(context);
        }
        return new ProductionAdInitializer(context);
    }
}
