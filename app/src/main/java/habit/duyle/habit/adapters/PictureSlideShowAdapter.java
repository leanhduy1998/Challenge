package habit.duyle.habit.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;

import habit.duyle.habit.R;
import habit.duyle.habit.holders.PictureSlideShowSingleCardHolder;
import habit.duyle.habit.models.FeedSinglePicture;

/**
 * Created by Duy Le on 12/24/2016.
 */

public class PictureSlideShowAdapter extends RecyclerView.Adapter<PictureSlideShowSingleCardHolder> implements View.OnClickListener {
    private CardView pictureSlideShowSingleCardView;
    private ArrayList<FeedSinglePicture> feedSinglePictureArrayList;
    private Context context;

    public PictureSlideShowAdapter(Context context, ArrayList<FeedSinglePicture> feedSinglePictureArrayList) {
        this.feedSinglePictureArrayList = feedSinglePictureArrayList;
        this.context = context;
    }

    @Override
    public PictureSlideShowSingleCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_slideshow_single_card,parent,false);
        return new PictureSlideShowSingleCardHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setIDs(View view){
        pictureSlideShowSingleCardView = (CardView) view.findViewById(R.id.pictureSlideShowSingleCardView);
        pictureSlideShowSingleCardView.setElevation(0);
    }

    @Override
    public void onBindViewHolder(PictureSlideShowSingleCardHolder holder, final int position) {
        try {
            holder.updateUI(feedSinglePictureArrayList.get(position),this.context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != feedSinglePictureArrayList ? feedSinglePictureArrayList.size() : 0);
    }

    @Override
    public void onClick(View v) {
    }
}
