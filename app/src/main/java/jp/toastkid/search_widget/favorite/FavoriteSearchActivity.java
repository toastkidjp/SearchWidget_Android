package jp.toastkid.search_widget.favorite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.github.gfx.android.orma.Relation;
import com.github.gfx.android.orma.widget.OrmaRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.schedulers.Schedulers;
import jp.toastkid.search_widget.BaseActivity;
import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.libs.Toaster;
import jp.toastkid.search_widget.libs.preference.PreferenceApplier;
import jp.toastkid.search_widget.search.SearchActivity;
import jp.toastkid.search_widget.search.SearchCategory;

/**
 * @author toastkidjp
 */
public class FavoriteSearchActivity extends BaseActivity {

    @BindView(R.id.favorite_toolbar)
    public Toolbar toolbar;

    @BindView(R.id.favorite_search_view)
    public RecyclerView favSearchsView;

    private PreferenceApplier preferenceApplier;

    private OrmaRecyclerViewAdapter<FavoriteSearch, FavoriteSearchHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_search);
        ButterKnife.bind(this);

        preferenceApplier = new PreferenceApplier(this);

        initToolbar(toolbar);
        toolbar.inflateMenu(R.menu.favorite_toolbar_menu);
        initFavSearchsView();
    }

    private void initFavSearchsView() {
        adapter = new Adapter(this, DbInitter.get(this).relationOfFavoriteSearch());
        favSearchsView.setAdapter(adapter);
        favSearchsView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void bindViews(final FavoriteSearchHolder holder, final FavoriteSearch favoriteSearch) {
        final SearchCategory category = SearchCategory.findByCategory(favoriteSearch.category);
        holder.setImageId(category.getIconId());

        final String query = favoriteSearch.query;
        holder.setText(query);

        holder.itemView.setOnClickListener(v -> startSearch(category, query));

        holder.removeView.setOnClickListener(v -> {
            adapter.removeItemAsMaybe(favoriteSearch).subscribeOn(Schedulers.io()).subscribe();
            Toaster.snackShort(holder.imageView, R.string.settings_color_delete, preferenceApplier.getColor());
        });
    }

    /**
     * Start search action.
     * @param category Search category
     * @param query    Search query
     */
    private void startSearch(final SearchCategory category, final String query) {
        startActivity(SearchActivity.makeShortcutIntent(this, category, query, true));
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyColorToToolbar(toolbar, preferenceApplier.getColor(), preferenceApplier.getFontColor());
    }

    @Override
    protected boolean clickMenu(final MenuItem item) {
        final int itemId = item.getItemId();
        if (itemId == R.id.favorite_toolbar_menu_clear) {
            new Clear(toolbar, adapter.getRelation().deleter()).invoke();
        }
        if (itemId == R.id.favorite_toolbar_menu_add) {
            invokeAddition();
        }
        return super.clickMenu(item);
    }

    @OnClick(R.id.favorite_search_addition)
    public void add() {
        invokeAddition();
    }

    private void invokeAddition() {
        new Addition(toolbar).invoke();
    }

    @Override
    protected int getTitleId() {
        return R.string.title_favorite_search;
    }

    private class Adapter extends OrmaRecyclerViewAdapter<FavoriteSearch, FavoriteSearchHolder> {

        Adapter(@NonNull Context context, @NonNull Relation<FavoriteSearch, ?> relation) {
            super(context, relation);
        }

        @Override
        public FavoriteSearchHolder onCreateViewHolder(
                final ViewGroup parent,
                final int viewType
        ) {
            final LayoutInflater inflater = LayoutInflater.from(FavoriteSearchActivity.this);
            return new FavoriteSearchHolder(inflater.inflate(R.layout.favorite_search_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final FavoriteSearchHolder holder, final int position) {
            bindViews(holder, adapter.getRelation().get(position));
        }

        @Override
        public int getItemCount() {
            return adapter.getRelation().count();
        }
    }

    /**
     * Make launcher intent.
     * @param context
     * @return {@link FavoriteSearchActivity} launcher intent
     */
    public static Intent makeIntent(final Context context) {
        final Intent intent = new Intent(context, FavoriteSearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
}
