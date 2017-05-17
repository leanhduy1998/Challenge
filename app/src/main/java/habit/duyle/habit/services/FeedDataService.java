package habit.duyle.habit.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import habit.duyle.habit.models.Feed;

/**
 * Created by Duy Le on 11/28/2016.
 */
public class FeedDataService {
    private static FeedDataService ourInstance = new FeedDataService();
    private ArrayList<Feed> feedArrayListForSorting = new ArrayList<>();
    private HashMap<String,Feed> feedHashMapForStoringData = new HashMap<>();
    private Calendar calender;

    public HashMap<String, Feed> getFeedHashMapForStoringData() {
        return feedHashMapForStoringData;
    }

    public ArrayList<Feed> getFeedArrayListForSorting() {
        return feedArrayListForSorting;
    }

    public void setFeedArrayListForSorting(ArrayList<Feed> feedArrayListForSorting) {
        this.feedArrayListForSorting = feedArrayListForSorting;
    }

    public static FeedDataService getInstance() {
        return ourInstance;
    }

    private FeedDataService() {
    }
    public long getTimeInMillisNow(){
        calender=Calendar.getInstance();
        return calender.getTimeInMillis();
    }
}
