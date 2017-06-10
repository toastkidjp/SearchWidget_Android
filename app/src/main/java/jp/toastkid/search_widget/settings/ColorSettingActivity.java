package jp.toastkid.search_widget.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    private PreferenceApplier mPreferenceApplier;

    private int initialBgColor;

    private int initialFontColor;

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

        setSavedColor();
    }

    private void setSavedColor() {
        bgPalette.setColor(initialBgColor);
        fontPalette.setColor(initialFontColor);
        applyColorToToolbar(toolbar, initialBgColor, initialFontColor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        applyColorToToolbar(toolbar, mPreferenceApplier.getColor(), mPreferenceApplier.getFontColor());
    }

    @OnClick(R.id.settings_color_ok)
    public void ok() {
        mPreferenceApplier.setColor(bgPalette.getColor());
        mPreferenceApplier.setFontColor(fontPalette.getColor());
        Updater.update(this);
        refresh();
        Toaster.snackShort(toolbar, R.string.settings_color_done_commit, bgPalette.getColor());
    }

    @OnClick(R.id.settings_color_prev)
    public void reset() {
        setSavedColor();
        Toaster.snackShort(toolbar, R.string.settings_color_done_reset, bgPalette.getColor());
    }

    @Override
    protected int getTitleId() {
        return R.string.settings_color_text;
    }

    public static Intent makeIntent(final Context context) {
        final Intent intent = new Intent(context, ColorSettingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

}
