package jp.toastkid.search_widget.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.libs.Logger;
import jp.toastkid.search_widget.libs.preference.PreferenceApplier;
import jp.toastkid.search_widget.search.suggest.SuggetFetcher;
import jp.toastkid.search_widget.settings.SettingsActivity;

/**
 * Search activity.
 *
 * @author toastkidjp
 */
public class SearchActivity extends AppCompatActivity {

    /** Background filter. */
    @BindView(R.id.search_background)
    public View mFilter;

    /** Searchbox background. */
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

    @BindView(R.id.search_suggests)
    public ListView mSearchSuggests;

    private UrlFactory mUrlFactory;

    private SuggestAdapter mSuggestAdapter;

    private Map<String, List<String>> mCache;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        mUrlFactory = new UrlFactory(this);
        mUrlFactory.initSpinner(mSearchCategories);

        mSearchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId != EditorInfo.IME_ACTION_SEARCH) {
                return false;
            }
            search(mSearchCategories.getSelectedItem().toString(), v.getText().toString());
            return true;
        });

        mSuggestAdapter = new SuggestAdapter(LayoutInflater.from(this));
        mSearchSuggests.setAdapter(mSuggestAdapter);

        mCache = new HashMap<>(30);
        final SuggetFetcher fetcher = new SuggetFetcher();
        mSearchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // NOP.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                final String key = s.toString();
                if (mCache.containsKey(key)) {
                    Logger.i("suggest: use cache " + s);
                    replaceSuggests(mCache.get(key));
                    return;
                }

                fetcher.fetchAsync(key, suggests -> {
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
        mBackground.setBackgroundColor(new PreferenceApplier(this).getColor());
        mSearchClose.setOnClickListener(v -> close());
        mFilter.setOnClickListener(v -> close());
    }

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
    }

    private void close() {
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
        intent.launchUrl(this, mUrlFactory.make(category, query));
    }

     private class SuggestAdapter extends BaseAdapter {

         private final LayoutInflater mInflater;
         private final List<String> mSuggests;

         SuggestAdapter(final LayoutInflater inflater) {
             mInflater = inflater;
             mSuggests = new ArrayList<>();
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
    public static Intent makeIntent(Context context) {
        final Intent intent = new Intent(context, SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
}
