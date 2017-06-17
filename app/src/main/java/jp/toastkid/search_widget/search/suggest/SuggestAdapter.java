package jp.toastkid.search_widget.search.suggest;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import jp.toastkid.search_widget.R;

/**
 * Suggest list adapter.
 *
 * @author toastkidjp
 */
public class SuggestAdapter extends BaseAdapter {

    /** Layout inflater. */
    private final LayoutInflater mInflater;

    /** Suggest items. */
    private final List<String> mSuggests;

    /** EditText. */
    private final EditText mSearchInput;

    /** Using selected suggest action. */
    private final Consumer<String> mSuggestConsumer;

    /**
     *
     * @param inflater
     * @param input
     * @param suggestConsumer
     */
    public SuggestAdapter(
            @NonNull final LayoutInflater inflater,
            @NonNull final EditText input,
            @NonNull final Consumer<String> suggestConsumer
    ) {
        mInflater        = inflater;
        mSearchInput     = input;
        mSuggestConsumer = suggestConsumer;
        mSuggests        = new ArrayList<>(10);
    }

    /**
     * Clear suggests.
     */
    public void clear() {
        mSuggests.clear();
        notifyDataSetChanged();
    }

    /**
     * Replace items.
     * @param items
     */
    public void replace(final List<String> items) {
        mSuggests.clear();
        mSuggests.addAll(items);
    }

    @Override
    public int getCount() {
        return mSuggests.size();
    }

    @Override
    public Object getItem(int position) {
        return mSuggests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mSuggests.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String suggest = mSuggests.get(position);

        final View inflate = mInflater.inflate(R.layout.search_suggest, null);

        final TextView textView = (TextView) inflate.findViewById(R.id.search_suggest_text);
        textView.setText(suggest);

        inflate.setOnClickListener(v -> onItemClicked(suggest));
        inflate.findViewById(R.id.search_suggest_add).setOnClickListener(v -> onAddClicked(suggest));
        return inflate;
    }

    /**
     * Add(+) clicked action.
     * @param suggest
     */
    private void onAddClicked(final String suggest) {
        mSearchInput.setText(suggest + " ");
        mSearchInput.setSelection(mSearchInput.getText().toString().length());
    }

    /**
     * Item clicked action.
     * @param suggest
     */
    private void onItemClicked(final String suggest) {
        mSearchInput.setText(suggest);
        try {
            mSuggestConsumer.accept(suggest);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}