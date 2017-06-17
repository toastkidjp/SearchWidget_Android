package jp.toastkid.search_widget.favorite;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.MessageFormat;

import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.libs.Logger;
import jp.toastkid.search_widget.libs.Toaster;
import jp.toastkid.search_widget.search.SearchCategorySpinnerInitializer;

/**
 * Show input dialog and call inserting action.
 *
 * @author toastkidjp
 */
class Addition {

    private final Context context;

    private final View view;

    Addition(@NonNull final View view) {
        this.context = view.getContext();
        this.view = view;
    }

    void invoke() {
        final View content = LayoutInflater.from(context)
                .inflate(R.layout.favorite_search_addition_dialog_content, null);
        final Spinner categorySelector
                = (Spinner) content.findViewById(R.id.favorite_search_addition_categories);
        SearchCategorySpinnerInitializer.initialize(categorySelector);

        new AlertDialog.Builder(context)
                .setTitle(R.string.favorite_add_title)
                .setView(content)
                .setCancelable(true)
                .setNegativeButton(R.string.cancel, (d, i) -> d.cancel())
                .setPositiveButton(R.string.ok,     (d, i) -> {
                    @ColorInt final int color = ((ColorDrawable) view.getBackground()).getColor();

                    final EditText input = (EditText) content
                            .findViewById(R.id.favorite_search_addition_query_input);

                    final String query = input.getText().toString();

                    Logger.i("a" + query + "a");
                    if (TextUtils.isEmpty(query)) {
                        Toaster.snackShort(
                                view,
                                R.string.favorite_search_addition_dialog_empty_message,
                                color
                        );
                        return;
                    }

                    final String category = categorySelector.getSelectedItem().toString();

                    new Insertion(context, category, query).insert();

                    final String message = MessageFormat.format(
                            context.getString(R.string.favorite_search_addition_successful_format),
                            query
                    );
                    Toaster.snackShort(view, message, color);
                    d.dismiss();
                })
                .show();
    }
}
