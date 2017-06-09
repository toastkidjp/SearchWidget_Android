package jp.toastkid.search_widget.libs.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Network checker.
 *
 * @author toastkidjp
 */
public class NetworkChecker {

    /**
     * Return true if we can't use network.
     * @param context
     * @return
     */
    public static boolean isNotAvailable(final Context context) {
        return !isAvailable(context);
    }

    /**
     * Check usable network.
     * @param context
     * @return
     */
    public static boolean isAvailable(final Context context) {
        final ConnectivityManager cm
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isConnected();
    }
}
