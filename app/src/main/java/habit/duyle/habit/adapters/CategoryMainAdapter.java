package habit.duyle.habit.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import habit.duyle.habit.Activities.MainActivity;
import habit.duyle.habit.R;
import habit.duyle.habit.holders.CatergoryMainHolder;
import habit.duyle.habit.models.Category;

/**
 * Created by leanh on 2/16/2017.
 */

public class CategoryMainAdapter extends RecyclerView.Adapter<CatergoryMainHolder> {
    private Context context;
    private ArrayList<Category> categoriesAL;
    private String displayOrAdd;
    private boolean[] clicked = new boolean[8];
    public CategoryMainAdapter(Context context, ArrayList<Category> categoriesAL,String displayOrAdd){
        this.categoriesAL=categoriesAL;
        this.context=context;
        this.displayOrAdd=displayOrAdd;
    }
    public boolean[] getClicked() {
        return clicked;
    }
    @Override
    public CatergoryMainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_main_card,null);
        CatergoryMainHolder holder = new CatergoryMainHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CatergoryMainHolder holder, final int position) {
        final Category category = categoriesAL.get(position);
        holder.updateUI(category.getCategoryName(),category.getLightRGBColor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(displayOrAdd.equals("display")){
                    MainActivity.getMainActivity().loadIndividualCategoryFragment(category.getCategoryName());
                    MainActivity.getMainActivity().closeNavigationDrawer();
                }
                else{
                    if(!clicked[position]){
                        clicked[position]=true;
                        holder.updateUI(category.getCategoryName(),category.getDarkRGBColor());
                    }
                    else{
                        clicked[position]=false;
                        holder.updateUI(category.getCategoryName(),category.getLightRGBColor());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null!=categoriesAL?categoriesAL.size():0;
    }
}
