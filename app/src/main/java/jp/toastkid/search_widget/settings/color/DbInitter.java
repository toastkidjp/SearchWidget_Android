package jp.toastkid.search_widget.settings.color;

import android.content.Context;

import jp.toastkid.search_widget.favorite.OrmaDatabase;

/**
 * @author toastkidjp
 */
class DbInitter {

    private static OrmaDatabase orma;

    static OrmaDatabase get(final Context context) {
        if (orma == null) {
            orma = OrmaDatabase.builder(context)
                    .name("saved_color.db")
                    .build();
        }
        return orma;
    }
}
