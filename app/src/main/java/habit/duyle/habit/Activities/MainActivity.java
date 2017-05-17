package habit.duyle.habit.Activities;

import android.Manifest;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;


import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import habit.duyle.habit.Fragments.AddNewHabitFragment;
import habit.duyle.habit.Fragments.CategoryMainFragment;
import habit.duyle.habit.Fragments.IndividualCategoryFragment;
import habit.duyle.habit.Fragments.LoginFragment;
import habit.duyle.habit.Fragments.NewFeedFragment;
import habit.duyle.habit.Fragments.ProfileFeedFragment;
import habit.duyle.habit.Fragments.RegisterGetInfoFragment;
import habit.duyle.habit.Fragments.ScreenSlidePagerFragment;
import habit.duyle.habit.Fragments.SearchSuggestionFragment;
import habit.duyle.habit.Fragments.UpdateHabitFragment;
import habit.duyle.habit.R;
import habit.duyle.habit.models.Category;
import habit.duyle.habit.models.FeedSinglePicture;
import habit.duyle.habit.models.User;
import habit.duyle.habit.services.EditFile;
import habit.duyle.habit.services.UserDataServices;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;
    private static MainActivity mainActivity;
    private User currentUser;
    private HashMap searchSuggestionHashMap = new HashMap();

    private SearchView searchView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private ProgressBar mainProgressBar;
    private static final int REQUEST_CODE_PERMISSION = 2;

    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }
    public static MainActivity getMainActivity() {
        return mainActivity;
    }
    public HashMap getSearchSuggestionHashMap() {
        return searchSuggestionHashMap;
    }


    private static void setMainActivity(MainActivity mainActivity) {
        MainActivity.mainActivity = mainActivity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkWritingPermission();
        setContentView(R.layout.activity_main);
        setIDs();
        setUpDatabaseForAdminOnly();

        hideProgressBar();
        trimCacheIfMemoryIsLow();
        loadLoginFragment();
    }
    private void setIDs(){
        setMainActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainProgressBar = (ProgressBar) findViewById(R.id.mainProgressBar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //if drawer is not clickable, reordering navigation view to be on the bottom in xml
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }
    public void closeNavigationDrawer(){
        drawer.closeDrawers();
    }
    private void trimCacheIfMemoryIsLow(){
        Glide.get(this).trimMemory(ComponentCallbacks2.TRIM_MEMORY_MODERATE);
    }

    private void setUpDatabaseForAdminOnly(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("category");

            Category category = new Category("Food", Color.rgb(248,189,10),Color.rgb(221, 169, 8));
            category.addSubCategory("Cooking");
            categoryRef.child("Food").setValue(category);


            category = new Category("Intelligent",Color.rgb(205,123,221),Color.rgb(181, 107, 196));
            category.addSubCategory("Books");
            category.addSubCategory("Talks");
            category.addSubCategory("Reading News");
            categoryRef.child("Intelligent").setValue(category);


            //category = new Category("Spiritual",Color.rgb(0,124,220));
        category = new Category("Spiritual",Color.rgb(56, 168, 255),Color.rgb(0,124,220));
            category.addSubCategory("Reading Holy Book");
            categoryRef.child("Spiritual").setValue(category);

            category = new Category("Health",Color.rgb(139,195,67),Color.rgb(124, 173, 60));
            category.addSubCategory("Diet");
            categoryRef.child("Health").setValue(category);

            category = new Category("Sport",Color.rgb(255,19,68),Color.rgb(209, 14, 55));
            category.addSubCategory("Running");
            category.addSubCategory("Lifting");
            categoryRef.child("Sport").setValue(category);

            category = new Category("Entertainment",Color.rgb(253,131,69),Color.rgb(206, 107, 57));
            category.addSubCategory("Hanging out");
            categoryRef.child("Entertainment").setValue(category);

            category = new Category("Love life",Color.rgb(253,131,69),Color.rgb(206, 107, 57));
            category.addSubCategory("Asking someone out");
            categoryRef.child("Love life").setValue(category);

            category = new Category("Drawing",Color.rgb(208,209,212),Color.rgb(171, 172, 173));
            category.addSubCategory("Art");
            categoryRef.child("Drawing").setValue(category);

    }
    public void hideProgressBar(){
        mainProgressBar.setVisibility(View.GONE);
    }
    public void showProgressBar(){
        mainProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
            searchView = (SearchView) searchMenuItem.getActionView();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(!newText.equals("")){
                        String[] eachWord = newText.split("\\s+");
                        if(eachWord.length==1){
                            addUserToList(capitalizeLetter(eachWord[0]));
                        }
                        else{
                            for(int i=0;i<eachWord.length;i++){
                                for(int y=0;y<eachWord.length;y++){
                                    if(!eachWord[i].equals(eachWord[y])){
                                        addUserToList(capitalizeLetter(eachWord[i])+"ZXCVBNM"+capitalizeLetter(eachWord[y]));
                                    }
                                }
                            }
                        }
                    }
                    else{
                        searchSuggestionHashMap.clear();
                        SearchSuggestionFragment.notifySearchAdapter();
                    }

                    return true;
                }
            });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if(!queryTextFocused) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                    getSupportFragmentManager().popBackStack();
                }
            }
        });

        return true;
    }
    private String capitalizeLetter(String text){
        if(text.length()>1){
            return (text.substring(0,1).toUpperCase()+text.substring(1,text.length()).toLowerCase()).replace("zxcvbnm","ZXCVBNM");
        }
        return text.toUpperCase();
    }

    private void addUserToList(final String combination){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("usersName/"+capitalizeLetter(combination.substring(0,1)));
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    if(postSnapshot.getKey().contains(combination)){
                        searchSuggestionHashMap.put(postSnapshot.getKey()+" name",getFullNamefromDataSnapshot(postSnapshot));
                        searchSuggestionHashMap.put(postSnapshot.getKey()+" profilePicsUrls",getPhotoUrlFromDataSnapshot(postSnapshot));
                        SearchSuggestionFragment.notifySearchAdapter();
                    }
                    else{
                        searchSuggestionHashMap.remove(postSnapshot.getKey()+" name");
                        searchSuggestionHashMap.put(postSnapshot.getKey()+" profilePicsUrls",getPhotoUrlFromDataSnapshot(postSnapshot));
                        SearchSuggestionFragment.notifySearchAdapter();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String getFullNamefromDataSnapshot(DataSnapshot dataSnapshot){
        return dataSnapshot.child("firstName").getValue(String.class)+" "
                +dataSnapshot.child("middleName").getValue(String.class)+" "
                +dataSnapshot.child("lastName").getValue(String.class);
    }
    private ArrayList<String> getPhotoUrlFromDataSnapshot(DataSnapshot dataSnapshot){
        ArrayList<String> profilePicsUrls =(ArrayList)dataSnapshot.child("profilePicsUrls").getValue();
        return profilePicsUrls;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_logout){
            UserDataServices.getInstance().setAutomateLogin(false+"");
            saveUserData(UserDataServices.getInstance().getEmail(),
                    UserDataServices.getInstance().getRememberMe(),UserDataServices.getInstance().getAutomateLogin());
            setCurrentUser(null);
            getMainActivity().loadLoginFragment();
        }
        if(id==R.id.action_search){
            loadSearchSuggestionFragment();
        }
        if(id==R.id.action_show_profile){
            loadProfileFeedFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return super.onOptionsItemSelected(item);
    }

    public void saveUserData(String email, String rememberMe, String automateLogin){
        try {
            File file = new File(getAppFolder().getAbsolutePath()+File.separator, "User's Data.txt");
            FileWriter writer = new FileWriter(file);
            writer.append(email+"\n");
            writer.append(rememberMe+"\n");
            writer.append(automateLogin+"");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readUserData(){
        File appFolder=getAppFolder();
        File[] userStatusData = appFolder.listFiles();
        if(userStatusData!=null && userStatusData.length>0){
            try {
                File userData = new File(appFolder.getAbsolutePath()+File.separator,  "User's Data.txt");
                BufferedReader br = new BufferedReader(new FileReader(userData));
                UserDataServices.getInstance().setEmail(String.valueOf(br.readLine()));
                UserDataServices.getInstance().setRememberMe(String.valueOf(br.readLine()));
                UserDataServices.getInstance().setAutomateLogin(String.valueOf(br.readLine()));
                br.close() ;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void loadSearchSuggestionFragment(){
        Fragment searchSuggestionFragment = new SearchSuggestionFragment();
        replaceFragment(searchSuggestionFragment);
    }
    public void loadAddHabitFragment(){
        Fragment addHabitFragment = new AddNewHabitFragment();
        replaceFragment(addHabitFragment);
    }
    public void loadNewFeedFragmentAndProgressBar(){
        loadNewFeedFragment();
        showProgressBar();
    }

    public void loadNewFeedFragment(){
        loadCategoryFragment();

        NewFeedFragment loadNewFeedFragment = new NewFeedFragment();
        if(loadNewFeedFragment.getRecyclerAdapter()!=null){
            loadNewFeedFragment.getRecyclerAdapter().notifyDataSetChanged();
        }
        MainActivity.getMainActivity().hideProgressBar();
        replaceFragment(loadNewFeedFragment);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void loadUpdateHabitFragment(){
        Fragment updateHabitFragment = new UpdateHabitFragment();
        replaceFragment(updateHabitFragment);
    }
    public void loadPictureSlideShowFragment(FeedSinglePicture feedSinglePicture){
        ScreenSlidePagerFragment screenSlidePagerFragment = new ScreenSlidePagerFragment(feedSinglePicture);
        replaceFragment(screenSlidePagerFragment);
    }
    public void loadLoginFragment(){
        Fragment loginFragment = new LoginFragment();
        replaceFragment(loginFragment);
    }
    public void loadRegisterGetInfoFragment(){
        Fragment registerGetInfoFragment = new RegisterGetInfoFragment();
        replaceFragment(registerGetInfoFragment);
    }
    public void loadProfileFeedFragment(){
        Fragment profileFeedFragment = new ProfileFeedFragment();
        replaceFragment(profileFeedFragment);
    }
    private void loadCategoryFragment(){
        Fragment categoryFragment = new CategoryMainFragment("display");
        replaceFragment(categoryFragment,R.id.category_container);
    }
    public void loadIndividualCategoryFragment(String category){
        IndividualCategoryFragment individualCategoryFragment = new IndividualCategoryFragment(category);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, individualCategoryFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(individualCategoryFragment.getClass().getName());
        ft.commit();
    }
    public void replaceFragment(Fragment fragment){
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate (backStateName, 0);
        if ((!fragmentPopped && getSupportFragmentManager().findFragmentByTag(fragmentTag) == null)){ //fragment not in back stack, create it.
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
        getSupportFragmentManager().executePendingTransactions();
    }
    public void replaceFragment(Fragment fragment, int containerID){
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate (backStateName, 0);
        if ((!fragmentPopped && getSupportFragmentManager().findFragmentByTag(fragmentTag) == null)){ //fragment not in back stack, create it.
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(containerID, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
        getSupportFragmentManager().executePendingTransactions();
    }
    public void refreshFragment(Fragment fragment){
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate (backStateName, 0);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.replace(R.id.fragment_container, fragment, fragmentTag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void deleteTrashFolder(){
        final File[] trashFolderList = getTrashFolder().listFiles();
        int count = trashFolderList.length;
        for(int i=0;i<count;i++){
            EditFile.deleteFile(SDorInternalDirectory()+"/camera_app/"+auth.getCurrentUser().getUid()+File.separator+"trash/",trashFolderList[0].getName());
        }
    }
    public void deleteAppFolder(){
        File appFolder = getMainActivity().getAppFolder();
        if(appFolder.length()>0){
            File[] files = appFolder.listFiles();
            for(int i=0;i<files.length;i++){
                EditFile.deleteFile(appFolder.getAbsolutePath()+File.separator,files[i].getName());
            }
        }
    }

    public File getTrashFolder(){
        File file=new File(SDorInternalDirectory()+"/camera_app/"+auth.getCurrentUser().getUid()+File.separator+"trash/");
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }
    public File getThumbnailFolder(){
        File file = new File(SDorInternalDirectory()+"/camera_app/"+auth.getCurrentUser().getUid()+File.separator+"thumbnail/");
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }
    public File getAppFolder(){
        File appFolder = new File(SDorInternalDirectory()+"/camera_app/"+"app/");
        if(!appFolder.exists()){
            appFolder.mkdir();
        }
        return appFolder;
    }
    private String SDorInternalDirectory(){
        return Environment.getExternalStorageDirectory().toString();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        int count = getSupportFragmentManager().getBackStackEntryCount();
        int lastItemInBackStack=2;
        if (count <=lastItemInBackStack) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
            if(getCurrentUser()!=null){
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                userRef.child(firebaseUser.getUid()).setValue(getCurrentUser());
            }
            MainActivity.getMainActivity().finish();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
            } else {
                // permission wasn't granted
            }
        }
    }

    private void checkWritingPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // permission wasn't granted
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
