package jp.toastkid.search_widget.libs.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

/**
 * @author toastkidjp
 */
public class PreferenceApplier {

    private enum Key {
        BG_COLOR, FONT_COLOR;
    }

    private SharedPreferences mPreferences;

    public PreferenceApplier(final Context c) {
        mPreferences = c.getSharedPreferences(getClass().getCanonicalName(), Context.MODE_PRIVATE);
    }

    public int getColor() {
        return mPreferences.getInt(Key.BG_COLOR.name(), Color.argb(128, 0, 66, 128));
    }

    public void setColor(final int color) {
        mPreferences.edit().putInt(Key.BG_COLOR.name(), color).apply();
    }
}
