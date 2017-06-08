package jp.toastkid.search_widget.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.libs.preference.PreferenceApplier;
import jp.toastkid.search_widget.settings.SettingsActivity;

/**
 * Search activity.
 * @author toastkidjp
 */
public class SearchActivity extends AppCompatActivity {

    /** Background. */
    @BindView(R.id.box_background)
    public View mBackground;

    /** Search input. */
    @BindView(R.id.search_input)
    public EditText mSearchInput;

    /** Search category. */
    @BindView(R.id.search_categories)
    public Spinner mSearchCategories;

    @BindView(R.id.search_close)
    public ImageView mSearchClose;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mSearchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId != EditorInfo.IME_ACTION_SEARCH) {
                return false;
            }
            search(mSearchCategories.getSelectedItem().toString(), v.getText().toString());
            return true;
        });
        mBackground.setBackgroundColor(new PreferenceApplier(this).getColor());
        mSearchClose.setOnClickListener(v -> close());
    }

    @OnClick(R.id.search_background)
    public void close() {
        finish();
    }

    /**
     * Do on click action.
     */
    @OnClick(R.id.search_action)
    public void clickSearchAction() {
        search(mSearchCategories.getSelectedItem().toString(), mSearchInput.getText().toString());
    }

    @OnClick(R.id.settings)
    public void launchSettings() {
        startActivity(SettingsActivity.makeIntent(this));
    }

    /**
     * Open search result.
     *
     * @param category search category
     * @param query    search query
     */
    public void search(final String category, final String query) {
        final CustomTabsIntent intent = new CustomTabsIntent.Builder()
                .setToolbarColor(new PreferenceApplier(this).getColor())
                .build();
        intent.launchUrl(this, new UrlFactory().make(category, query));
    }

    /**
     * Make launcher intent.
     * @param context
     * @return launcher intent
     */
    public static Intent makeIntent(Context context) {
        final Intent intent = new Intent(context, SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
}
