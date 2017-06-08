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

    /**
     * URL Generator.
     */
    private interface Generator {
        String generate(final String host, final String query);
    }

    /** Default URL generator */
    private static Generator DEFAULT = (h, q) -> h + Utf8StringEncoder.encode(q);

    /**
     * Factories.
     */
    private enum Factory {
        WIKIPEDIA(R.string.search_category_wikipedia,
                R.drawable.ic_search_black,
                "https://ja.wikipedia.org/w/index.php?search="
        ),
        TWITTER(R.string.search_category_twitter,
                R.drawable.ic_search_black,
                "https://twitter.com/search?src=typd&q="
        ),
        YOUTUBE(R.string.search_category_youtube,
                R.drawable.ic_youtube_searched,
                "https://www.youtube.com/results?search_query="
        ),
        WEB(R.string.search_category_web,
                R.drawable.ic_world,
                "https://duckduckgo.com/%s?ia=web",
                String::format
        ),
        IMAGE(R.string.search_category_image,
                R.drawable.ic_image_search,
                "https://duckduckgo.com/%s?ia=images&iax=1",
                String::format
        ),
        VIDEO(R.string.search_category_video,
                R.drawable.ic_video,
                "https://duckduckgo.com/%s?ia=videos&iax=1",
                String::format
        ),
        MAP(R.string.search_category_map,
                R.drawable.ic_map,
                "https://www.google.co.jp/maps/place/"
        ),
        APPS(R.string.search_category_apps,
                R.drawable.ic_android_app,
                "https://play.google.com/store/search?q="
        ),
        AMAZON(R.string.search_category_shopping,
                R.drawable.ic_shopping,
                "https://www.amazon.co.jp/s/ref=nb_sb_noss?field-keywords="
        ),
        AOZORA(R.string.search_category_free_book,
                R.drawable.ic_library_books,
                "https://www.google.co.jp/search?as_dt=i&as_sitesearch=www.aozora.gr.jp&as_q="
        );

        private final int mId;

        private final int mIconId;

        private String mHost;

        private Generator mGenerator;

        Factory(final int id, final int iconId, final String host) {
            this(id, iconId, host, DEFAULT);
        }

        Factory(final int id, final int iconId, final String host, final Generator generator) {
            mId         = id;
            mIconId     = iconId;
            mHost       = host;
            mGenerator = generator;
        }

        public String make(final String query) {
            return mGenerator.generate(mHost, query);
        }

        public static Factory findByCategory(final String category) {
            for (final Factory f : Factory.values()) {
                if (f.name().equals(category.toUpperCase())) {
                    return f;
                }
            }
            return WEB;
        }
    }

    public void initSpinner(@NonNull final Spinner spinner) {
        final BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return Factory.values().length;
            }

            @Override
            public Object getItem(int position) {
                return Factory.values()[position];
            }

            @Override
            public long getItemId(int position) {
                return Factory.values()[position].mId;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Factory factory = Factory.values()[position];

                final LayoutInflater inflater = LayoutInflater.from(mContext);
                final View view = inflater.inflate(R.layout.search_category, null);
                final ImageView imageView = (ImageView) view.findViewById(R.id.search_category_image);
                imageView.setImageDrawable(AppCompatResources.getDrawable(mContext, factory.mIconId));
                final TextView textView = (TextView) view.findViewById(R.id.search_category_text);
                textView.setText(factory.mId);
                return view;
            }
        };
        spinner.setAdapter(adapter);
    }

    public Uri make(final String category, final String query) {
        return Uri.parse(Factory.findByCategory(category).make(query));
    }

}
