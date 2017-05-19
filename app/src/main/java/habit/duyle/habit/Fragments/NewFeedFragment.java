package habit.duyle.habit.Fragments;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;

import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import habit.duyle.habit.Activities.MainActivity;
import habit.duyle.habit.Decorator.VerticalSpaceItemDecorator;
import habit.duyle.habit.adapters.FeedColumnAdapter;
import habit.duyle.habit.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import habit.duyle.habit.models.DayDesciption;
import habit.duyle.habit.models.Feed;
import habit.duyle.habit.models.FeedSinglePicture;
import habit.duyle.habit.services.FeedDataService;
import habit.duyle.habit.services.UserDataServices;

/**
 * Created by Duy Le on 11/21/2016.
 */

public class NewFeedFragment extends Fragment {
    private FloatingActionButton updateHabitButton;
    private FeedColumnAdapter recyclerAdapter;
    private FloatingActionButton addButton;
    private Feed.Builder feedBuilder;
    private static NewFeedFragment newFeedFragment;
    private RecyclerView newFeedRecyclerView;
    private FloatingActionMenu addFeedMenu;

    public static NewFeedFragment getNewFeedFragment() {
        return newFeedFragment;
    }

    public NewFeedFragment (){

    }
    public FeedColumnAdapter getRecyclerAdapter() {
        return recyclerAdapter;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.new_feed_fragment,container,false);
        setIDs(view);
        setListeners();
        checkIfUserIsInDatabase();
        setupNewFeedRecyclerView(view.getContext());
        refreshData();
        setUpdateHabitButtonVisibility();

        return view;
    }
    public void setIDs(View view){
        newFeedRecyclerView = (RecyclerView) view.findViewById(R.id.newFeedRecyclerView);
        updateHabitButton = (FloatingActionButton) view.findViewById(R.id.updateHabitButton);
        addButton = (FloatingActionButton) view.findViewById(R.id.addNewHabitButton);
        addFeedMenu = (FloatingActionMenu) view.findViewById(R.id.addFeedMenu);
        newFeedFragment=this;
    }

    private void setListeners(){
        addButton.setOnClickListener(addButtonListener);
        updateHabitButton.setOnClickListener(updateButtonListener);
    }
    private void setupFloatButton(View view){

    }

    View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.loadAddHabitFragment();
        }
    };
    View.OnClickListener updateButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MainActivity.getMainActivity().loadUpdateHabitFragment();
        }
    };
    private void checkIfUserIsInDatabase(){
        if(UserDataServices.getInstance().isUserNotInDatabase()){
            MainActivity.getMainActivity().loadRegisterGetInfoFragment();
        }
    }

    public void refreshData() {
        readFeedStatusFromDB();
        recyclerAdapter.updateData();
    }
    public void readFeedStatusFromDB(){
        DatabaseReference feedStatusRef = FirebaseDatabase.getInstance().getReference("feedStatus");
        feedStatusRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    if(postSnapShot.getValue(String.class).equals("Public")){
                        getUserDataFromDataSnapshot(postSnapShot
                        );
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

    public void setUpdateHabitButtonVisibility(){
        if(FeedDataService.getInstance().getFeedArrayListForSorting().size()==0){
            updateHabitButton.setVisibility(View.GONE);
        }
        else{
            updateHabitButton.setVisibility(View.VISIBLE);
        }
    }
    private void setupNewFeedRecyclerView(final Context context){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        newFeedRecyclerView.setHasFixedSize(true);
        newFeedRecyclerView.setLayoutManager(layoutManager);
        newFeedRecyclerView.addItemDecoration(new VerticalSpaceItemDecorator(30));
        recyclerAdapter = new FeedColumnAdapter(context,FeedDataService.getInstance().getFeedArrayListForSorting());

        newFeedRecyclerView.setAdapter(recyclerAdapter);

        newFeedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState!=0){
                    addFeedMenu.hideMenu(true);
                }
                else{
                    addFeedMenu.showMenu(true);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        newFeedRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }


    public void getUserDataFromDataSnapshot(DataSnapshot dataSnapshot){
        DatabaseReference allFeedRef = FirebaseDatabase.getInstance().getReference("feed/"+dataSnapshot.getRef().getParent().getKey().toString());
        allFeedRef.child(dataSnapshot.getKey()).orderByChild("updatedTime").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                feedBuilder =setUpFeedBuilder(dataSnapshot,feedBuilder);
                if(!feedBuilder.isNull()){
                    HashMap<String,Feed> feedHashMap = FeedDataService.getInstance().getFeedHashMapForStoringData();
                    Feed feed = feedBuilder.build();
                    feedHashMap.put(feed.getCreatedTime()+"",feed);

                    ArrayList<Feed> feedArrayList = new ArrayList<Feed>(feedHashMap.values());
                    sortFeeds(feedArrayList);
                    FeedDataService.getInstance().setFeedArrayListForSorting(feedArrayList);

                    setUpdateHabitButtonVisibility();
                    recyclerAdapter.updateData();
                    feedBuilder=new Feed.Builder();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals("allFeedPicture")) {
                    GenericTypeIndicator<ArrayList<FeedSinglePicture>> t = new GenericTypeIndicator<ArrayList<FeedSinglePicture>>() {
                    };
                    ArrayList<FeedSinglePicture> allFeedPicture = dataSnapshot.getValue(t);

                    HashMap<String,Feed> feedHashMap = FeedDataService.getInstance().getFeedHashMapForStoringData();
                    Feed feed = feedHashMap.get(dataSnapshot.getRef().getParent().getKey());
                    feed.setAllFeedPicture(allFeedPicture);
                }
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
    private Feed.Builder setUpFeedBuilder(DataSnapshot postSnapshot,Feed.Builder feedBuilder){
        feedBuilder=checkFeedBuilder(feedBuilder);

        switch(String.valueOf(postSnapshot.getKey())){
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
                GenericTypeIndicator<ArrayList<DayDesciption>> t = new GenericTypeIndicator<ArrayList<DayDesciption>>() {};
                ArrayList<DayDesciption> retrieved = postSnapshot.getValue(t);
                ArrayList<DayDesciption> dayDescriptionArrayList = new ArrayList<DayDesciption>();
                for (int i = 0; i < retrieved.size(); i++) {
                    dayDescriptionArrayList.add(new DayDesciption(retrieved.get(i).getDay(),retrieved.get(i).getDayDescription()));
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

    private Feed.Builder  checkFeedBuilder(Feed.Builder feedBuilder){
        if(feedBuilder==null){
            feedBuilder= new Feed.Builder();
            return feedBuilder;
        }
        return feedBuilder;
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }
    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}


