package habit.duyle.habit.Fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import habit.duyle.habit.Decorator.VerticalSpaceItemDecorator;
import habit.duyle.habit.R;
import habit.duyle.habit.adapters.PictureSlideShowAdapter;
import habit.duyle.habit.models.FeedSinglePicture;

/**
 * Created by Duy Le on 12/24/2016.
 */


public class PictureSlideShowFragment extends Fragment {
    private RecyclerView pictureSlideShowRecycleView;
    private TextView pictureSlideShowNameTV;
    private TextView pictureSlideShowDayTV;
    private String habitName;
    private String day;
    private int scrollCoor = 0;
    private ArrayList<FeedSinglePicture> singlePicturesArrayList;
    private ArrayList<FeedSinglePicture> singleDayPicsArrayList = new ArrayList<>();
    public PictureSlideShowFragment() {

    }
    @SuppressLint("ValidFragment")
    public PictureSlideShowFragment(int day,ArrayList<FeedSinglePicture> singlePicturesArrayList) {
        this.singlePicturesArrayList=singlePicturesArrayList;
        filterPictureForSameFeed(day);
    }


    public void filterPictureForSameFeed(int day){
        singleDayPicsArrayList = (ArrayList<FeedSinglePicture>) singlePicturesArrayList.clone();
        for(Iterator<FeedSinglePicture> iterator = singleDayPicsArrayList.iterator(); iterator.hasNext();){
            FeedSinglePicture feedSinglePicture = iterator.next();
            if(feedSinglePicture.getDay()!=day){
                iterator.remove();
            }
            this.day = day+"";
            habitName = feedSinglePicture.getFeedName();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.picture_slideshow_fragment,container,false);
        setIDs(view);
        setUpRecyclerView(view);
        setTextViews();
        return view;
    }
    private void setIDs(View view){
        pictureSlideShowRecycleView = (RecyclerView) view.findViewById(R.id.pictureSlideShowRecycleView);
        pictureSlideShowNameTV = (TextView) view.findViewById(R.id.pictureSlideShowNameTV);
        pictureSlideShowDayTV = (TextView) view.findViewById(R.id.pictureSlideShowDayTV);
    }
    private void setUpRecyclerView(View view){
        pictureSlideShowRecycleView.addItemDecoration(new VerticalSpaceItemDecorator(30));
        pictureSlideShowRecycleView.setNestedScrollingEnabled(false);
        pictureSlideShowRecycleView.setHasFixedSize(true);
        pictureSlideShowRecycleView.setLayoutManager(createLayoutManager(view));

        PictureSlideShowAdapter pictureSlideShowAdapter = new PictureSlideShowAdapter(view.getContext(),singleDayPicsArrayList);
        pictureSlideShowRecycleView.setAdapter(pictureSlideShowAdapter);

        pictureSlideShowRecycleView.addOnScrollListener(recyclerViewOnScrollListener);
    }
    private void setTextViews(){
        pictureSlideShowDayTV.setText(day);
        pictureSlideShowNameTV.setText(habitName);
    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            scrollCoor = scrollCoor + dy;
            if(scrollCoor<=0){
                scrollCoor=0;
            }
        }
    };

    public LinearLayoutManager createLayoutManager(View view){
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return layoutManager;
    }


}
