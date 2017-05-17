package habit.duyle.habit.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import habit.duyle.habit.R;
import habit.duyle.habit.adapters.CategoryMainAdapter;
import habit.duyle.habit.services.CategoryData;

/**
 * Created by leanh on 2/16/2017.
 */

public class CategoryMainFragment extends Fragment {
    private RecyclerView categoryRV;
    private NestedScrollView scrollView;
    private CategoryMainAdapter categoryMainAdapter;
    private View view;
    private String displayOrAdd;
    private Button editCategoryBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cagetory_main_fragment,container,false);
        setID(view);
        setupRecyclerView(view);
        timerTask.run();
        setUpUI();
        this.view=view;
        return view;
    }

    public CategoryMainFragment(String displayOrAdd) {
        this.displayOrAdd = displayOrAdd;
    }

    private void setID(View view){
        categoryRV = (RecyclerView) view.findViewById(R.id.categoryRV);
        editCategoryBtn = (Button) view.findViewById(R.id.edit_category_btn);
    }
    private void setUpUI(){
        if(displayOrAdd.equals("add")){
            editCategoryBtn.setVisibility(View.GONE);
        }
        else{
            editCategoryBtn.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerView(View view){
        categoryMainAdapter = new CategoryMainAdapter(view.getContext(), CategoryData.getInstance().getCategoryArrayList(),displayOrAdd);
        categoryRV.setAdapter(categoryMainAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        categoryRV.setLayoutManager(linearLayoutManager);
    }
    public void refreshData(){
        categoryMainAdapter.notifyDataSetChanged();
    }
    private Timer timer;
    private TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            if(CategoryData.getInstance().getCategoryArrayList().size()<8){
                start();
            }
            else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshData();
                    }
                });
                stop();
            }
        }
    };

    public void start() {
        if(timer != null) {
            return;
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    public void stop() {
        timer = null;
    }
}
