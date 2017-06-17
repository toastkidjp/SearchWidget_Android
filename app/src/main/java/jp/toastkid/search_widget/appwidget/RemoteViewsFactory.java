package jp.toastkid.search_widget.appwidget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.libs.PendingIntentFactory;
import jp.toastkid.search_widget.libs.preference.PreferenceApplier;

/**
 * App Widget's RemoteViews factory.
 *
 * @author toastkidjp
 */
class RemoteViewsFactory {

    /**
     * Make RemoteViews.
     * @param context
     * @return RemoteViews
     */
    @NonNull
    static RemoteViews make(final Context context) {
        final RemoteViews remoteViews
                = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setOnClickPendingIntent(
                R.id.widget_search,   PendingIntentFactory.makeSearchIntent(context));
        remoteViews.setOnClickPendingIntent(
                R.id.widget_settings, PendingIntentFactory.makeSettingsIntent(context));
        remoteViews.setOnClickPendingIntent(
                R.id.widget_favorite, PendingIntentFactory.makeFavoriteSearchPendingIndent(context));

        final PreferenceApplier preferenceApplier = new PreferenceApplier(context);

        remoteViews.setInt(
                R.id.widget_background, "setBackgroundColor", preferenceApplier.getColor());
        remoteViews.setInt(
                R.id.widget_search_border, "setBackgroundColor", preferenceApplier.getFontColor());
        remoteViews.setInt(
                R.id.widget_search_image, "setColorFilter", preferenceApplier.getFontColor());
        remoteViews.setInt(
                R.id.widget_favorite, "setColorFilter", preferenceApplier.getFontColor());
        remoteViews.setInt(
                R.id.widget_settings, "setColorFilter", preferenceApplier.getFontColor());
        remoteViews.setTextColor(
                R.id.widget_search_text, preferenceApplier.getFontColor());

        return remoteViews;
    }

}
