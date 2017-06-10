package jp.toastkid.search_widget.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.toastkid.search_widget.BaseActivity;
import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.appwidget.Updater;
import jp.toastkid.search_widget.libs.preference.PreferenceApplier;

/**
 * Settings activity.
 *
 * @author toastkidjp
 */
public class SettingsActivity extends BaseActivity {

    @BindView(R.id.settings_toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.settings_enable_suggest_text)
    public TextView mEnableSuggestText;

    @BindView(R.id.settings_enable_suggest_check)
    public CheckBox mEnableSuggestCheck;

    private PreferenceApplier mPreferenceApplier;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        mPreferenceApplier = new PreferenceApplier(this);
        initToolbar(mToolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        applyColorToToolbar(mToolbar, mPreferenceApplier.getColor(), mPreferenceApplier.getFontColor());
        mEnableSuggestCheck.setChecked(mPreferenceApplier.isEnableSuggest());
    }

    @OnClick(R.id.settings_color)
    public void color() {
        startActivity(ColorSettingActivity.makeIntent(this));
    }

    @OnClick(R.id.settings_enable_suggest)
    public void switchSuggest() {
        mPreferenceApplier.switchEnableSuggest();
        mEnableSuggestCheck.setChecked(mPreferenceApplier.isEnableSuggest());
    }

    @OnClick(R.id.settings_clear)
    public void clearSettings() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_clear)
                .setMessage(Html.fromHtml(getString(R.string.confirm_clear_all_settings)))
                .setCancelable(true)
                .setNegativeButton(R.string.cancel, (d, i) -> d.cancel())
                .setPositiveButton(R.string.ok,      (d, i) -> {
                    mPreferenceApplier.clear();
                    Updater.update(this);
                    refresh();
                    Snackbar.make(mToolbar, R.string.done_clear, Snackbar.LENGTH_SHORT).show();
                })
                .show();
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