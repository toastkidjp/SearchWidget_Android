package jp.toastkid.search_widget.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.BaseAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.toastkid.search_widget.BaseActivity;
import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.libs.preference.PreferenceApplier;

/**
 * Settings activity.
 *
 * @author toastkidjp
 */
public class SettingsActivity extends BaseActivity {

    @BindView(R.id.settings_toolbar)
    public Toolbar toolbar;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initToolbar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final PreferenceApplier preferenceApplier = new PreferenceApplier(this);
        applyColorToToolbar(toolbar, preferenceApplier.getColor(), preferenceApplier.getFontColor());
    }

    @OnClick(R.id.settings_color)
    public void color() {
        startActivity(ColorSettingActivity.makeIntent(this));
    }

    @OnClick(R.id.settings_licenses)
    public void license() {
        new LicenseViewer(this).invoke();
    }

    @Override
    protected int getTitleId() {
        return R.string.action_settings;
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