package thegamers.duyle.gamers.services;

import java.util.ArrayList;

import thegamers.duyle.gamers.models.Feed;
import thegamers.duyle.gamers.models.RowFeed;

/**
 * Created by Duy Le on 11/28/2016.
 */
public class DataService {
    private static DataService ourInstance = new DataService();

    public static DataService getInstance() {
        return ourInstance;
    }

    private DataService() {
    }
    public ArrayList<RowFeed> getFeed(){
        //pretend data is loaded
        ArrayList<RowFeed> habits = new ArrayList<>();
        habits.add(new RowFeed("h1",30,"Day(s)","des1","public", (long) 1234567));
        habits.add(new RowFeed("h2",20,"Day(s)","des2","public", (long) 1234567));
        habits.add(new RowFeed("h3",10,"Day(s)","des3","public", (long) 1234567));

                //finish pretend loading data
        return habits;
    }

}
