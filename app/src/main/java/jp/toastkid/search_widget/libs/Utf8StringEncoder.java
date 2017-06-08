package jp.toastkid.search_widget.libs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author toastkidjp
 */
public class Utf8StringEncoder {

    public static String encode(final String query) {
        try {
            return URLEncoder.encode(query, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return query;
    }
}
