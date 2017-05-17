package habit.duyle.habit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import habit.duyle.habit.R;
import habit.duyle.habit.holders.SearchSuggestionHolder;

/**
 * Created by Duy Le on 1/21/2017.
 */

public class SearchSuggestionAdapter extends RecyclerView.Adapter<SearchSuggestionHolder> {
    private Context context;
    private HashMap allUserHashMap;
    private final ArrayList<String> username = new ArrayList<>();

    public SearchSuggestionAdapter(Context context, HashMap allUserHashMap){
        this.context=context;
        this.allUserHashMap=allUserHashMap;
    }

    @Override
    public SearchSuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_suggestion_card,parent,false);
        addToUsernameArrayList();
        return new SearchSuggestionHolder(view);
    }
    private void addToUsernameArrayList(){
        ArrayList<String> usernamesKeys=new ArrayList<>(allUserHashMap.keySet());
        for(int i=0;i<usernamesKeys.size();i++){
            if(usernamesKeys.get(i).contains("name")){
                username.add(getUserNameOnly(usernamesKeys.get(i)));
            }
        }
    }
    private String getUserNameOnly(String string){
        int length = string.length();
        return string.substring(0,length-5);
    }

    @Override
    public void onBindViewHolder(SearchSuggestionHolder holder, int position) {
        holder.updateUI(getName(position),getProfilePicUri(position));
    }
    private String getName(int position){
        return allUserHashMap.get(username.get(position)+" name").toString();
    }
    private String getProfilePicUri(int position){
        return allUserHashMap.get(username.get(position)+" profilePicsUrls").toString();
    }

    @Override
    public int getItemCount() {
        return (null != allUserHashMap ? allUserHashMap.size()/2 : 0);
    }

    @Override
    public void onViewAttachedToWindow(SearchSuggestionHolder holder) {
        super.onViewAttachedToWindow(holder);
        addToUsernameArrayList();
    }

}
