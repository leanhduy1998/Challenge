package habit.duyle.habit.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import habit.duyle.habit.models.Feed;
import habit.duyle.habit.R;

/**
 * Created by Duy Le on 11/28/2016.
 */

public class FeedColumnHolder extends RecyclerView.ViewHolder {
    private TextView titleTextView;
    private RecyclerView newFeedRecyclerView;

        public FeedColumnHolder(View itemView) {
            super(itemView);
            this.titleTextView = (TextView)itemView.findViewById(R.id.main_text);
            this.newFeedRecyclerView=(RecyclerView) itemView.findViewById(R.id.horizontalRecyclerView);
        }
        public void updateUI(Feed feed) {
            titleTextView.setText(feed.getHabitName());
        }
        public RecyclerView getNewFeedRecyclerview() {
            return newFeedRecyclerView;
        }
}
