package habit.duyle.habit.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import habit.duyle.habit.Activities.MainActivity;
import habit.duyle.habit.R;
import habit.duyle.habit.holders.FeedPictureRowHolder;
import habit.duyle.habit.models.DayDesciption;
import habit.duyle.habit.models.Feed;
import habit.duyle.habit.models.FeedSinglePicture;
import habit.duyle.habit.models.User;
import habit.duyle.habit.services.FeedDataService;
import habit.duyle.habit.services.Database;
import habit.duyle.habit.services.PictureUploader;

/**
 * Created by Duy Le on 12/14/2016.
 */

public class UpdateHabitFragment extends Fragment {
    private Spinner habitSpinner;
    private Spinner daySpinner;
    private Spinner publicityUpdateSpinner;
    private Button doneButton;
    private Button cancelButton;

    private ArrayList<DayDesciption> dayDescriptionArrayList = new ArrayList<>();
    private ArrayList<FeedSinglePicture> pictureArrayList= null;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private boolean existedDescription=false;
    private Feed currentFeed;


    private EditText dayDescriptionEditText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_new_habit_fragment,container,false);
        setIDs(view);
        setAdapters(view);
        setListener();
        setVisibility();
        setUpPictureTakeContainer();
        return view;
    }
    public void setIDs(View view){
        habitSpinner = (Spinner) view.findViewById(R.id.habitSpinner);
        daySpinner = (Spinner) view.findViewById(R.id.daySpinner);
        doneButton = (Button) view.findViewById(R.id.doneButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        dayDescriptionEditText= (EditText) view.findViewById(R.id.dayDescriptionEditText);
        publicityUpdateSpinner= (Spinner) view.findViewById(R.id.publicityUpdateSpinner);
    }
    public void setVisibility(){
        dayDescriptionEditText.setVisibility(EditText.GONE);
    }
    public void setUpPictureTakeContainer(){
        android.support.v4.app.FragmentManager manager = getChildFragmentManager();
        Fragment fragment=new TakeNewPictureFrameFragment();
        FragmentTransaction transaction= manager.beginTransaction();
        transaction.add(R.id.takePictureFragmentContainer,fragment);
        transaction.commit();
    }
    public void setListener(){
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getMainActivity().loadNewFeedFragment();
            }
        });
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<dayDescriptionArrayList.size();i++){
                    if(dayDescriptionArrayList.get(i).getDay()==Integer.valueOf(daySpinner.getSelectedItem().toString())){
                        Toast.makeText(MainActivity.getMainActivity().getBaseContext(),"Only one description for one day is allowed",Toast.LENGTH_SHORT).show();
                        dayDescriptionEditText.setVisibility(EditText.GONE);
                        existedDescription=true;
                    }
                    else{
                        dayDescriptionEditText.setVisibility(EditText.VISIBLE);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePicture();
            }
        });
        final ArrayList<Feed> feedArrayList = FeedDataService.getInstance().getFeedArrayListForSorting();
        habitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dayDescriptionArrayList.removeAll(dayDescriptionArrayList);
                for(int i=0;i<feedArrayList.get(position).getDayDescriptionArrayList().size();i++){
                    if(!dayDescriptionArrayList.contains(feedArrayList.get(position).getDayDescriptionArrayList().get(i))){
                        dayDescriptionArrayList.add(feedArrayList.get(position).getDayDescriptionArrayList().get(i));
                        currentFeed=feedArrayList.get(position);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
    public void setAdapters(View view){
        ArrayAdapter publicityAdapter = new ArrayAdapter<String>(view.getContext(),R.layout.support_simple_spinner_dropdown_item,getPublicityStatusList());
        ArrayAdapter habitAdapter = new ArrayAdapter<String>(view.getContext(),R.layout.support_simple_spinner_dropdown_item,getHabitNameList());
        ArrayAdapter dayAdapter = new ArrayAdapter<Integer>(view.getContext(),R.layout.support_simple_spinner_dropdown_item,getDayList(30));
        publicityUpdateSpinner.setAdapter(publicityAdapter);
        habitSpinner.setAdapter(habitAdapter);
        daySpinner.setAdapter(dayAdapter);
    }

    public void updatePicture(){
        final DatabaseReference currentFeedReference  = FirebaseDatabase.getInstance().getReference("feed/"+user.getUid()+"/"+currentFeed.getCreatedTime()+"");

        pictureArrayList= createPictureArrayList();
        File[] trashFolderList = MainActivity.getMainActivity().getTrashFolder().listFiles();

        if(dayDescriptionEditText.getText().toString().isEmpty()){
            dayDescriptionEditText.setText("");
        }

        if(existedDescription==false){
            dayDescriptionArrayList.add(new DayDesciption(Integer.valueOf(daySpinner.getSelectedItem().toString()),dayDescriptionEditText.getText().toString()));
            Collections.sort(dayDescriptionArrayList, new Comparator<DayDesciption>() {
                @Override
                public int compare(DayDesciption o1, DayDesciption o2) {
                    if(o1.getDay()>o2.getDay()){
                        return 1;
                    }
                    else{
                        return -1;
                    }
                }
            });
            currentFeedReference.child("dayDescriptionArrayList").setValue(dayDescriptionArrayList);
        }

        if(trashFolderList.length==0){
            MainActivity.getMainActivity().loadNewFeedFragment();
            boolean emptyPicture = false;
            for(int i=0;i<pictureArrayList.size();i++){
                if(pictureArrayList.get(i).getPictureName().equals("noimage.jpg")&&pictureArrayList.get(i).getDay()==Integer.valueOf(daySpinner.getSelectedItem().toString())){
                    emptyPicture = true;
                }
            }
            if(!emptyPicture){
                FeedSinglePicture feedSinglePicture = new FeedSinglePicture.Builder()
                        .thumbnailStoragePath("nourl")
                        .pictureStoragePath("nourl")
                        .pictureName("noimage.jpg")
                        .day(Integer.valueOf(daySpinner.getSelectedItem().toString()))
                        .pictureName(habitSpinner.getSelectedItem().toString()).build();
                pictureArrayList.add(feedSinglePicture);

                currentFeedReference.child("allFeedPicture").setValue(pictureArrayList);
                Calendar c = Calendar.getInstance();
                currentFeedReference.child("updatedTime").setValue(c.getTimeInMillis());
            }
            MainActivity.getMainActivity().loadNewFeedFragment();
            NewFeedFragment.getNewFeedFragment().refreshData();
        }
        else{
            final ArrayList<DayDesciption> dayDescriptionArrayList = new ArrayList<DayDesciption>();
            dayDescriptionArrayList.add(getDescription());
            final ArrayList<FeedSinglePicture> allPictureinRow = new ArrayList<FeedSinglePicture>();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

            MainActivity.getMainActivity().loadNewFeedFragmentAndProgressBar();
            for(int i=0;i<trashFolderList.length;i++){
                File pic = new File(trashFolderList[i].getPath());
                final Uri picUri = Uri.fromFile(pic);
                final StorageReference picRef = storageRef.child("data/"+getUid()+"/"+getHabit()+"/"+picUri.getLastPathSegment());
                final StorageReference thumbnailRef = storageRef.child("data/"+getUid()+"/"+getHabit()+"/"+"THUMBNAIL"+"/"+picUri.getLastPathSegment());


                try {
                    PictureUploader.addNewPictureToDatabaseAndReturnUri(trashFolderList[i].getAbsolutePath(),getUid(),getHabit());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                FeedSinglePicture feedSinglePicture= new FeedSinglePicture.Builder()
                        .feedName(getHabit())
                        .pictureName(FeedDataService.getInstance().getTimeInMillisNow()+".jpg")
                        .pictureStoragePath(picRef.getPath())
                        .day(1)
                        .thumbnailStoragePath(thumbnailRef.getPath()).build();
                allPictureinRow.add(feedSinglePicture);


                ///

                Calendar c = Calendar.getInstance();
                Long timeNow = c.getTimeInMillis();

                ArrayList<FeedSinglePicture> temp = new ArrayList<FeedSinglePicture>();

                for(int y=0;y<pictureArrayList.size();y++){
                    if(pictureArrayList.get(y).getPictureName().equals("noimage.jpg")&&pictureArrayList.get(y).getDay()==Integer.valueOf(daySpinner.getSelectedItem().toString())){
                        temp.add(pictureArrayList.get(y));
                    }
                }
                pictureArrayList.removeAll(temp);

                FeedSinglePicture temp2 = new FeedSinglePicture.Builder().day(Integer.valueOf(daySpinner.getSelectedItem().toString()))
                        .pictureName(timeNow+".jpg")
                        .pictureStoragePath(picRef.getPath())
                        .thumbnailStoragePath(thumbnailRef.getPath())
                        .feedName(getHabit()).build();


                pictureArrayList.add(temp2);

                currentFeedReference.child("allFeedPicture").setValue(pictureArrayList);

                ArrayList<Integer> showDotButtonList = FeedPictureRowHolder.getShowDotButtonList();
                showDotButtonList.add(Integer.valueOf(daySpinner.getSelectedItem().toString()));

                Database.updateUidMetadataForPic(getUid(),picRef,getPublicity());
            }
            /*for(int i=0;i<trashFolderList.length;i++){
                Uri file = Uri.fromFile(new File(trashFolderList[i].getAbsolutePath()));
                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://habit-aa00d.firebaseio.com");
                final StorageReference picRef = storageRef.child("data/"+user.getUid()+File.separator+habitSpinner.getSelectedItem().toString()+File.separator+file.getLastPathSegment());

                UploadTask uploadTask = picRef.putFile(file);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri pictureUri = taskSnapshot.getDownloadUrl();


                    }
                });
            }*/
            MainActivity.getMainActivity().loadNewFeedFragment();
        }
    }
    
    private ArrayList<FeedSinglePicture> createPictureArrayList(){
        for(int i=0;i<FeedDataService.getInstance().getFeedArrayListForSorting().size();i++){
            if(habitSpinner.getSelectedItem().equals(FeedDataService.getInstance().getFeedArrayListForSorting().get(i).getHabitName())){
                pictureArrayList = FeedDataService.getInstance().getFeedArrayListForSorting().get(i).getAllFeedPicture();
            }
        }
        return pictureArrayList;
    }
    private ArrayList<Integer> getDayList(int times){
        ArrayList<Integer> dayList = new ArrayList<Integer>();
        for(int i=1;i<=times;i++){
            dayList.add(i);
        }
        return dayList;
    }
    private ArrayList<String> getHabitNameList(){
        ArrayList<String> habitNameList = new ArrayList<String>();
        for(int i=0;i< FeedDataService.getInstance().getFeedArrayListForSorting().size();i++){
            habitNameList.add(FeedDataService.getInstance().getFeedArrayListForSorting().get(i).getHabitName());
        }
        return habitNameList;
    }
    private ArrayList<String> getPublicityStatusList(){
        ArrayList<String> publicity = new ArrayList<String>();
        publicity.add("Public");
        publicity.add("Private");
        return publicity;
    }
    private String getUid(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser().getUid();
    }
    private String getHabit(){
        return habitSpinner.getSelectedItem().toString();
    }
    private String getPublicity(){
        return publicityUpdateSpinner.getSelectedItem().toString();
    }
    private DayDesciption getDescription(){
        return new DayDesciption((Integer.valueOf(daySpinner.getSelectedItem().toString())),dayDescriptionEditText.toString()) ;
    }

}
