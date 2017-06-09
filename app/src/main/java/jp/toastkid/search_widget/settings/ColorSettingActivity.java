package jp.toastkid.search_widget.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.toastkid.search_widget.BaseActivity;
import jp.toastkid.search_widget.R;
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

    @BindView(R.id.settings_color_preview_background)
    public View previewBackground;

    @BindView(R.id.widget_search_text)
    public TextView previewText;

    private PreferenceApplier mPreferenceApplier;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_color);
        ButterKnife.bind(this);

        mPreferenceApplier = new PreferenceApplier(this);

        initPalette();
        initToolbar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyColorToToolbar(toolbar, mPreferenceApplier.getColor(), mPreferenceApplier.getFontColor());
    }

    private void initPalette() {
        bgPalette.addSVBar(bgSvBar);
        bgPalette.addOpacityBar(bgOpacityBar);
        bgPalette.setOnColorChangedListener(c -> previewBackground.setBackgroundColor(c));

        fontPalette.addSVBar(fontSvBar);
        fontPalette.addOpacityBar(fontOpacityBar);
        fontPalette.setOnColorChangedListener(c -> previewText.setTextColor(c));

        bgPalette.setColor(mPreferenceApplier.getColor());
        fontPalette.setColor(mPreferenceApplier.getFontColor());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPreferenceApplier.setColor(bgPalette.getColor());
        mPreferenceApplier.setFontColor(fontPalette.getColor());
        sendBroadcast(new Intent("UPDATE_WIDGET"));
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
