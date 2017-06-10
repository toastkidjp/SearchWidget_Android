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
        return mPreferences.getInt(Key.BG_COLOR.name(), Color.argb(128, 0, 128, 66));
    }

    public void setColor(final int color) {
        mPreferences.edit().putInt(Key.BG_COLOR.name(), color).apply();
    }

    public int getFontColor() {
        return mPreferences.getInt(Key.FONT_COLOR.name(), Color.WHITE);
    }

    public void setFontColor(final int color) {
        mPreferences.edit().putInt(Key.FONT_COLOR.name(), color).apply();
    }

    public void clear() {
        mPreferences.edit().clear().apply();
    }

}
