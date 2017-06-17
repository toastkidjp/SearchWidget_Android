package jp.toastkid.search_widget.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import jp.toastkid.search_widget.BaseActivity;
import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.favorite.FavoriteSearchActivity;
import jp.toastkid.search_widget.favorite.FavoriteSearchService;
import jp.toastkid.search_widget.libs.network.NetworkChecker;
import jp.toastkid.search_widget.libs.preference.PreferenceApplier;
import jp.toastkid.search_widget.search.suggest.SuggestAdapter;
import jp.toastkid.search_widget.search.suggest.SuggestFetcher;
import jp.toastkid.search_widget.settings.SettingsActivity;

/**
 * Search activity.
 *
 * @author toastkidjp
 */
public class SearchActivity extends BaseActivity {

    /** Key of extra. */
    private static final String EXTRA_KEY_FINISH_SOON = "finish_soon";

    /** Background filter. */
    @BindView(R.id.search_background_filter)
    public View mFilter;

    @BindView(R.id.search_toolbar)
    public Toolbar mToolbar;

    /** Search input. */
    @BindView(R.id.search_input)
    public AppCompatEditText mSearchInput;

    /** Search category. */
    @BindView(R.id.search_categories)
    public Spinner mSearchCategories;

    /** Do on click action's background. */
    @BindView(R.id.search_action_background)
    public View mSearchActionBackground;

    /** Do on click action. */
    @BindView(R.id.search_action)
    public TextView mSearchAction;

    @BindView(R.id.search_clear)
    public ImageView mSearchClear;

    /** Suggest list. */
    @BindView(R.id.search_suggests)
    public ListView mSearchSuggests;

    /** Preference applier. */
    private PreferenceApplier mPreferenceApplier;

    /** Search result URL factory. */
    private UrlFactory mUrlFactory;

    /** Suggest Adapter. */
    private SuggestAdapter mSuggestAdapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        mPreferenceApplier = new PreferenceApplier(this);

        initUrlFactory();
        initSuggests();
        initSearchInput();
        initToolbar(mToolbar);
        mToolbar.inflateMenu(R.menu.search_toolbar_menu);

        mFilter.setOnClickListener(v -> close());

        final Intent intent = getIntent();
        if (intent != null && intent.hasExtra(SearchManager.QUERY)) {
            final String category = intent.hasExtra(FavoriteSearchService.EXTRA_KEY_CATEGORY)
                    ? intent.getStringExtra(FavoriteSearchService.EXTRA_KEY_CATEGORY)
                    : SearchCategory.WEB.name();
            search(category, intent.getStringExtra(SearchManager.QUERY));
            if (intent.getBooleanExtra(EXTRA_KEY_FINISH_SOON, false)) {
                finish();
            }
        }

