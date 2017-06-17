package jp.toastkid.search_widget.favorite;

import android.app.SearchManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import io.realm.Realm;

/**
 *
 * @author toastkidjp
 */
public class FavoriteSearchService extends Service {

    public static final String EXTRA_KEY_CATEGORY = "Category";

    public static final String EXTRA_KEY_QUERY    = SearchManager.QUERY;

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        final FavoriteSearch favoriteSearch = makeFavoriteSearch(
                intent.getStringExtra(EXTRA_KEY_CATEGORY),
                intent.getStringExtra(EXTRA_KEY_QUERY)
        );
        insertFavoriteColor(favoriteSearch);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    private void insertFavoriteColor(final FavoriteSearch favoriteSearch) {
        Realm.init(getApplicationContext());
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(favoriteSearch);
        realm.commitTransaction();
    }

    private FavoriteSearch makeFavoriteSearch(final String category, final String query) {
        final FavoriteSearch fs = new FavoriteSearch();
        fs.setCategory(category);
        fs.setQuery(query);
        return fs;
    }

}
