package thegamers.duyle.gamers.Fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.design.widget.FloatingActionButton;
import android.widget.LinearLayout;

import thegamers.duyle.gamers.Activities.MainActivity;
import thegamers.duyle.gamers.adapters.NewFeedAdapter;
import thegamers.duyle.gamers.R;

import java.io.File;
import java.util.ArrayList;

import thegamers.duyle.gamers.services.DataService;

/**
 * Created by Duy Le on 11/21/2016.
 */

public class NewFeedFragment extends Fragment {
    public NewFeedFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_feed_fragment,container,false);

        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.addNewHabitButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadAddHabitFragment();
            }
        });

        File saveFolder = new File(Environment.getExternalStorageDirectory().toString()+"/camera_app/save/");
        if(saveFolder.exists()){
            File[] listFiles = saveFolder.listFiles();
            ArrayList<String> habits = new ArrayList<String>();
            for(int i=0;i<listFiles.length;i++){
                habits.add(listFiles[i].getName());
            }

/*
            newFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String skill = String.valueOf(parent.getItemAtPosition(position));
                    Toast.makeText(view.getContext(),skill,Toast.LENGTH_LONG).show();
                }
            });*/
        }
        RecyclerView newFeedRecyclerView = (RecyclerView) view.findViewById(R.id.newFeedRecyclerView);
        newFeedRecyclerView.setHasFixedSize(true);
        newFeedRecyclerView.addItemDecoration(new HorizontalSpaceItemDecorator(30));

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext());
        verticalLayoutManager.setOrientation(LinearLayout.VERTICAL);

        newFeedRecyclerView.setLayoutManager(verticalLayoutManager);

        NewFeedAdapter adapter = new NewFeedAdapter(DataService.getInstance().getFeed());
        newFeedRecyclerView.setAdapter(adapter);




        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }
}

class HorizontalSpaceItemDecorator extends RecyclerView.ItemDecoration {

    private final int spacer;

    public HorizontalSpaceItemDecorator(int spacer) {
        this.spacer = spacer;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = spacer;
    }
}
