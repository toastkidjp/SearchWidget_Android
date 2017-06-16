package jp.toastkid.search_widget.settings.color;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import jp.toastkid.search_widget.BaseActivity;
import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.appwidget.Updater;
import jp.toastkid.search_widget.libs.Toaster;
import jp.toastkid.search_widget.libs.preference.PreferenceApplier;

/**
 * Color setting activity.
 *
 * @author toastkidjp
 */
public class ColorSettingActivity extends BaseActivity {

    @BindView(R.id.settings_color_toolbar)
    public Toolbar toolbar;

    @BindView(R.id.background_palette)
    public ColorPicker bgPalette;

    @BindView(R.id.background_svbar)
    public SVBar bgSvBar;

    @BindView(R.id.background_opacitybar)
    public OpacityBar bgOpacityBar;

    @BindView(R.id.font_palette)
    public ColorPicker fontPalette;

    @BindView(R.id.font_svbar)
    public SVBar fontSvBar;

    @BindView(R.id.font_opacitybar)
    public OpacityBar fontOpacityBar;

    @BindView(R.id.settings_color_ok)
    public Button ok;

    @BindView(R.id.settings_color_prev)
    public Button prev;

    @BindView(R.id.saved_colors)
    public RecyclerView savedColorsView;

    private int initialBgColor;

    private int initialFontColor;

    private PreferenceApplier mPreferenceApplier;

    private RealmResults<SavedColor> savedColors;

    private RecyclerView.Adapter<SavedColorHolder> adapter;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_color);
        ButterKnife.bind(this);

        mPreferenceApplier = new PreferenceApplier(this);

        initialFontColor = mPreferenceApplier.getFontColor();
        prev.setTextColor(initialFontColor);

        initialBgColor = mPreferenceApplier.getColor();
        prev.setBackgroundColor(initialBgColor);

        initPalette();
        initToolbar(toolbar);
        initSavedColors();
    }

    private void initPalette() {
        bgPalette.addSVBar(bgSvBar);
        bgPalette.addOpacityBar(bgOpacityBar);
        bgPalette.setOnColorChangedListener(c -> {
            toolbar.setBackgroundColor(c);
            ok.setBackgroundColor(c);
        });

        fontPalette.addSVBar(fontSvBar);
        fontPalette.addOpacityBar(fontOpacityBar);
        fontPalette.setOnColorChangedListener(c -> {
            toolbar.setTitleTextColor(c);
            ok.setTextColor(c);
        });

        setPreviousColor();
    }

    private void setPreviousColor() {
        bgPalette.setColor(initialBgColor);
        fontPalette.setColor(initialFontColor);
        applyColorToToolbar(toolbar, initialBgColor, initialFontColor);
    }

    private void initSavedColors() {

        adapter = new RecyclerView.Adapter<SavedColorHolder>() {
            @Override
            public SavedColorHolder onCreateViewHolder(
                    final ViewGroup parent,
                    final int viewType
            ) {
                final LayoutInflater inflater = LayoutInflater.from(ColorSettingActivity.this);
                return new SavedColorHolder(inflater.inflate(R.layout.saved_color, parent, false));
            }

            @Override
            public void onBindViewHolder(final SavedColorHolder holder, final int position) {
                makeSavedColorView(holder.textView, savedColors.get(position));
            }

            @Override
            public int getItemCount() {
                return savedColors.size();
            }
        };
        savedColorsView.setAdapter(adapter);
        savedColorsView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Realm.init(this);
        savedColors = Realm.getDefaultInstance().where(SavedColor.class).findAll();
        if (savedColors.size() == 0) {
            return;
        }
        savedColors.addChangeListener(r -> adapter.notifyDataSetChanged());
        adapter.notifyDataSetChanged();
    }

    private void makeSavedColorView(final Button button, final SavedColor color) {
        Colors.setSaved(button, color);
        button.setOnClickListener(v -> commitNewColor(color.getBgColor(), color.getFontColor()));
        button.setOnLongClickListener(v -> {
            final int index = savedColors.indexOf(color);
            Realm.getDefaultInstance().executeTransaction(realm -> color.deleteFromRealm());
            adapter.notifyItemRemoved(index);
            Toaster.snackShort(button, R.string.settings_color_delete, mPreferenceApplier.getColor());
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        final int bgColor   = mPreferenceApplier.getColor();
        final int fontColor = mPreferenceApplier.getFontColor();
        applyColorToToolbar(toolbar, bgColor, fontColor);
        Colors.setBgAndText(ok, bgColor, fontColor);
    }

    @OnClick(R.id.settings_color_ok)
    public void ok() {
        final int bgColor   = bgPalette.getColor();
        final int fontColor = fontPalette.getColor();

        commitNewColor(bgColor, fontColor);

        final Bundle bundle = new Bundle();
        bundle.putString("bg",   Integer.toHexString(bgColor));
        bundle.putString("font", Integer.toHexString(fontColor));
        sendLog("color_set", bundle);

        Colors.insertColor(Realm.getDefaultInstance(), bgColor, fontColor);
        adapter.notifyItemInserted(savedColors.size());
    }

    private void commitNewColor(final int bgColor, final int fontColor) {
        mPreferenceApplier.setColor(bgColor);

        mPreferenceApplier.setFontColor(fontColor);

        Updater.update(this);
        refresh();
        Toaster.snackShort(toolbar, R.string.settings_color_done_commit, bgColor);
    }

    @OnClick(R.id.settings_color_prev)
    public void reset() {
        setPreviousColor();
        Toaster.snackShort(toolbar, R.string.settings_color_done_reset, bgPalette.getColor());
    }

    @OnClick(R.id.clear_saved_color)
    public void clearSavedColor() {
        Colors.showClearColorsDialog(this, toolbar);
    }

    @Override
    protected int getTitleId() {
        return R.string.title_settings_color;
    }

    /**
     * Make launcher intent.
     * @param context Context
     * @return {@link Intent}
     */
    public static Intent makeIntent(final Context context) {
        final Intent intent = new Intent(context, ColorSettingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

}
