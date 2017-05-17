package habit.duyle.habit.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import habit.duyle.habit.Activities.MainActivity;
import habit.duyle.habit.R;
import habit.duyle.habit.models.User;
import habit.duyle.habit.services.MissingOrWrongDataException;

/**
 * Created by leanh on 2/11/2017.
 */

public class ProfilePicAndInfoFragment extends Fragment {
    private ImageView mainProfileIV;
    private ImageView leftSideProfileIV;
    private ImageView rightSideProfileIV;
    private TextView profileNameTV;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_pic_and_info_fragment,container,false);
        context=view.getContext();
        setId(view);
        updateProfileInfo();
        return view;
    }
    private void setId(View view){
        mainProfileIV = (ImageView) view.findViewById(R.id.mainProfileIV);
        leftSideProfileIV = (ImageView) view.findViewById(R.id.leftSideProfileIV);
        rightSideProfileIV = (ImageView) view.findViewById(R.id.rightSideProfileIV);
        profileNameTV = (TextView) view.findViewById(R.id.profileNameTV);
    }
    private void updateProfileInfo(){
        User user =MainActivity.getMainActivity().getCurrentUser();
        String name =user.getFirstName()+user.getMiddleName()+user.getLastName();
        updateProfileInfo(user.getprofilePicsUrls(),name);
    }

    private void updateProfileInfo(ArrayList<String> photoUrl,String name)  {
        leftSideProfileIV.setVisibility(View.VISIBLE);
        rightSideProfileIV.setVisibility(View.VISIBLE);
        mainProfileIV.setVisibility(View.VISIBLE);
        setPicsForIVs(photoUrl);
        profileNameTV.setText(name);
    }
    private void setPicsForIVs(ArrayList<String> photoUrl){
        switch(photoUrl.size()){
            case 0:
                try {
                    throw new MissingOrWrongDataException("User has no photo url!!!");
                } catch (MissingOrWrongDataException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                Picasso.with(context).load(photoUrl.get(0)).resize(100,100).into(mainProfileIV);
                leftSideProfileIV.setVisibility(View.INVISIBLE);
                rightSideProfileIV.setVisibility(View.INVISIBLE);
                break;
            case 2:
                Picasso.with(context).load(photoUrl.get(0)).resize(100,100).into(leftSideProfileIV);
                Picasso.with(context).load(photoUrl.get(0)).resize(100,100).into(rightSideProfileIV);
                mainProfileIV.setVisibility(View.INVISIBLE);
                break;
            case 3:
                Picasso.with(context).load(photoUrl.get(0)).resize(100,100).into(leftSideProfileIV);
                Picasso.with(context).load(photoUrl.get(1)).resize(100,100).into(mainProfileIV);
                Picasso.with(context).load(photoUrl.get(2)).resize(100,100).into(rightSideProfileIV);
                break;
            default:
                try {
                    throw new MissingOrWrongDataException("More than 3 profile photo urls");
                } catch (MissingOrWrongDataException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
