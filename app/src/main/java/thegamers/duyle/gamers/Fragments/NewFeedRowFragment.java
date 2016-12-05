package thegamers.duyle.gamers.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import thegamers.duyle.gamers.Activities.MainActivity;
import thegamers.duyle.gamers.R;
import thegamers.duyle.gamers.adapters.NewFeedAdapter;
import thegamers.duyle.gamers.services.DataService;

/**
 * Created by Duy Le on 12/3/2016.
 */

public class NewFeedRowFragment extends Fragment {
    public NewFeedRowFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_feed_row,container,false);

        MainActivity.getMainActivity().loadFeedRowFragment();

        RecyclerView newFeedRowRecyclerView = (RecyclerView) view.findViewById(R.id.newFeedRowRecyclerView);
        newFeedRowRecyclerView.setHasFixedSize(true);
        newFeedRowRecyclerView.addItemDecoration(new HorizontalSpaceItemDecorator(30));

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext());
        horizontalLayoutManager.setOrientation(LinearLayout.HORIZONTAL);

        NewFeedAdapter adapter1 = new NewFeedAdapter(DataService.getInstance().getFeed());
        newFeedRowRecyclerView.setAdapter(adapter1);




        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.getMainActivity().loadFeedRowFragment();
    }
}
