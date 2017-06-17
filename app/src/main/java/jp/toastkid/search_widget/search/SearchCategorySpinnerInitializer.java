package jp.toastkid.search_widget.search;

import android.content.Context;
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

public class SearchCategorySpinnerInitializer {

    public static void initialize(@NonNull final Spinner spinner) {
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

                final Context context = spinner.getContext();
                final LayoutInflater inflater = LayoutInflater.from(context);
                final View view = inflater.inflate(R.layout.search_category, null);
                final ImageView imageView = (ImageView) view.findViewById(R.id.search_category_image);
                imageView.setImageDrawable(AppCompatResources.getDrawable(context, searchCategory.getIconId()));
                final TextView textView = (TextView) view.findViewById(R.id.search_category_text);
                textView.setText(searchCategory.getId());
                return view;
            }
        };
        spinner.setAdapter(adapter);
    }

}
