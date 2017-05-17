package habit.duyle.habit.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import habit.duyle.habit.R;

/**
 * Created by leanh on 2/22/2017.
 */

public class IndividualCategoryFragment extends Fragment {
    private RecyclerView individualCategoryRV;
    private TextView individualCategoryTV;
    private String categoryName;

    public IndividualCategoryFragment(String categoryName) {
        this.categoryName = categoryName;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.individual_category_fragment,container,false);
        setIDs(view);
        setIndividualCategoryTV(categoryName);
        return view;
    }
    private void setIDs(View view){
        individualCategoryRV = (RecyclerView) view.findViewById(R.id.individualCategoryRV);
        individualCategoryTV = (TextView) view.findViewById(R.id.individualCategoryNameTV);
    }
    private void setIndividualCategoryTV(String categoryName){
        individualCategoryTV.setText(categoryName);
    }
    private void setUpRecyclerView(){


    }
}
