package habit.duyle.habit.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import habit.duyle.habit.models.Category;
import habit.duyle.habit.models.DayDesciption;
import habit.duyle.habit.models.Feed;
import habit.duyle.habit.Activities.MainActivity;
import habit.duyle.habit.R;
import habit.duyle.habit.models.FeedSinglePicture;
import habit.duyle.habit.models.User;
import habit.duyle.habit.services.FeedDataService;
import habit.duyle.habit.services.Database;
import habit.duyle.habit.services.PictureUploader;


/**
 * Created by Duy Le on 11/5/2016.
 */

public class AddNewHabitFragment extends Fragment {
    private Button takePictureB;
    private ImageView imageView;
    private Spinner typeOfLengthSpinner;
    private static Spinner publicitySpinner;
    private EditText habitEditText;
    private EditText amountOfDaysEditText;
    private EditText feedDescriptionEditText;
    private Button doneButton;
    private EditText dayDescriptionEditText;
    private Button cancelButton;
    private static String thumbnail="THUMBNAIL";
    private Button retakePictureButton;
    private Button addMorePictureButton;
    private ImageButton categoryShowBtn;
    private FrameLayout categoryContainer;

    private static final int CAM_REQUEST=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_habit_fragment,container,false);
        setIDs(view);
        setListener();
        setAdapters(view);
        setUpPictureTakeContainer();
        setUpCategoryContainer();
        return view;
    }
    public void setIDs(View view){
        doneButton = (Button) view.findViewById(R.id.doneButton);
        habitEditText=(EditText) view.findViewById(R.id.habitEditText);
        amountOfDaysEditText=(EditText)view.findViewById(R.id.amountOfDaysEditText);
        feedDescriptionEditText=(EditText) view.findViewById(R.id.feedDescriptionEditText);
        dayDescriptionEditText = (EditText) view.findViewById(R.id.dayDescriptionEditText);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        categoryShowBtn = (ImageButton) view.findViewById(R.id.category_show_btn);
        typeOfLengthSpinner = (Spinner) view.findViewById(R.id.typeOfLengthSpinner);
        publicitySpinner = (Spinner) view.findViewById(R.id.publicitySpinner);
        categoryContainer=(FrameLayout) view.findViewById(R.id.category_container);
    }
    public void setListener(){
        cancelButton.setOnClickListener(cancelBtnListener);
        doneButton.setOnClickListener(doneBtnListener);
        categoryShowBtn.setOnClickListener(categoryShowListener);
    }
    public void setAdapters(View view){
        ArrayAdapter typeOfLengthAdapter = new ArrayAdapter<String>(view.getContext(),R.layout.support_simple_spinner_dropdown_item,createTypeOfLengthList());
        typeOfLengthSpinner.setAdapter(typeOfLengthAdapter);

        ArrayAdapter publicityAdapter = new ArrayAdapter<String>(view.getContext(),R.layout.support_simple_spinner_dropdown_item,createPublicityList());
        publicitySpinner.setAdapter(publicityAdapter);
    }
    private void setUpPictureTakeContainer(){
        android.support.v4.app.FragmentManager manager = getChildFragmentManager();
        Fragment fragment=new TakeNewPictureFrameFragment();
        FragmentTransaction transaction= manager.beginTransaction();
        transaction.add(R.id.takePictureFragmentContainer,fragment);
        transaction.commit();
    }
    private void setUpCategoryContainer(){
        categoryContainer.setVisibility(View.GONE);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        CategoryMainFragment categoryMainFragment = new CategoryMainFragment("add");
        ft.add(R.id.category_container,categoryMainFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(categoryMainFragment.getClass().getName());
        ft.commit();
    }

    View.OnClickListener categoryShowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(categoryContainer.getVisibility()==View.VISIBLE){
                categoryContainer.setVisibility(View.GONE);
            }
            else{
                categoryContainer.setVisibility(View.VISIBLE);
            }
        }
    };
    View.OnClickListener cancelBtnListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity.getMainActivity().loadNewFeedFragment();
        }
    };
    View.OnClickListener doneBtnListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(requiredInfoIsNotEmpty()){
                fixFeedDescriptionTextIfEmpty();
                fixDayDescriptionTextIfEmpty();

                final File[] trashFolderList = (new File(MainActivity.getMainActivity().getTrashFolder().getAbsolutePath())).listFiles();
                alertIfHabitExist();

                if(trashFolderList.length==0){
                    ArrayList<DayDesciption> dayDescriptionArrayList = new ArrayList<DayDesciption>();
                    dayDescriptionArrayList.add(createFirstDayDescription());

                    ArrayList<FeedSinglePicture> allPictureinRow = new ArrayList<FeedSinglePicture>();
                    FeedSinglePicture feedSinglePicture= new FeedSinglePicture.Builder()
                            .feedName(habitEditText.getText().toString())
                            .pictureName("noimage.jpg")
                            .pictureStoragePath("nourl")
                            .day(1)
                            .thumbnailStoragePath("noimage.jpg").build();
                    allPictureinRow.add(feedSinglePicture);
                    Feed feed = buildFeed(allPictureinRow,dayDescriptionArrayList);


                    Database.uploadDataToDatabase(feed,getUid(),publicitySpinner.getSelectedItem().toString());
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.loadNewFeedFragment();
                }
                else{
                    final ArrayList<DayDesciption> dayDescriptionArrayList = new ArrayList<DayDesciption>();
                    dayDescriptionArrayList.add(createFirstDayDescription());
                    final ArrayList<FeedSinglePicture> allPictureinRow = new ArrayList<FeedSinglePicture>();
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();


                    MainActivity.getMainActivity().loadNewFeedFragmentAndProgressBar();
                    for(int i=0;i<trashFolderList.length;i++){
                        File pic = new File(trashFolderList[i].getPath());
                        final Uri picUri = Uri.fromFile(pic);
                        final StorageReference picRef = storageRef.child("data/"+getUid()+"/"+getHabit()+"/"+picUri.getLastPathSegment());
                        final StorageReference thumbnailRef = storageRef.child("data/"+getUid()+"/"+getHabit()+"/"+thumbnail+"/"+picUri.getLastPathSegment());

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

                        Feed feed = buildFeed(allPictureinRow,dayDescriptionArrayList);

                        Database.uploadDataToDatabase(feed,getUid(),getPublicity());
                        Database.updateUidMetadataForPic(getUid(),picRef,getPublicity());
                        }


                    }
                }
        }

    };
    private Feed buildFeed(ArrayList<FeedSinglePicture> allPictureInRow,ArrayList<DayDesciption> dayDescriptionArrayList){
        User user = MainActivity.getMainActivity().getCurrentUser();
        Feed feed = new Feed.Builder().allFeedPicture(allPictureInRow)
                .feedDescription(getFeedDescription())
                .amountOfDay(getAmountOfDay())
                .createdTime(FeedDataService.getInstance().getTimeInMillisNow())
                .updatedTime(FeedDataService.getInstance().getTimeInMillisNow())
                .habitName(getHabit())
                .username(user.getUsername())
                .typeOfLength(getTypeOfLength())
                .publicity(getPublicity())
                .uid(getUid())
                .categoryArrayList(new ArrayList<Category>())
                .dayDescriptionArrayList(dayDescriptionArrayList)
                .build();
        return feed;
    }
    private String getFeedDescription(){
        return feedDescriptionEditText.getText().toString();
    }
    private int getAmountOfDay(){
        return Integer.valueOf(amountOfDaysEditText.getText().toString());
    }
    private String getUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    private String getHabit(){
        return habitEditText.getText().toString();
    }
    private String getTypeOfLength(){
        return typeOfLengthSpinner.getSelectedItem().toString();
    }
    private static String getPublicity(){
        return publicitySpinner.getSelectedItem().toString();
    }

    private boolean requiredInfoIsNotEmpty(){
        if(habitEditText.getText().toString().isEmpty()){
            return false;
        }
        if(amountOfDaysEditText.getText().toString().isEmpty()){
            Toast.makeText(getContext(),"Please fill in the amount of days!",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    private DayDesciption createFirstDayDescription(){
        return new DayDesciption(1,dayDescriptionEditText.getText().toString());
    }

    private void fixFeedDescriptionTextIfEmpty(){
        if(feedDescriptionEditText.getText().toString().isEmpty()){
            feedDescriptionEditText.setText("");
        }
    }
    private void fixDayDescriptionTextIfEmpty(){
        if(dayDescriptionEditText.getText().toString().isEmpty()){
            dayDescriptionEditText.setText("");
        }
    }

    private ArrayList<String> createTypeOfLengthList(){
        ArrayList<String> typeOfLength = new ArrayList<String>();
        typeOfLength.add("Day(s)");
        typeOfLength.add("Month(s)");
        typeOfLength.add("Year(s)");
        return typeOfLength;
    }
    private ArrayList<String> createPublicityList(){
        ArrayList<String> publicity = new ArrayList<String>();
        publicity.add("Public");
        publicity.add("Private");
        return publicity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        addMorePictureButton.setVisibility(View.VISIBLE);
        takePictureB.setText("Retake Picture");
        File[] picShow = new File(MainActivity.getMainActivity().getTrashFolder().getAbsolutePath()).listFiles();
        File file = new File(MainActivity.getMainActivity().getTrashFolder().getAbsolutePath()+picShow[0].getName());
        Picasso.with(getContext()).load(file).resize(400,200).into(imageView);
    }
    public void alertIfHabitExist(){
        for(int i=0;i< FeedDataService.getInstance().getFeedArrayListForSorting().size();i++){
            if(FeedDataService.getInstance().getFeedArrayListForSorting().get(i).getHabitName().equals(habitEditText.getText().toString())){
                Toast.makeText(MainActivity.getMainActivity().getBaseContext(),"There is already an existing habit!",Toast.LENGTH_SHORT).show();
            }
        }
    }




}


