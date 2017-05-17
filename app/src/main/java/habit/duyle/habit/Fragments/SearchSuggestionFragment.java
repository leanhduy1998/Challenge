package habit.duyle.habit.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import habit.duyle.habit.Activities.MainActivity;
import habit.duyle.habit.Decorator.VerticalSpaceItemDecorator;
import habit.duyle.habit.R;
import habit.duyle.habit.adapters.SearchSuggestionAdapter;

/**
 * Created by Duy Le on 1/21/2017.
 */

public class SearchSuggestionFragment extends Fragment {
    private RecyclerView searchSuggestionRecyclerView;
    private static SearchSuggestionAdapter searchSuggestionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_view_suggestion_fragment,container,false);
        setIDs(view);
        setUpRecyclerView(view);

        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK ) {
                    MainActivity.getMainActivity().loadNewFeedFragment();
                }
                return false;
            }
        });
        return view;
    }
    public void setIDs(View view){
        searchSuggestionRecyclerView = (RecyclerView) view.findViewById(R.id.searchViewSuggestionRecyclerView);
    }
    public void setUpRecyclerView(View view){
        searchSuggestionAdapter = new SearchSuggestionAdapter(view.getContext(), MainActivity.getMainActivity().getSearchSuggestionHashMap());

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        searchSuggestionRecyclerView.setAdapter(searchSuggestionAdapter);
        searchSuggestionRecyclerView.setLayoutManager(layoutManager);
        searchSuggestionRecyclerView.setHasFixedSize(true);
        searchSuggestionRecyclerView.addItemDecoration(new VerticalSpaceItemDecorator(5));
    }
    public static void notifySearchAdapter(){
        getSearchSuggestionHashMapAdapter().notifyDataSetChanged();
    }
    public static SearchSuggestionAdapter getSearchSuggestionHashMapAdapter() {
        return searchSuggestionAdapter;
    }

}
