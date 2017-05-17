package habit.duyle.habit.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import habit.duyle.habit.Activities.MainActivity;
import habit.duyle.habit.Decorator.VerticalSpaceItemDecorator;
import habit.duyle.habit.R;
import habit.duyle.habit.adapters.ProfileFeedColumnAdapter;
import habit.duyle.habit.models.DayDesciption;
import habit.duyle.habit.models.Feed;
import habit.duyle.habit.models.FeedSinglePicture;

/**
 * Created by leanh on 2/10/2017.
 */

public class ProfileFeedFragment extends Fragment {
    private ProfileFeedColumnAdapter recyclerAdapter;
    private RecyclerView profileRecyclerView;
    private Feed.Builder feedBuilder;
    private HashMap feedsHashMap=new HashMap();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.profile_fragment, container, false);
        //need to be in this order
        setIDs(view);
        getUserFeedFromDB();
        setUpprofileRecyclerView(view.getContext());
        return view;
    }

    private void setIDs(View view) {
        profileRecyclerView = (RecyclerView) view.findViewById(R.id.profileRecyclerView);
    }


    public void setUpprofileRecyclerView(final Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        profileRecyclerView.setHasFixedSize(true);
        profileRecyclerView.setLayoutManager(layoutManager);
        profileRecyclerView.addItemDecoration(new VerticalSpaceItemDecorator(30));

        ArrayList<Feed> feedArrayList = new ArrayList<>(feedsHashMap.values());
        feedArrayList=sortFeeds(feedArrayList);


        recyclerAdapter = new ProfileFeedColumnAdapter(context, feedArrayList);
        profileRecyclerView.setAdapter(recyclerAdapter);
    }

    private void getUserFeedFromDB() {
        DatabaseReference allFeedRef = FirebaseDatabase.getInstance().getReference("feed/" + getUserUid());
        allFeedRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    feedBuilder = setUpFeedBuilder(postSnapshot, feedBuilder);
                    if (!feedBuilder.isNull()) {
                        Feed feed = feedBuilder.build();
                        feedsHashMap.put(dataSnapshot.getKey(),feed);

                        ArrayList<Feed> feedArrayList = new ArrayList<>(feedsHashMap.values());
                        feedArrayList=sortFeeds(feedArrayList);

                        recyclerAdapter = new ProfileFeedColumnAdapter(getContext(), feedArrayList);
                        profileRecyclerView.setAdapter(recyclerAdapter);

                        feedBuilder = new Feed.Builder();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
    private ArrayList sortFeeds(ArrayList<Feed> feeds){
        Collections.sort(feeds, Feed.getFeedUpdateTimeComparator());
        return feeds;
    }

    private Feed.Builder setUpFeedBuilder(DataSnapshot postSnapshot, Feed.Builder feedBuilder) {
        feedBuilder = checkFeedBuilder(feedBuilder);

        switch (String.valueOf(postSnapshot.getKey())) {
            case "createdTime":
                feedBuilder.createdTime(postSnapshot.getValue(Long.class));
                break;
            case "updatedTime":
                feedBuilder.updatedTime(postSnapshot.getValue(Long.class));
                break;
            case "habitName":
                feedBuilder.habitName(postSnapshot.getValue(String.class));
                break;
            case "username":
                feedBuilder.username(postSnapshot.getValue(String.class));
                break;
            case "amountOfDay":
                feedBuilder.amountOfDay(postSnapshot.getValue(Integer.class));
                break;
            case "typeOfLength":
                feedBuilder.typeOfLength(postSnapshot.getValue(String.class));
                break;
            case "feedDescription":
                feedBuilder.feedDescription(postSnapshot.getValue(String.class));
                break;
            case "publicity":
                feedBuilder.publicity(postSnapshot.getValue(String.class));
                break;
            case "uid":
                feedBuilder.uid(postSnapshot.getValue(String.class));
                break;
            case "dayDescriptionArrayList":
                GenericTypeIndicator<ArrayList<DayDesciption>> t = new GenericTypeIndicator<ArrayList<DayDesciption>>() {
                };
                ArrayList<DayDesciption> retrieved = postSnapshot.getValue(t);
                ArrayList<DayDesciption> dayDescriptionArrayList = new ArrayList<DayDesciption>();
                for (int i = 0; i < retrieved.size(); i++) {
                    dayDescriptionArrayList.add(new DayDesciption(retrieved.get(i).getDay(), retrieved.get(i).getDayDescription()));
                }
                feedBuilder.dayDescriptionArrayList(dayDescriptionArrayList);
                break;
            case "allFeedPicture":
                GenericTypeIndicator<ArrayList<FeedSinglePicture>> t2 = new GenericTypeIndicator<ArrayList<FeedSinglePicture>>() {
                };
                ArrayList<FeedSinglePicture> retrieved2 = postSnapshot.getValue(t2);
                ArrayList<FeedSinglePicture> allFeedPicture = new ArrayList<FeedSinglePicture>();

                for (int i = 0; i < retrieved2.size(); i++) {
                    FeedSinglePicture feedSinglePicture = new FeedSinglePicture.Builder()
                            .pictureName(retrieved2.get(i).getPictureName())
                            .pictureStoragePath(retrieved2.get(i).getPictureStoragePath())
                            .day(retrieved2.get(i).getDay())
                            .feedName(retrieved2.get(i).getFeedName())
                            .thumbnailStoragePath(retrieved2.get(i).getThumbnailStoragePath()).build();
                    allFeedPicture.add(feedSinglePicture);
                }
                feedBuilder.allFeedPicture(allFeedPicture);

            default:
                break;
        }
        return feedBuilder;
    }

    private Feed.Builder checkFeedBuilder(Feed.Builder feedBuilder) {
        if (feedBuilder == null) {
            feedBuilder = new Feed.Builder();
            return feedBuilder;
        }
        return feedBuilder;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private String getUserUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
