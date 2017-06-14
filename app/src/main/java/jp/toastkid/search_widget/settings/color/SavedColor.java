package jp.toastkid.search_widget.settings.color;

import io.realm.RealmObject;

/**
 * @author toastkidjp
 */
public class SavedColor extends RealmObject {

    private int bgColor;

    private int fontColor;

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }
}
