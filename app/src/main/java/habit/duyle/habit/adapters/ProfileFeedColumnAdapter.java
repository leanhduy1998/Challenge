package habit.duyle.habit.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

import habit.duyle.habit.Decorator.HorizontalSpaceItemDecorator;
import habit.duyle.habit.Fragments.ProfilePicAndInfoFragment;
import habit.duyle.habit.R;
import habit.duyle.habit.holders.ProfileFeedColumnHolder;
import habit.duyle.habit.holders.FeedPictureRowHolder;
import habit.duyle.habit.models.Feed;
import habit.duyle.habit.models.FeedSinglePicture;

/**
 * Created by leanh on 2/10/2017.
 */

public class ProfileFeedColumnAdapter extends RecyclerView.Adapter<ProfileFeedColumnHolder> {
    private Context context;
    private ArrayList<Feed> feeds;
    private CardView profileColumnCard;
    private RecyclerView profileColumnRecyclerView;
    private View view;

    public ProfileFeedColumnAdapter(Context context, ArrayList<Feed>feeds){
        this.context=context;
        this.feeds=feeds;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public ProfileFeedColumnHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_column, parent, false);
        setID(view);
        return new ProfileFeedColumnHolder(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setID(View view){
        profileColumnCard = (CardView) view.findViewById(R.id.profileColumnCard);
        profileColumnCard.setElevation(0);
        profileColumnCard.setPreventCornerOverlap(false);

        profileColumnRecyclerView = (RecyclerView) view.findViewById(R.id.profileColumnRecyclerView);
    }

    @Override
    public void onBindViewHolder(ProfileFeedColumnHolder holder, int position) {
        if(position==0){
            showProfilePicAndInfoFragment();
        }

        ArrayList<FeedSinglePicture> feedPicturesArrayList = feeds.get(position).getAllFeedPicture();

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

        //reuse
        FeedPictureRowHolder.setShowDotButtonList(showDotButtonList);

        ProfileRowAdapter rowAdapter = new ProfileRowAdapter(context,rowsAfterShorten);
        if(feedPicturesArrayList.size()>1){
            profileColumnRecyclerView.addItemDecoration(new HorizontalSpaceItemDecorator(30));
        }
        profileColumnRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        profileColumnRecyclerView.setLayoutManager(layoutManager);
        profileColumnRecyclerView.setAdapter(rowAdapter);
    }
    public void showProfilePicAndInfoFragment(){
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        ProfilePicAndInfoFragment fragment = new ProfilePicAndInfoFragment();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.profilePicsAndInfoContainer, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }


    @Override
    public int getItemCount() {
        return null != feeds ? feeds.size() : 0;
    }

}
