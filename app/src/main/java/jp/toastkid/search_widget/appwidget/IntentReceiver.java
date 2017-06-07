package jp.toastkid.search_widget.appwidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.libs.preference.PreferenceApplier;

/**
 * @author toastkidjp
 */
public class IntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals("UPDATE_WIDGET")) {
            Provider.updateWidget(
                    context.getApplicationContext(),
                    RemoteViewsFactory.make(context)
            );
        }
    }
}