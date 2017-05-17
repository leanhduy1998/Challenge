package habit.duyle.habit.models;

import android.graphics.Color;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by leanh on 4/3/2017.
 */

public class Category {
    private ArrayList<Category> subCategory;
    private String categoryName;
    private int lightRGBColor;
    private int darkRGBColor;

    public int getLightRGBColor() {
        return lightRGBColor;
    }

    public int getDarkRGBColor() {
        return darkRGBColor;
    }

    public void setLightRGBColor(int lightRGBColor) {
        this.lightRGBColor = lightRGBColor;
    }

    public void setDarkRGBColor(int darkRGBColor) {
        this.darkRGBColor = darkRGBColor;
    }

    public Category(String category,int lightRGBColor,int darkRGBColor){
        this.lightRGBColor=lightRGBColor;
        this.darkRGBColor=darkRGBColor;
        categoryName = category;
        subCategory = new ArrayList<>();
    }
    public void addSubCategory(String sub){
        subCategory.add(new Category(sub,Color.rgb(0,0,0),Color.rgb(0,0,0)));
    }
    public Category(){

    }
    public ArrayList<Category> getSubCategory() {
        return subCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setSubCategory(ArrayList<Category> subCategory) {
        this.subCategory = subCategory;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
