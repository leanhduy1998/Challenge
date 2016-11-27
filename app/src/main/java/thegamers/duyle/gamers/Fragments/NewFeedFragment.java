package thegamers.duyle.gamers.Fragments;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.design.widget.FloatingActionButton;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import thegamers.duyle.gamers.Activities.MainActivity;
import thegamers.duyle.gamers.R;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import thegamers.duyle.gamers.Activities.CustomAdapter;

/**
 * Created by Duy Le on 11/21/2016.
 */

public class NewFeedFragment extends Fragment {
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

            ListView newFeedList = (ListView) view.findViewById(R.id.newFeedList);


            ListAdapter adapter= new CustomAdapter(view.getContext(),habits);
            newFeedList.setAdapter(adapter);

            newFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String skill = String.valueOf(parent.getItemAtPosition(position));
                    Toast.makeText(view.getContext(),skill,Toast.LENGTH_LONG).show();
                }
            });
        }

        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }
}
