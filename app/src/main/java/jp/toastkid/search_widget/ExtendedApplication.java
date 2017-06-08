package jp.toastkid.search_widget;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * For using LeakCanary and so on...
 *
 * @author toastkidjp
 */
public class ExtendedApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
