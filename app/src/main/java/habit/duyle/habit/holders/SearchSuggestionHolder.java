package habit.duyle.habit.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import habit.duyle.habit.R;

/**
 * Created by Duy Le on 1/21/2017.
 */

public class SearchSuggestionHolder extends RecyclerView.ViewHolder {
    private TextView searchSuggestionTextView;
    private ImageView searchProfileIcon;
    public SearchSuggestionHolder(View itemView) {
        super(itemView);
        setIds();
    }
    private void setIds(){
        searchSuggestionTextView = (TextView) itemView.findViewById(R.id.searchSuggestionTextView);
        searchProfileIcon = (ImageView) itemView.findViewById(R.id.searchProfileIcon);
    }
    public void updateUI(String searchName,String searchprofilePicsUrls){
        searchSuggestionTextView.setText(searchName);
        Picasso.with(itemView.getContext()).load(searchprofilePicsUrls).resize(50,50).into(searchProfileIcon);
    }
}
