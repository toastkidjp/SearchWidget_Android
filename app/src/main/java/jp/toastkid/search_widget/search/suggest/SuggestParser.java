package jp.toastkid.search_widget.search.suggest;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author toastkidjp
 */
class SuggestParser {

    /** For extracting suggested word. */
    private static final Pattern PATTERN
            = Pattern.compile("<suggestion data=\"(.+?)\"/>", Pattern.DOTALL);

    /**
     * Parse response xml.
     * @param response
     * @return suggest words
     */
    List<String> parse(@NonNull final String response) {
        final String[] split = response.split("</CompleteSuggestion>");
        final List<String> suggests = new ArrayList<>(split.length);
        for (final String line : split) {
            final Matcher matcher = PATTERN.matcher(line);
            if (matcher.find()) {
                suggests.add(matcher.group(1));
            }
        }
        return suggests;
    }
}
