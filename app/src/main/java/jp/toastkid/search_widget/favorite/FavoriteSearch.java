package jp.toastkid.search_widget.favorite;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

/**
 * @author toastkidjp
 */
@Table
public class FavoriteSearch {

    @PrimaryKey(autoincrement = true)
    public long id;

    @Column
    public String category;

    @Column
    public String query;
}
