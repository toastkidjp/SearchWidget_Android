package jp.toastkid.search_widget.search;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;

import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.favorite.AddingFavoriteSearchReceiver;
import jp.toastkid.search_widget.libs.PendingIntentFactory;

/**
 * Search intent launcher.
 *
 * @author toastkidjp
 */
class SearchIntentLauncher {

    private @NonNull Context context;

    private @ColorInt int backgroundColor;

    private @ColorInt int fontColor;

    private @NonNull String category;

    private @NonNull String query;

    private Resources resources;

    SearchIntentLauncher(@NonNull final Context context) {
        this.context   = context;
        this.resources = context.getResources();
    }

    SearchIntentLauncher setBackgroundColor(@ColorInt final int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    SearchIntentLauncher setFontColor(@ColorInt final int fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    SearchIntentLauncher setCategory(@NonNull final String category) {
        this.category = category;
        return this;
    }

    SearchIntentLauncher setQuery(@NonNull final String query) {
        this.query = query;
        return this;
    }

    void invoke() {
        final CustomTabsIntent intent = new CustomTabsIntent.Builder()
                .setToolbarColor(backgroundColor)
                .setCloseButtonIcon(decodeResource(R.drawable.ic_back))
                .setSecondaryToolbarColor(fontColor)
                .setStartAnimations(context, android.R.anim.fade_in, android.R.anim.fade_out)
                .setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .addDefaultShareMenuItem()
                .addMenuItem(
                        context.getString(R.string.title_search),
                        PendingIntentFactory.makeSearchIntent(context)
                )
                .addMenuItem(
                        context.getString(R.string.title_settings),
                        PendingIntentFactory.makeSettingsIntent(context)
                )
                .addMenuItem(
                        context.getString(R.string.title_settings_color),
                        PendingIntentFactory.makeColorSettingsIntent(context)
                )
                .addMenuItem(
                        "お気に入り検索に追加",
                        AddingFavoriteSearchReceiver.makeBroadcastPendingIndent(context, category, query)
                )
                .build();
        intent.launchUrl(context, new UrlFactory().make(category, query));
    }

    private Bitmap decodeResource(@DrawableRes final int id) {
        return BitmapFactory.decodeResource(resources, id);
    }
}
