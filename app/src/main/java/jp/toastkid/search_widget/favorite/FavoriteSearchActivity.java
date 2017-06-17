package jp.toastkid.search_widget.favorite;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
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

    private RealmResults<FavoriteSearch> savedSearchs;

    private RecyclerView.Adapter<FavoriteSearchHolder> adapter;

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
        adapter = new RecyclerView.Adapter<FavoriteSearchHolder>() {
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
                bindViews(holder, savedSearchs.get(position));
            }

            @Override
            public int getItemCount() {
                return savedSearchs.size();
            }
        };
        favSearchsView.setAdapter(adapter);
        favSearchsView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Realm.init(this);
        savedSearchs = Realm.getDefaultInstance().where(FavoriteSearch.class).findAll();
        if (savedSearchs.size() == 0) {
            return;
        }
        savedSearchs.addChangeListener(r -> adapter.notifyDataSetChanged());
        adapter.notifyDataSetChanged();
    }

    private void bindViews(final FavoriteSearchHolder holder, final FavoriteSearch favoriteSearch) {
        final SearchCategory category = SearchCategory.findByCategory(favoriteSearch.getCategory());
        holder.setImageId(category.getIconId());

        final String query = favoriteSearch.getQuery();
        holder.setText(query);

        holder.itemView.setOnClickListener(v -> startSearch(category, query));

        holder.itemView.setOnLongClickListener(v -> {
            final int index = savedSearchs.indexOf(favoriteSearch);
            Realm.getDefaultInstance().executeTransaction(realm -> favoriteSearch.deleteFromRealm());
            adapter.notifyItemRemoved(index);
            Toaster.snackShort(holder.imageView, R.string.settings_color_delete, preferenceApplier.getColor());
            return true;
        });
    }

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
            new AlertDialog.Builder(this)
                    .setTitle(R.string.favorite_clear_all_title)
                    .setMessage(Html.fromHtml(getString(R.string.confirm_clear_all_settings)))
                    .setCancelable(true)
                    .setNegativeButton(R.string.cancel, (d, i) -> d.cancel())
                    .setPositiveButton(R.string.ok,     (d, i) -> {
                        Realm.getDefaultInstance().executeTransaction(realm -> realm.delete(FavoriteSearch.class));
                        Toaster.snackShort(toolbar, R.string.settings_color_delete, ((ColorDrawable) toolbar.getBackground()).getColor());
                        d.dismiss();
                    })
                    .show();
        }
        return super.clickMenu(item);
    }

    @Override
    protected int getTitleId() {
        return R.string.title_favorite_search;
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
