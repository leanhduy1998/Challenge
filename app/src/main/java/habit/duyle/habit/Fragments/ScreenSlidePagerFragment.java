package habit.duyle.habit.Fragments;

/**
 * Created by leanh on 2/1/2017.
 */

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;

import habit.duyle.habit.R;
import habit.duyle.habit.models.Feed;
import habit.duyle.habit.models.FeedSinglePicture;
import habit.duyle.habit.services.FeedDataService;

@SuppressLint("ValidFragment")
public class ScreenSlidePagerFragment extends Fragment {
    private ViewPager pager;
    private ArrayList<FeedSinglePicture> singlePicturesArrayList = new ArrayList<>();
    private ArrayList<Integer>daysForCountArrayList;
    
    @SuppressLint("ValidFragment")
    public ScreenSlidePagerFragment(FeedSinglePicture feedSinglePicture){
        filterPicturesForDiffrentFeeds(feedSinglePicture.getFeedName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.picture_slideshow_viewpager_fragment,container,false);
        setIDs(view);
        return view;
    }

    public void setIDs(View view){
        pager = (ViewPager) view.findViewById(R.id.pictureSlideShowPager);
        pager.setAdapter(new ScreenSlidePagerAdapter(getChildFragmentManager()));
    }

    public void filterPicturesForDiffrentFeeds(String feedName){
        ArrayList<Feed> feedArrayList = FeedDataService.getInstance().getFeedArrayListForSorting();
        daysForCountArrayList = new ArrayList<>();
        singlePicturesArrayList.clear();

        for(Iterator<Feed> feedIterator = feedArrayList.iterator();feedIterator.hasNext();){
            Feed feed = feedIterator.next();
            ArrayList<FeedSinglePicture> feedSinglePictures = feed.getAllFeedPicture();

            for(Iterator<FeedSinglePicture> feedSinglePictureIterator = feedSinglePictures.iterator();feedSinglePictureIterator.hasNext();) {
                FeedSinglePicture feedSinglePicture = feedSinglePictureIterator.next();
                if(feedSinglePicture.getFeedName().equals(feedName)){
                    if(!daysForCountArrayList.contains(feedSinglePicture.getDay())){
                        daysForCountArrayList.add(feedSinglePicture.getDay());
                    }
                    singlePicturesArrayList.add(feedSinglePicture);
                }
            }
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new PictureSlideShowFragment(daysForCountArrayList.get(position),singlePicturesArrayList);
        }

        @Override
        public int getCount() {
            return daysForCountArrayList.size();
        }
    }
}
