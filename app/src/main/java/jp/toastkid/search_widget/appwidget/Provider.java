package jp.toastkid.search_widget.appwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

/**
 * App widget's provider.
 *
 * @author toastkidjp
 */
public class Provider extends AppWidgetProvider {

    @Override
    public void onUpdate(
            final Context context,
            final AppWidgetManager appWidgetManager,
            final int[] appWidgetIds
    ) {
        updateWidget(context, RemoteViewsFactory.make(context));
    }

    /**
     * Update widget.
     * @param context
     * @param remoteViews
     */
    public static void updateWidget(final Context context, final RemoteViews remoteViews) {
        final ComponentName myWidget = new ComponentName(context, Provider.class);
        final AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }
}