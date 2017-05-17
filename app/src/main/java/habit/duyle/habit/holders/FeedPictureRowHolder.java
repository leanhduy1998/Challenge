package habit.duyle.habit.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import habit.duyle.habit.Activities.MainActivity;
import habit.duyle.habit.R;
import habit.duyle.habit.models.FeedSinglePicture;

/**
 * Created by Duy Le on 12/5/2016.
 */

public class FeedPictureRowHolder extends RecyclerView.ViewHolder {
    private TextView dayTitle;
    private ImageView itemImage;
    private Button dotButton;
    private static ArrayList<Integer> showDotButtonList = new ArrayList<>();
    public static void setShowDotButtonList(ArrayList<Integer> showDotButtonList) {
        FeedPictureRowHolder.showDotButtonList = showDotButtonList;
    }

    public FeedPictureRowHolder(View itemView) {
        super(itemView);
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

        if(feedSinglePicture.getPictureName().equals("noimage.jpg")||feedSinglePicture.getPictureStoragePath().toString().equals("nourl")){
            Picasso.with(context).load(R.drawable.noimage).into(itemImage);
        }
        else{
            final File thumbnailFolder = MainActivity.getMainActivity().getThumbnailFolder();
            final File pictureFile = new File(thumbnailFolder.getAbsolutePath()+"/"+feedSinglePicture.getPictureName());
            if(!pictureFile.exists()){
                StorageReference storageReference= FirebaseStorage.getInstance().getReference(feedSinglePicture.getPictureStoragePath());
                Glide.with(context)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .into(itemImage);
            }
            else{
                Picasso.with(context).load(pictureFile).into(itemImage);
            }
        }
    }


}
