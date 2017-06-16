package jp.toastkid.search_widget.search;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;

import jp.toastkid.search_widget.R;

/**
 * Search intent launcher.
 *
 * @author toastkidjp
 */
class SearchIntentLauncher {

    private @NonNull Context context;

    private @ColorInt int backgroundColor;

    private @ColorInt int fontColor;

    private @NonNull Uri uri;

    SearchIntentLauncher(@NonNull final Context context) {
        this.context = context;
    }

    SearchIntentLauncher setBackgroundColor(@ColorInt final int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    SearchIntentLauncher setFontColor(@ColorInt final int fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    SearchIntentLauncher setUri(@NonNull final Uri uri) {
        this.uri = uri;
        return this;
    }

    void invoke() {
        final CustomTabsIntent intent = new CustomTabsIntent.Builder()
                .setToolbarColor(backgroundColor)
                .setCloseButtonIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_back))
                .setSecondaryToolbarColor(fontColor)
                .setStartAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out)
                .setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .build();
        intent.launchUrl(context, uri);
    }
}
