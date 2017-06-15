package jp.toastkid.search_widget.search.suggest;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Suggest Web API response fetcher.
 *
 * @author toastkidjp
 */
public class SuggestFetcher {

    /** Suggest Web API. */
    private static final String URL
            = "https://www.google.com/complete/search?&output=toolbar";

    /** HTTP client. */
    private OkHttpClient mClient;

    /**
     * Initialize HTTP client.
     */
    public SuggestFetcher() {
        mClient = new OkHttpClient.Builder()
                .connectTimeout(3L, TimeUnit.SECONDS)
                .readTimeout(3L, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Fetch Web API result asynchronously.
     *
     * @param query
     * @param consumer
     */
    public void fetchAsync(final String query, final Consumer<List<String>> consumer) {
        final Request request = new Request.Builder()
                .url(makeSuggestUrl(query))
                .build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(
                    final Call call,
                    @NonNull final Response response
            ) throws IOException {
                if (response.body() == null) {
                    return;
                }
                try {
                    consumer.accept(new SuggestParser().parse(response.body().string()));
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Make suggest Web API requesting URL.
     * @param query Query
     * @return suggest Web API requesting URL
     */
    @NonNull
    private String makeSuggestUrl(@NonNull final String query) {
        return URL + "&hl=" + Locale.getDefault().getLanguage()
                + "&q=" + Uri.encode(query);
    }
}
