package jp.toastkid.search_widget.libs;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import jp.toastkid.search_widget.favorite.AddingFavoriteSearchService;
import jp.toastkid.search_widget.favorite.FavoriteSearchActivity;
import jp.toastkid.search_widget.search.SearchActivity;
import jp.toastkid.search_widget.settings.SettingsActivity;
import jp.toastkid.search_widget.settings.color.ColorSettingActivity;

/**
 * Factory of {@link PendingIntent}.
 *
 * @author toastkidjp
 */
public class PendingIntentFactory {

    /**
     * Make launch search intent.
     * @param context
     * @return {@link SearchActivity}'s pending intent
     */
    public static PendingIntent makeSearchIntent(final Context context) {
        final Intent intent = SearchActivity.makeIntent(context);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Make launch settings intent.
     * @param context
     * @return {@link SettingsActivity}'s pending intent
     */
    public static PendingIntent makeSettingsIntent(final Context context) {
        final Intent intent = SettingsActivity.makeIntent(context);
        return PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Make launch color settings intent.
     * @param context
     * @return {@link ColorSettingActivity}'s pending intent
     */
    public static PendingIntent makeColorSettingsIntent(final Context context) {
        final Intent intent = ColorSettingActivity.makeIntent(context);
        return PendingIntent.getActivity(context, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Make adding favorite search intent.
     * @param context
     * @return {@link FavoriteSearchActivity} launching pending intent
     */
    public static PendingIntent makeFavoriteSearchPendingIndent(final Context context) {
        final Intent intent = new Intent(context, FavoriteSearchActivity.class);
        return PendingIntent.getActivity(context, 3, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Make adding favorite search intent.
     * @param context
     * @param category
     * @param query
     * @return {@link AddingFavoriteSearchService}'s pending intent
     */
    public static PendingIntent makeFavoriteSearchAddingPendingIndent(
            final Context context, final String category, final String query) {
        final Intent intent = new Intent(context, AddingFavoriteSearchService.class);
        intent.putExtra(AddingFavoriteSearchService.EXTRA_KEY_CATEGORY, category);
        intent.putExtra(AddingFavoriteSearchService.EXTRA_KEY_QUERY,    query);
        return PendingIntent.getService(context, 4, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
