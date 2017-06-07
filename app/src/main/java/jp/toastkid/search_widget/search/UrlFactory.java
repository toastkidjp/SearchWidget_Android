package jp.toastkid.search_widget.search;

import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author toastkidjp
 */
class UrlFactory {

    /**
     * URL Generator.
     */
    private interface Generator {
        String generate(final String host, final String query);
    }

    /** Default URL generator */
    private static Generator DEFAULT = (h, q) -> h + encodeQuery(q);

    /**
     * Factories.
     */
    private enum Factory {
        WEB("https://search.yahoo.co.jp/search?p="),
        WIKIPEDIA("https://ja.wikipedia.org/w/index.php?search="),
        TWITTER("https://twitter.com/search?src=typd&q="),
        YOUTUBE("https://www.youtube.com/results?search_query="),
        AOZORA("https://www.google.co.jp/search?as_dt=i&as_sitesearch=www.aozora.gr.jp&as_q=")
        ;

        private String mHost;

        private Generator mGenerator;

        Factory(final String host) {
            this(host, DEFAULT);
        }

        Factory(final String host, final Generator generator) {
            mHost = host;
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

    public Uri make(final String category, final String query) {
        return Uri.parse(Factory.findByCategory(category).make(query));
    }

    private static String encodeQuery(final String query) {
        try {
            return URLEncoder.encode(query, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return query;
    }

}
