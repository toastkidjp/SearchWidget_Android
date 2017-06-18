package jp.toastkid.search_widget.favorite;

import android.app.SearchManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 *
 * @author toastkidjp
 */
public class AddingFavoriteSearchService extends Service {

    public static final String EXTRA_KEY_CATEGORY = "Category";

    public static final String EXTRA_KEY_QUERY    = SearchManager.QUERY;

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        if (intent == null) {
            return START_STICKY_COMPATIBILITY;
        }
        new Insertion(
                getApplicationContext(),
                intent.getStringExtra(EXTRA_KEY_CATEGORY),
                intent.getStringExtra(EXTRA_KEY_QUERY)
        ).insert();
        return START_STICKY_COMPATIBILITY;
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

}
