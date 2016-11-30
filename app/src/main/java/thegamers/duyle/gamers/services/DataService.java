package thegamers.duyle.gamers.services;

import java.util.ArrayList;

import thegamers.duyle.gamers.models.Feed;

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
    public ArrayList<Feed> getFeed(){
        //pretend data is loaded
        ArrayList<Feed> habits = new ArrayList<>();
        habits.add(new Feed("h1",30,"Day(s)","des1","public", (long) 1234567));
        habits.add(new Feed("h2",20,"Day(s)","des2","public", (long) 1234567));
        habits.add(new Feed("h3",10,"Day(s)","des3","public", (long) 1234567));

                //finish pretend loading data
        return habits;
    }
}
