package jp.toastkid.search_widget.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.TintContextWrapper;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
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
import jp.toastkid.search_widget.libs.Logger;
import jp.toastkid.search_widget.libs.network.NetworkChecker;
import jp.toastkid.search_widget.libs.preference.PreferenceApplier;
import jp.toastkid.search_widget.search.suggest.SuggestFetcher;
import jp.toastkid.search_widget.settings.SettingsActivity;

/**
 * Search activity.
 *
 * @author toastkidjp
 */
public class SearchActivity extends BaseActivity {



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

    /** Do on click action. */
    @BindView(R.id.search_action)
    public TextView mSearchAction;

    @BindView(R.id.search_clear)
    public ImageView mSearchClear;

    /** Suggest list. */
    @BindView(R.id.search_suggests)
    public ListView mSearchSuggests;

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
            search("WEB", intent.getStringExtra(SearchManager.QUERY));
        }

        mSearchAction.setOnClickListener(v -> search(
                mSearchCategories.getSelectedItem().toString(),
                mSearchInput.getText().toString()
                )
        );

        mSearchClear.setOnClickListener(v -> mSearchInput.setText(""));

    }

    private void initUrlFactory() {
        mUrlFactory = new UrlFactory(this);
        mUrlFactory.initSpinner(mSearchCategories);
    }

    private void initSuggests() {
        mSuggestAdapter = new SuggestAdapter(LayoutInflater.from(this));
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
                    Logger.i("suggest: use cache " + s);
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
                    Logger.i("suggest: fetch " + s + " " + suggests);
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

        mSearchAction.setBackgroundColor(
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
    public void search(final String category, final String query) {
        final CustomTabsIntent intent = new CustomTabsIntent.Builder()
                .setToolbarColor(mPreferenceApplier.getColor())
                .setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_back))
                .setSecondaryToolbarColor(mPreferenceApplier.getFontColor())
                .setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
                .setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .build();
        intent.launchUrl(this, mUrlFactory.make(category, query));
    }

    @Override
    protected int getTitleId() {
        return R.string.search_action_title;
    }

    private class SuggestAdapter extends BaseAdapter {

         private final LayoutInflater mInflater;

         private final List<String> mSuggests;

         SuggestAdapter(final LayoutInflater inflater) {
             mInflater = inflater;
             mSuggests = new ArrayList<>();
         }

         void clear() {
             mSuggests.clear();
             notifyDataSetChanged();
         }

         void replace(final List<String> strs) {
             mSuggests.clear();
             mSuggests.addAll(strs);
         }

         @Override
         public int getCount() {
             return mSuggests.size();
         }

         @Override
         public Object getItem(int position) {
             return mSuggests.get(position);
         }

         @Override
         public long getItemId(int position) {
             return mSuggests.get(position).hashCode();
         }

         @Override
         public View getView(int position, View convertView, ViewGroup parent) {
             final View inflate = mInflater.inflate(R.layout.search_suggest, null);
             final TextView textView = (TextView) inflate.findViewById(R.id.search_suggest_text);
             final String suggest = mSuggests.get(position);
             textView.setText(suggest);
             inflate.findViewById(R.id.search_suggest_add).setOnClickListener(v -> {
                 mSearchInput.setText(suggest + " ");
                 mSearchInput.setSelection(mSearchInput.getText().toString().length());
             });
             inflate.setOnClickListener(
                     v -> {
                         mSearchInput.setText(suggest);
                         search(mSearchCategories.getSelectedItem().toString(), suggest);
                     });
             return inflate;
         }
    }

    /**
     * Make launcher intent.
     * @param context
     * @return launcher intent
     */
    public static Intent makeIntent(final Context context) {
        final Intent intent = new Intent(context, SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
}
