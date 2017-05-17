package habit.duyle.habit.holders;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import habit.duyle.habit.R;

/**
 * Created by leanh on 2/16/2017.
 */

public class CatergoryMainHolder extends RecyclerView.ViewHolder {
    private TextView categoryTV;
    private ImageView categoryIV;


    public CatergoryMainHolder(View view) {
        super(view);
        setIDs(view);
    }
    private void setIDs(View view){
        categoryTV=(TextView) view.findViewById(R.id.categoryTV);
        categoryIV=(ImageView) view.findViewById(R.id.categoryIV);
    }
    public void updateUI(String category,int rbg){
        categoryTV.setText(category);
        categoryIV.setColorFilter(rbg);
        // make sure to replace the string here with the string from R.String in the future!!

    }
}
