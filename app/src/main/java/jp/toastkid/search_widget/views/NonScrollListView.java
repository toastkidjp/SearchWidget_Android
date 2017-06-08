package jp.toastkid.search_widget.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author toastkidjp
 */
public class NonScrollListView extends ListView {

    public NonScrollListView(final Context context) {
        super(context);
    }

    public NonScrollListView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public NonScrollListView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST)
        );
        getLayoutParams().height = getMeasuredHeight();
    }
}
