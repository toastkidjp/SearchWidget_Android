package jp.toastkid.search_widget.libs;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author toastkidjp
 */
public class Inputs {

    /**
     * Show software keybord.
     * @param activity
     * @param editText
     */
    public static void showKeyboard(final Activity activity, final EditText editText) {
        final InputMethodManager inputMethodManager
                = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }
}
