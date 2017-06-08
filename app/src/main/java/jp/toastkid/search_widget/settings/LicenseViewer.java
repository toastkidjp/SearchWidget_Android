package jp.toastkid.search_widget.settings;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.toastkid.search_widget.R;
import okio.Okio;

/**
 * License files viewer.
 *
 * @author toastkidjp
 */
class LicenseViewer {

    public static final String DIRECTORY_OF_LICENSES = "licenses";
    /** For using get assets and show dialog.  */
    private Context mContext;

    /**
     * Initialize with context.
     * @param context
     */
    LicenseViewer(@NonNull final Context context) {
        mContext = context;
    }

    /**
     * Invoke viewer.
     */
    void invoke() {
        try {
            final AssetManager assets = mContext.getAssets();
            final String[] licenseFiles = assets.list(DIRECTORY_OF_LICENSES);
            final Map<String, String> licenseMap = new LinkedHashMap<>(licenseFiles.length);
            for (final String fileName : licenseFiles) {
                final InputStream stream = assets.open(DIRECTORY_OF_LICENSES + "/" + fileName);
                licenseMap.put(fileName.substring(0, fileName.lastIndexOf(".")), readUtf8(stream));
                stream.close();
            }
            final String[] items = licenseMap.keySet().toArray(new String[]{});
            new AlertDialog.Builder(mContext).setTitle(R.string.title_licenses)
                    .setItems(items, (d, index) ->
                            new AlertDialog.Builder(mContext)
                                    .setTitle(items[index])
                                    .setMessage(licenseMap.get(items[index]))
                                    .setCancelable(true)
                                    .setPositiveButton(R.string.title_close, (dialog, i) -> dialog.dismiss())
                                    .show()
                    )
                    .setCancelable(true)
                    .setPositiveButton(R.string.title_close, (d, i) -> d.dismiss())
                    .show();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private String readUtf8(final InputStream i) throws IOException {
        return Okio.buffer(Okio.source(i)).readUtf8();
    }
}
