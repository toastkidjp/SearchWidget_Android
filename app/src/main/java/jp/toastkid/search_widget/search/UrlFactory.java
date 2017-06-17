package jp.toastkid.search_widget.search;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import jp.toastkid.search_widget.R;

/**
 * @author toastkidjp
 */
class UrlFactory {

    Uri make(final String category, final String query) {
        return Uri.parse(SearchCategory.findByCategory(category).make(query));
    }

}
