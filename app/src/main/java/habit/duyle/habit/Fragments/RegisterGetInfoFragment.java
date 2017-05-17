package habit.duyle.habit.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import habit.duyle.habit.Activities.MainActivity;
import habit.duyle.habit.R;
import habit.duyle.habit.models.User;
import habit.duyle.habit.services.FeedDataService;
import habit.duyle.habit.services.UserDataServices;

/**
 * Created by Duy Le on 12/31/2016.
 */

public class RegisterGetInfoFragment extends Fragment {
    private Button OKButton;
    private EditText firstNameET;
    private EditText middleNameET;
    private EditText lastNameET;
    private User currentUser;

    private static int sameNameCount=0;

    @SuppressLint("ValidFragment")
    public RegisterGetInfoFragment(User currentUser){
        this.currentUser=currentUser;
    }
    public RegisterGetInfoFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.register_get_info_fragment,container,false);
        setIDs(view);
        checkIfCurrentUserExist();
        setListeners();
        return view;
    }

    View.OnClickListener OKBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(requireInfoIsNotEmpty()){
                final DatabaseReference usersNameRef = FirebaseDatabase.getInstance().getReference("usersName/"+getLastNameInitial());
                usersNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                            if(postSnapshot.getKey().equals(getUsername())){
                                sameNameCount=sameNameCount+1;
                            }
                        }
                        User user =builderUser();
                        MainActivity.getMainActivity().setCurrentUser(user);

                        usersNameRef.child(getUsername()).setValue(getLimitedUserInfo());

                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                        userRef.child(getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                UserDataServices.getInstance().setUserNotInDatabase(false);
                                MainActivity.getMainActivity().loadNewFeedFragment();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    };
    private User builderUser(){
        User user = new User.Builder().firstName(getFirstName())
                .middleName(getMiddleName())
                .lastName(getLastName())
                .profilePicsUrls(getNewUserProfilePicUri())
                .username(getUsername())
                .getEmptyFriendList()
                .categoryLevels(new HashMap<String, Integer>())
                .build();
        return user;
    }
    private String getUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    private String getLastName(){
        return lastNameET.getText().toString();
    }
    private String getFirstName(){
        return firstNameET.getText().toString();
    }
    private String getMiddleName(){
        return middleNameET.getText().toString();
    }
    private String getUsername(){
        return getLastName()+"ZXCVBNM"+getFirstName()+"ZXCVBNM"+sameNameCount;
    }
    private HashMap getLimitedUserInfo(){
        HashMap info = new HashMap<>();
        info.put("uid",getUid());
        info.put("firstName",getFirstName());
        info.put("lastName",getLastName());
        info.put("middleName",getMiddleName());
        info.put("profilePicsUrls",getNewUserProfilePicUri());
        return info;
    }
    private ArrayList<String> getNewUserProfilePicUri(){
        ArrayList<String> profilePicsUrls = new ArrayList<>();
        profilePicsUrls.add("https://firebasestorage.googleapis.com/v0/b/habit-aa00d.appspot.com/o/app%2Fempty_profile_picture.jpg?alt=media&token=31a130f3-0f20-4362-89c5-54cb25fa6dcc");
        return profilePicsUrls;
    }
    private String getLastNameInitial(){
        return getLastName().substring(0,1).toUpperCase();
    }
    public boolean requireInfoIsNotEmpty(){
        if(getFirstName().isEmpty()){
            Toast.makeText(getContext(),"Please fill in your first name",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(getLastName().isEmpty()){
            Toast.makeText(getContext(),"Please fill in your last name",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void setIDs(View view){
        OKButton = (Button) view.findViewById(R.id.OKButton);
        firstNameET = (EditText) view.findViewById(R.id.firstNameET);
        middleNameET = (EditText) view.findViewById(R.id.middleNameET);
        lastNameET = (EditText) view.findViewById(R.id.lastNameET);
    }
    public void checkIfCurrentUserExist(){
        if(currentUser!=null){
            if(currentUser.getLastName()!=null&&!currentUser.getLastName().isEmpty()){
                lastNameET.setText(currentUser.getLastName());
            }
            if(currentUser.getMiddleName()!=null&&!currentUser.getMiddleName().isEmpty()){
                middleNameET.setText(currentUser.getMiddleName());
            }
            if(currentUser.getFirstName()!=null&&!currentUser.getFirstName().isEmpty()){
                firstNameET.setText(currentUser.getFirstName());
            }
            if(currentUser.getUsername()!=null&&!currentUser.getUsername().isEmpty()){
                firstNameET.setText(currentUser.getUsername());
            }
        }
    }
    public void setListeners(){
        OKButton.setOnClickListener(OKBtnListener);
    }


}