        mSearchClear.setOnClickListener(v -> mSearchInput.setText(""));
    }

    private void initUrlFactory() {
        mUrlFactory = new UrlFactory(this);
        mUrlFactory.initSpinner(mSearchCategories);
    }

    private void initSuggests() {
        mSuggestAdapter = new SuggestAdapter(
                LayoutInflater.from(this),
                mSearchInput,
                suggest -> search(mSearchCategories.getSelectedItem().toString(), suggest)
                );
        mSearchSuggests.setAdapter(mSuggestAdapter);
    }

    private void initSearchInput() {
        mSearchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId != EditorInfo.IME_ACTION_SEARCH) {
                return false;
            }
            search(mSearchCategories.getSelectedItem().toString(), v.getText().toString());
            return true;
        });

        mSearchInput.addTextChangedListener(new TextWatcher() {

            private final SuggestFetcher mFetcher = new SuggestFetcher();

            private final Map<String, List<String>> mCache = new HashMap<>(30);

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // NOP.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (mPreferenceApplier.isDisableSuggest()) {
                    mSuggestAdapter.clear();
                    return;
                }

                final String key = s.toString();
                if (mCache.containsKey(key)) {
                    replaceSuggests(mCache.get(key));
                    return;
                }

                if (NetworkChecker.isNotAvailable(SearchActivity.this)) {
                    return;
                }

                mFetcher.fetchAsync(key, suggests -> {
                    if (suggests == null || suggests.isEmpty()) {
                        Completable.create(e -> {
                            mSearchSuggests.setVisibility(View.GONE);
                            e.onComplete();
                        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
                        return;
                    }
                    replaceSuggests(suggests);
                    mCache.put(key, suggests);
                });
            }

            @Override
            public void afterTextChanged(Editable s) {
                // NOP.
            }
        });
    }

    @Override
    protected boolean clickMenu(final MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.search_toolbar_menu_setting) {
            startActivity(SettingsActivity.makeIntent(this));
        }
        if (itemId == R.id.search_toolbar_menu_favorite) {
            startActivity(FavoriteSearchActivity.makeIntent(this));
        }
        return super.clickMenu(item);
    }

    /**
     * Replace suggests with specified items.
     * @param suggests
     */
    private void replaceSuggests(final List<String> suggests) {
        runOnUiThread(() -> {
            mSearchSuggests.setVisibility(View.VISIBLE);
            mSuggestAdapter.replace(suggests);
            mSuggestAdapter.notifyDataSetChanged();
            mSuggestAdapter.notifyDataSetInvalidated();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final InputMethodManager inputMethodManager
                = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(mSearchInput, 0);
        mFilter.setBackgroundColor(getResources().getColor(R.color.darkgray_scale));
        applyColor();
    }

    private void applyColor() {
        final PreferenceApplier preferenceApplier = mPreferenceApplier;
        final int bgColor = preferenceApplier.getColor();
        final int fontColor = preferenceApplier.getFontColor();
        applyColorToToolbar(mToolbar, bgColor, fontColor);
        mSearchInput.setTextColor(fontColor);
        mSearchInput.setHintTextColor(fontColor);
        mSearchInput.setHighlightColor(fontColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ColorUtils.setAlphaComponent(bgColor, 255));
        }

        mSearchActionBackground.setBackgroundColor(
                ColorUtils.setAlphaComponent(mPreferenceApplier.getColor(), 128));
        mSearchAction.setTextColor(mPreferenceApplier.getFontColor());
        mSearchClear.setColorFilter(mPreferenceApplier.getFontColor());
    }

    /**
     * Close this activity.
     */
    private void close() {
        finish();
    }

    /**
     * Open search result.
     *
     * @param category search category
     * @param query    search query
     */
    private void search(final String category, final String query) {

        final Bundle bundle = new Bundle();
        bundle.putString("category", category);
        bundle.putString("query", query);
        sendLog("search", bundle);

        new SearchIntentLauncher(this)
                .setBackgroundColor(mPreferenceApplier.getColor())
                .setFontColor(mPreferenceApplier.getFontColor())
                .setCategory(category)
                .setQuery(query)
                .invoke();
    }

    @Override
    protected int getTitleId() {
        return R.string.search_action_title;
    }

    @OnClick(R.id.search_action)
    public void clickSearch() {
        search(mSearchCategories.getSelectedItem().toString(), mSearchInput.getText().toString());
    }

    /**
     * Make launcher intent.
     * @param context
     * @return launcher intent
     */
    public static Intent makeIntent(@NonNull final Context context) {
        final Intent intent = new Intent(context, SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    /**
     * Make launcher intent.
     * @param context
     * @return launcher intent
     */
    public static Intent makeShortcutIntent(
            @NonNull final Context context,
            @NonNull final SearchCategory category,
            @NonNull final String query
    ) {
        return makeShortcutIntent(context, category, query, false);
    }

    /**
     * Make launcher intent.
     * @param context
     * @param category
     * @param query
     * @param finishSoon
     * @return launcher intent
     */
    public static Intent makeShortcutIntent(
            @NonNull final Context context,
            @NonNull final SearchCategory category,
            @NonNull final String query,
            final boolean finishSoon
    ) {
        final Intent intent = makeIntent(context);
        intent.putExtra(FavoriteSearchService.EXTRA_KEY_CATEGORY, category.name());
        intent.putExtra(SearchManager.QUERY,   query);
        intent.putExtra(EXTRA_KEY_FINISH_SOON, finishSoon);
        return intent;
    }
}
