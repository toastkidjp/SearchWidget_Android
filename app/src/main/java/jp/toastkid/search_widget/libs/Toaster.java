package jp.toastkid.search_widget.libs;

import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Simple toasting utilities.
 *
 * @author toastkidjp
 */
public class Toaster {

    /**
     * Show simple snackbar on short time.
     *
     * @param view
     * @param messageId
     * @param color
     */
    public static void snackShort(
            final View view,
            @StringRes final int messageId,
            @ColorInt final int color
            ) {
        final Snackbar snackbar
                = Snackbar.make(view, messageId, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(color);
        snackbar.show();
    }
}
