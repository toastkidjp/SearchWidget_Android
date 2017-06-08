package jp.toastkid.search_widget.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.slider.AlphaSlider;
import com.flask.colorpicker.slider.LightnessSlider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.libs.preference.PreferenceApplier;

/**
 * Settings activity.
 *
 * @author toastkidjp
 */
public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.color_picker_view)
    public ColorPickerView palette;

    @BindView(R.id.lightness_slider)
    public LightnessSlider lightnessSlider;

    @BindView(R.id.alpha_slider)
    public AlphaSlider alphaSlider;

    @BindView(R.id.settings_toolbar)
    public Toolbar toolbar;

    private PreferenceApplier mPreferenceApplier;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        mPreferenceApplier = new PreferenceApplier(this);
        final int bgColor = mPreferenceApplier.getColor();
        palette.setColor(bgColor, false);
        initToolbar(bgColor);
    }

    /**
     * Initialize Toolbar.
     * @param bgColor Toolbar's background color.
     */
    private void initToolbar(final int bgColor) {
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(R.string.action_settings);
        toolbar.setBackgroundColor(bgColor);
        toolbar.inflateMenu(R.menu.settings_toolbar_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.settings_toolbar_menu_exit) {
                finish();
            }
            return true;
        });
    }

    @OnClick(R.id.settings_licenses)
    public void license() {
        new LicenseViewer(this).invoke();
    }

    @Override
    protected void onStop() {
        super.onStop();
        int selectedColor = palette.getSelectedColor();
        mPreferenceApplier.setColor(selectedColor);
        sendBroadcast(new Intent("UPDATE_WIDGET"));
    }

    /**
     * Make launcher intent.
     * @param context
     * @return
     */
    @NonNull
    public static Intent makeIntent(final Context context) {
        final Intent intent = new Intent(context, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
}