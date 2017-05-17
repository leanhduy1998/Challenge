package habit.duyle.habit.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;

import habit.duyle.habit.R;
import habit.duyle.habit.holders.ProfilePictureHolder;
import habit.duyle.habit.models.FeedSinglePicture;

/**
 * Created by Duy Le on 12/5/2016.
 */

public class ProfileRowAdapter extends RecyclerView.Adapter<ProfilePictureHolder> {
    private Context context;
    private ArrayList<FeedSinglePicture> singleFeedPictureArrayList;
    private CardView profileRowCard;


    public ProfileRowAdapter(Context context, ArrayList<FeedSinglePicture> itemsList) {
        this.context = context;
        this.singleFeedPictureArrayList=itemsList;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public ProfilePictureHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_row_card,null);
        profileRowCard= (CardView) view.findViewById(R.id.profileRowCard);
        profileRowCard.setPreventCornerOverlap(false);
        profileRowCard.setElevation(0);
        ProfilePictureHolder mh = new ProfilePictureHolder(view);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ProfilePictureHolder holder, int position) {
        FeedSinglePicture feedSinglePicture = singleFeedPictureArrayList.get(position);
        try {
            holder.updateUI(feedSinglePicture, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        return (null != singleFeedPictureArrayList ? singleFeedPictureArrayList.size() : 0);
    }
}

