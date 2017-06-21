package jp.toastkid.search_widget.favorite;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

    /** Context. */
    private final Context context;

    /** For using extract background color. */
    private final View view;

    /**
     *
     * @param view
     */
    Addition(@NonNull final View view) {
        this.context = view.getContext();
        this.view = view;
    }

    /**
     * Show input dialog.
     */
    void invoke() {
        final View content = LayoutInflater.from(context)
                .inflate(R.layout.favorite_search_addition_dialog_content, null);

        final Spinner categorySelector = initSpinner(content);

        final EditText input = initInput(content);

        new AlertDialog.Builder(context)
                .setTitle(R.string.favorite_add_title)
                .setView(content)
                .setCancelable(true)
                .setNegativeButton(R.string.cancel, (d, i) -> d.cancel())
                .setPositiveButton(R.string.ok,     (d, i) -> {
                    @ColorInt final int color = ((ColorDrawable) view.getBackground()).getColor();

                    final String query = input.getText().toString();

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

    /**
     * Initialize spinner.
     * @param content
     * @return
     */
    @NonNull
    private Spinner initSpinner(final View content) {
        final Spinner categorySelector
                = (Spinner) content.findViewById(R.id.favorite_search_addition_categories);
        SearchCategorySpinnerInitializer.initialize(categorySelector);
        return categorySelector;
    }

    /**
     * Initialize input field.
     * @param content
     * @return
     */
    @NonNull
    private EditText initInput(final View content) {
        final TextInputLayout inputLayout
                = (TextInputLayout) content.findViewById(R.id.favorite_search_addition_query);

        final EditText input = inputLayout.getEditText();
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.toString().length() == 0) {
                    inputLayout.setError(
                            context.getString(R.string.favorite_search_error_input_message));
                    return;
                }
                inputLayout.setErrorEnabled(false);
            }
        });
        return input;
    }
}
