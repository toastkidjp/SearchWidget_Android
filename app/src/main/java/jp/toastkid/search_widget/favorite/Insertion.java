package jp.toastkid.search_widget.favorite;

import android.content.Context;
import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

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
        Completable.create(e -> {
            DbInitter.get(context).insertIntoFavoriteSearch(favoriteSearch);
            e.onComplete();
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private FavoriteSearch makeFavoriteSearch(final String category, final String query) {
        final FavoriteSearch fs = new FavoriteSearch();
        fs.category = category;
        fs.query    = query;
        return fs;
    }

}
