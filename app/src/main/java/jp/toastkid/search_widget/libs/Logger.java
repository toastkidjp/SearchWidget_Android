package jp.toastkid.search_widget.libs;

import android.util.Log;

import jp.toastkid.search_widget.BuildConfig;

/**
 * @author toastkidjp
 */
public class Logger {

    public static void i(final String s) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        Log.i("Logger", s);
    }
}
