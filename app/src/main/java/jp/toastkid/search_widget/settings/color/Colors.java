package jp.toastkid.search_widget.settings.color;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import io.realm.Realm;
import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.libs.Toaster;

/**
 * @author toastkidjp
 */
class Colors {

    static void setBgAndText(
            final TextView tv,
            @ColorInt final int bgColor,
            @ColorInt final int fontColor
    ) {
        tv.setBackgroundColor(bgColor);
        tv.setTextColor(fontColor);
    }

    static void setSaved(final TextView tv, final SavedColor color) {
        tv.setBackgroundColor(color.getBgColor());
        tv.setTextColor(color.getFontColor());
    }

    @NonNull
    private static SavedColor makeSavedColor(
            @ColorInt final int bgColor,
            @ColorInt final int fontColor
    ) {
        final SavedColor color = new SavedColor();
        color.setBgColor(bgColor);
        color.setFontColor(fontColor);
        return color;
    }

    static void insertColor(final Realm realm, final int bgColor, final int fontColor) {
        realm.beginTransaction();
        realm.copyToRealm(makeSavedColor(bgColor, fontColor));
        realm.commitTransaction();
    }

    static void showClearColorsDialog(
            final Context context,
            final View view
    ) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.title_clear_saved_color)
                .setMessage(Html.fromHtml(context.getString(R.string.confirm_clear_all_settings)))
                .setCancelable(true)
                .setNegativeButton(R.string.cancel, (d, i) -> d.cancel())
                .setPositiveButton(R.string.ok,     (d, i) -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> realm.delete(SavedColor.class));
                    Toaster.snackShort(view, R.string.settings_color_delete, ((ColorDrawable) view.getBackground()).getColor());
                    d.dismiss();
                })
                .show();
    }
}
