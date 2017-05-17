package habit.duyle.habit.holders;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import habit.duyle.habit.Activities.MainActivity;
import habit.duyle.habit.R;
import habit.duyle.habit.models.FeedSinglePicture;

/**
 * Created by Duy Le on 12/24/2016.
 */

public class PictureSlideShowSingleCardHolder extends RecyclerView.ViewHolder {
    private ImageView pictureSlideShowCardPicture;
    private TextView pictureSlideShowDayTextView;

    public PictureSlideShowSingleCardHolder(View itemView) {
        super(itemView);
        pictureSlideShowCardPicture = (ImageView) itemView.findViewById(R.id.pictureSlideShowSingleCard);
        pictureSlideShowDayTextView = (TextView) itemView.findViewById(R.id.pictureSlideShowSingleDayTV);
        pictureSlideShowDayTextView.setVisibility(View.GONE);
    }
    public void updateUI(final FeedSinglePicture feedSinglePicture, final Context context) throws IOException {
        final File thumbnailFolder = MainActivity.getMainActivity().getThumbnailFolder();
        if(feedSinglePicture.getPictureName().equals("noimage.jpg")||feedSinglePicture.getPictureStoragePath().equals("nourl")){
            Picasso.with(context).load(R.drawable.noimage).into(pictureSlideShowCardPicture);
        }
        else{
            MainActivity.getMainActivity().showProgressBar();
            StorageReference storageReference= FirebaseStorage.getInstance().getReference(feedSinglePicture.getPictureStoragePath());
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(pictureSlideShowCardPicture);
        }
        MainActivity.getMainActivity().hideProgressBar();
    }
}
