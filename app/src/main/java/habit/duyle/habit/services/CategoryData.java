package habit.duyle.habit.services;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import habit.duyle.habit.Activities.MainActivity;
import habit.duyle.habit.Fragments.CategoryMainFragment;
import habit.duyle.habit.models.Category;

/**
 * Created by leanh on 4/3/2017.
 */

public class CategoryData {
    private static CategoryData categoryDataInstance = new CategoryData();
    private  ArrayList<Category> categoryArrayList = new ArrayList<>();
    public static CategoryData getInstance(){
        return categoryDataInstance;
    }
    public ArrayList<Category> getCategoryArrayList(){
        return categoryArrayList;
    }

    private void setCategoryArrayList(ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }

    public void getCategoriesFromDB(){
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("category");
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    GenericTypeIndicator<Category> t = new GenericTypeIndicator<Category>() {};
                    Category category = postSnapshot.getValue(t);
                    categoryArrayList.add(category);
                    if(categoryArrayList.size()==8){
                        setCategoryArrayList(categoryArrayList);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
