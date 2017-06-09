package jp.toastkid.search_widget;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.toastkid.search_widget.libs.preference.PreferenceApplier;

/**
 * @author toastkidjp
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Initialize Toolbar.
     * @param toolbar Toolbar
     */
    protected void initToolbar(final Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitle(getTitleId());
        toolbar.inflateMenu(R.menu.settings_toolbar_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.settings_toolbar_menu_exit) {
                finish();
            }
            return true;
        });
    }

    /**
     * Apply color to Toolbar.
     * @param toolbar Toolbar
     * @param bgColor Toolbar's background color
     * @param fontColor Toolbar's font color
     */
    protected void applyColorToToolbar(final Toolbar toolbar, final int bgColor, final int fontColor) {
        toolbar.setTitleTextColor(fontColor);
        toolbar.setBackgroundColor(bgColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ColorUtils.setAlphaComponent(bgColor, 255));
        }
    }

    protected abstract int getTitleId();
}
