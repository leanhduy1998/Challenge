package thegamers.duyle.gamers.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import thegamers.duyle.gamers.R;
import thegamers.duyle.gamers.holders.FeedViewHolder;
import thegamers.duyle.gamers.models.Feed;

/**
 * Created by Duy Le on 11/30/2016.
 */

public class NewFeedRowAdapter extends RecyclerView.Adapter<FeedViewHolder> {
    private ArrayList<Feed> feed;
    public NewFeedRowAdapter(ArrayList<Feed> feed) {
        this.feed = feed;
    }
    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View feedCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_card, parent, false);
        return new FeedViewHolder(feedCard);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        final Feed temp = feed.get(position);
        holder.updateUI(temp);
    }

    @Override
    public int getItemCount() {
        return feed.size();
    }
}
