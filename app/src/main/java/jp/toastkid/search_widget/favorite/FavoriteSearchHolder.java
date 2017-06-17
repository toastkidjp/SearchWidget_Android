package jp.toastkid.search_widget.favorite;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import jp.toastkid.search_widget.R;

/**
 * @author toastkidjp
 */
class FavoriteSearchHolder extends RecyclerView.ViewHolder {

    final ImageView imageView;

    final TextView  textView;

    FavoriteSearchHolder(final View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.favorite_search_image);
        textView  = (TextView) itemView.findViewById(R.id.favorite_search_text);
    }

    void setImageId(@DrawableRes final int iconId) {
        imageView.setImageDrawable(
                AppCompatDrawableManager.get().getDrawable(imageView.getContext(), iconId));
    }

    void setText(@NonNull final String query) {
        textView.setText(query);
    }
}