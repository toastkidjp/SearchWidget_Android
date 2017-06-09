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
import jp.toastkid.search_widget.libs.Utf8StringEncoder;

/**
 * @author toastkidjp
 */
class UrlFactory {

    private Context mContext;

    UrlFactory(@NonNull final Context context) {
        mContext = context;
    }

    void initSpinner(@NonNull final Spinner spinner) {
        final BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return SearchCategory.values().length;
            }

            @Override
            public Object getItem(int position) {
                return SearchCategory.values()[position];
            }

            @Override
            public long getItemId(int position) {
                return SearchCategory.values()[position].getId();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final SearchCategory searchCategory = SearchCategory.values()[position];

                final LayoutInflater inflater = LayoutInflater.from(mContext);
                final View view = inflater.inflate(R.layout.search_category, null);
                final ImageView imageView = (ImageView) view.findViewById(R.id.search_category_image);
                imageView.setImageDrawable(AppCompatResources.getDrawable(mContext, searchCategory.getIconId()));
                final TextView textView = (TextView) view.findViewById(R.id.search_category_text);
                textView.setText(searchCategory.getId());
                return view;
            }
        };
        spinner.setAdapter(adapter);
    }

    Uri make(final String category, final String query) {
        return Uri.parse(SearchCategory.findByCategory(category).make(query));
    }

}
