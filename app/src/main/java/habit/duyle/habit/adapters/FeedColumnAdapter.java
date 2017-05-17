package habit.duyle.habit.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

import habit.duyle.habit.Decorator.HorizontalSpaceItemDecorator;
import habit.duyle.habit.R;
import habit.duyle.habit.holders.FeedColumnHolder;
import habit.duyle.habit.holders.FeedPictureRowHolder;
import habit.duyle.habit.models.Feed;
import habit.duyle.habit.models.FeedSinglePicture;
import habit.duyle.habit.services.FeedDataService;

/**
 * Created by Duy Le on 11/3/2016.
 */

public class FeedColumnAdapter extends RecyclerView.Adapter<FeedColumnHolder> implements View.OnClickListener {
    private Context context;
    private ArrayList<Feed> allRows;
    private CardView feedCard;

    public FeedColumnAdapter(Context context, ArrayList<Feed> allIteminRow) {
        this.allRows = allIteminRow;
        this.context=context;
    }
    @Override
    public FeedColumnHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_column, parent, false);
        feedCard = (CardView) view.findViewById(R.id.feedColumnCard);
    //    feedCard.setElevation(0);
        return new FeedColumnHolder(view);
    }

    @Override
    public void onBindViewHolder(final FeedColumnHolder holder, int position) {

        final Feed feed = allRows.get(position);
        holder.updateUI(feed);

        ArrayList<FeedSinglePicture> feedPicturesArrayList = allRows.get(position).getAllFeedPicture();

        Collections.sort(feedPicturesArrayList, FeedSinglePicture.getPictureComparator());

        ArrayList<FeedSinglePicture> notVisiblePicturesArrayList = new ArrayList<>();
        ArrayList<Integer> showDotButtonList = new ArrayList<>();
        for(int i=0;i<feedPicturesArrayList.size()-1;i++){
            FeedSinglePicture o1 = feedPicturesArrayList.get(i);
            FeedSinglePicture o2 = feedPicturesArrayList.get(i+1);
            if(o1.getDay()==o2.getDay()){
                notVisiblePicturesArrayList.add(o2);
                showDotButtonList.add(o1.getDay());
            }
        }

        // need to create a clone because allRows.get(position).getAllFeedPicture() is static. Which means, all
        //change in rows will change all rows and everything
        ArrayList<FeedSinglePicture> rowsAfterShorten = (ArrayList<FeedSinglePicture>) feedPicturesArrayList.clone();
        rowsAfterShorten.removeAll(notVisiblePicturesArrayList);

        FeedPictureRowHolder.setShowDotButtonList(showDotButtonList);
        FeedRowAdapter rowAdapter = new FeedRowAdapter(context,rowsAfterShorten);

        RecyclerView newFeedRecylerView = holder.getNewFeedRecyclerview();
        if(feedPicturesArrayList.size()>1){
            newFeedRecylerView.addItemDecoration(new HorizontalSpaceItemDecorator(30));
        }
        newFeedRecylerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        newFeedRecylerView.setLayoutManager(layoutManager);
        newFeedRecylerView.setAdapter(rowAdapter);
    }

    @Override
    public int getItemCount() {
        return (null != allRows ? allRows.size() : 0);
    }
    public void updateData(){
        allRows.clear();
        allRows.addAll(FeedDataService.getInstance().getFeedArrayListForSorting());
        notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {

    }
}
