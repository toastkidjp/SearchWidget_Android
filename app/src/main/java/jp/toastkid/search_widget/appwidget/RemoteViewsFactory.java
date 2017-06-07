package jp.toastkid.search_widget.appwidget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.libs.preference.PreferenceApplier;
import jp.toastkid.search_widget.search.SearchActivity;
import jp.toastkid.search_widget.settings.SettingsActivity;

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
        remoteViews.setOnClickPendingIntent(R.id.widget_search, makeSearchIntent(context));
        remoteViews.setOnClickPendingIntent(R.id.widget_settings, makeSettingsIntent(context));
        remoteViews.setInt(R.id.widget_background, "setBackgroundColor", new PreferenceApplier(context).getColor());
        return remoteViews;
    }

    /**
     * Make launch search intent.
     * @param context
     * @return
     */
    static PendingIntent makeSearchIntent(final Context context) {
        final Intent intent = SearchActivity.makeIntent(context);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Make launch settings intent.
     * @param context
     * @return
     */
    static PendingIntent makeSettingsIntent(final Context context) {
        final Intent intent = SettingsActivity.makeIntent(context);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
