package habit.duyle.habit.holders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import habit.duyle.habit.Activities.MainActivity;
import habit.duyle.habit.R;
import habit.duyle.habit.models.FeedSinglePicture;

/**
 * Created by Duy Le on 12/5/2016.
 */

public class ProfilePictureHolder extends RecyclerView.ViewHolder {
    private TextView dayTitle;
    private ImageView itemImage;
    private Button dotButton;
    private final long ONE_MEGABYTE = 1024 * 1024;


    public static void setShowDotButtonList(ArrayList<Integer> showDotButtonList) {
        ProfilePictureHolder.showDotButtonList = showDotButtonList;
    }

    private static ArrayList<Integer> showDotButtonList = new ArrayList<>();
    public ProfilePictureHolder(View itemView) {
        super(itemView);
        setId();
    }
    private void setId(){
        dayTitle = (TextView) itemView.findViewById(R.id.dayTitle);
        itemImage = (ImageView) itemView.findViewById(R.id.singleFeedImage);
        dotButton = (Button) itemView.findViewById(R.id.dotButton);
    }

    public static ArrayList<Integer> getShowDotButtonList() {
        return showDotButtonList;
    }

    public void updateUI(final FeedSinglePicture feedSinglePicture, final Context context) throws IOException {
        dayTitle.setText("Day "+feedSinglePicture.getDay());
        dotButton.setVisibility(Button.GONE);
        dotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getMainActivity().loadPictureSlideShowFragment(feedSinglePicture);
            }
        });

        for(int i=0;i<showDotButtonList.size();i++){
            if(feedSinglePicture.getDay()==showDotButtonList.get(i)){
                dotButton.setVisibility(Button.VISIBLE);
            }
        }

        if(feedSinglePicture.getPictureName().equals("noimage.jpg")||feedSinglePicture.getPictureStoragePath().equals("nourl")){
            Picasso.with(context).load(R.drawable.noimage).into(itemImage);
        }
        else{
            StorageReference storageReference= FirebaseStorage.getInstance().getReference(feedSinglePicture.getPictureStoragePath());
            Glide.with(context).using(new FirebaseImageLoader()).load(storageReference).listener(new RequestListener<StorageReference, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    return false;
                }
            }).into(itemImage);
        }
    }


}
