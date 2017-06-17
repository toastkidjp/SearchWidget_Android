package jp.toastkid.search_widget.favorite;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;

import com.github.gfx.android.orma.Deleter;

import io.reactivex.schedulers.Schedulers;
import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.libs.Toaster;

/**
 * @author toastkidjp
 */
class Clear {

    private final Context context;

    private final View view;

    private final Deleter<FavoriteSearch, ?> deleter;

    Clear(@NonNull final View view, final Deleter<FavoriteSearch, ?> deleter) {
        this.view    = view;
        this.context = view.getContext();
        this.deleter = deleter;
    }

    void invoke() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.favorite_clear_all_title)
                .setMessage(Html.fromHtml(context.getString(R.string.confirm_clear_all_settings)))
                .setCancelable(true)
                .setNegativeButton(R.string.cancel, (d, i) -> d.cancel())
                .setPositiveButton(R.string.ok,     (d, i) -> {
                    deleter.executeAsSingle()
                            .subscribeOn(Schedulers.io())
                            .subscribe(v -> {
                                Toaster.snackShort(
                                        view,
                                        R.string.settings_color_delete,
                                        ((ColorDrawable) view.getBackground()).getColor()
                                );
                                d.dismiss();
                            }
                    );
                })
                .show();
    }
}
