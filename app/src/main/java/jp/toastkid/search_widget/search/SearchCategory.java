package jp.toastkid.search_widget.search;

import android.net.Uri;

import java.util.Locale;

import jp.toastkid.search_widget.R;

/**
 * Web search category.
 *
 * @author toastkidjp
 */
public enum SearchCategory {

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
            "https://%s.wikipedia.org/w/index.php?search=",
            (h, q) -> String.format(h, Locale.getDefault().getLanguage()) + Uri.encode(q)
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
            "https://www.amazon.co.jp/s/ref=nb_sb_noss?field-keywords=",
            (h, q) -> {
                if (Locale.JAPANESE.getLanguage().equals(Locale.getDefault().getLanguage())) {
                    return h + Uri.encode(q);
                }
                return "https://www.amazon.com/s/ref=nb_sb_noss?field-keywords=" + Uri.encode(q);
            }
    ),
    TECHNICAL_QA(R.string.search_category_technical_qa,
            R.drawable.ic_technical_qa,
            "https://stackoverflow.com/search?q="
    ),
    TECHNOLOGY(R.string.search_category_technology,
              R.drawable.ic_technology,
            "http://jp.techcrunch.com/search/",
            (h, q) -> {
                if (Locale.JAPANESE.getLanguage().equals(Locale.getDefault().getLanguage())) {
                    return h + Uri.encode(q);
                }
                return "https://techcrunch.com/search/" + Uri.encode(q);
            }
    ),
    GITHUB(R.string.search_category_github,
            R.drawable.ic_github,
            "https://github.com/search?utf8=%E2%9C%93&type=&q="
    ),
    MVNREPOSITORY(R.string.search_category_mvnrepository,
            R.drawable.ic_mvn,
            "https://mvnrepository.com/search?q="
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
        this(id, iconId, host, (h, q) -> h + Uri.encode(q));
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