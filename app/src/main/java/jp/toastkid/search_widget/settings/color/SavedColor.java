package jp.toastkid.search_widget.settings.color;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

/**
 * @author toastkidjp
 */
@Table
public class SavedColor {

    @PrimaryKey(autoincrement = true)
    public long id;

    @Column
    public  int bgColor;

    @Column
    public  int fontColor;

}
