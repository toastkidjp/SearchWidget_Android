package jp.toastkid.search_widget.search;

import jp.toastkid.search_widget.R;
import jp.toastkid.search_widget.libs.Utf8StringEncoder;

/**
 * Web search category.
 *
 * @author toastkidjp
 */
enum SearchCategory {

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
    YOUTUBE(R.string.search_category_youtube,
            R.drawable.ic_video,
            "https://www.youtube.com/results?search_query="
    ),
    WIKIPEDIA(R.string.search_category_wikipedia,
            R.drawable.ic_library_books,
            "https://ja.wikipedia.org/w/index.php?search="
    ),
    TWITTER(R.string.search_category_twitter,
            R.drawable.ic_sns,
            "https://twitter.com/search?src=typd&q="
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
    TECHNICAL(R.string.search_category_technical_qa,
            R.drawable.ic_technical_qa,
            "https://stackoverflow.com/search?q="
    )
    ;

    /**
     * URL Generator.
     */
    private interface Generator {
        String generate(final String host, final String query);
    }

    private final int mId;

    private final int mIconId;

    private String mHost;

    private Generator mGenerator;

    SearchCategory(final int id, final int iconId, final String host) {
        this(id, iconId, host, (h, q) -> h + Utf8StringEncoder.encode(q));
    }

    SearchCategory(final int id, final int iconId, final String host, final Generator generator) {
        mId         = id;
        mIconId     = iconId;
        mHost       = host;
        mGenerator = generator;
    }

    public String make(final String query) {
        return mGenerator.generate(mHost, query);
    }

    public int getId() {
        return mId;
    }

    public int getIconId() {
        return mIconId;
    }

    public static SearchCategory findByCategory(final String category) {
        for (final SearchCategory f : SearchCategory.values()) {
            if (f.name().equals(category.toUpperCase())) {
                return f;
            }
        }
        return WEB;
    }
}