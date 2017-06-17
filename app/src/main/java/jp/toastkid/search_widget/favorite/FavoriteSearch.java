package jp.toastkid.search_widget.favorite;

import android.support.annotation.NonNull;

import io.realm.RealmObject;

/**
 * @author toastkidjp
 */
public class FavoriteSearch extends RealmObject {

    private String category;

    private String query;

    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull final String category) {
        this.category = category;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(@NonNull final String query) {
        this.query = query;
    }
}
