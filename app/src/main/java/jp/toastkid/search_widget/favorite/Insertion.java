package jp.toastkid.search_widget.favorite;

import android.content.Context;
import android.support.annotation.NonNull;

import io.realm.Realm;

/**
 * @author toastkidjp
 */
class Insertion {

    private Context context;

    private String category;

    private String query;

    Insertion(
            @NonNull final Context context,
            @NonNull final String  category,
            @NonNull final String  query
    ) {
        this.context = context;
        this.category = category;
        this.query = query;
    }

    void insert() {
        insertFavoriteSearch(makeFavoriteSearch(category, query));
    }

    private void insertFavoriteSearch(final FavoriteSearch favoriteSearch) {
        Realm.init(context);
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
