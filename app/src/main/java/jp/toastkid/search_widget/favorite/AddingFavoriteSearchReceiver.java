package jp.toastkid.search_widget.favorite;

import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import io.reactivex.annotations.NonNull;

/**
 *
 * @author toastkidjp
 */
public class AddingFavoriteSearchReceiver extends BroadcastReceiver {

    /**
     * Extra key of category.
     */
    public static final String EXTRA_KEY_CATEGORY = "Category";

    /**
     * Extra key of query.
     */
    public static final String EXTRA_KEY_QUERY    = SearchManager.QUERY;

    @Override
    public void onReceive(
            final Context context,
            final Intent intent
    ) {
        if (intent == null || context == null) {
            Toast.makeText(context, "intent or context is null.", Toast.LENGTH_SHORT).show();
            return;
        }
        new Insertion(
                context.getApplicationContext(),
                intent.getStringExtra(EXTRA_KEY_CATEGORY),
                intent.getStringExtra(EXTRA_KEY_QUERY)
        ).insert();
        Toast.makeText(context, intent.getStringExtra(EXTRA_KEY_QUERY), Toast.LENGTH_SHORT).show();
    }

    /**
     * Make adding favorite search intent.
     * @param context
     * @param category
     * @param query
     * @return {@link AddingFavoriteSearchReceiver}'s pending intent
     */
    public static PendingIntent makeBroadcastPendingIndent(
            @NonNull final Context context,
            @NonNull final String category,
            @NonNull final String query
    ) {
        final Intent intent = new Intent(context, AddingFavoriteSearchReceiver.class);
        intent.putExtra(EXTRA_KEY_CATEGORY, category);
        intent.putExtra(EXTRA_KEY_QUERY,    query);
        return PendingIntent.getBroadcast(context, 4, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
