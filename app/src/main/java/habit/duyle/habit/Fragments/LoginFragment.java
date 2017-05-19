package habit.duyle.habit.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import habit.duyle.habit.Activities.MainActivity;
import habit.duyle.habit.R;
import habit.duyle.habit.models.User;
import habit.duyle.habit.services.CategoryData;
import habit.duyle.habit.services.Encryption;
import habit.duyle.habit.services.UserDataServices;
import habit.duyle.habit.services.Wifi;

/**
 * A login screen that offers login via email/password.
 */
public class LoginFragment extends Fragment implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private ConstraintLayout mLoginFormView;
    private CheckBox rememberMeCheckBox;
    private Button registerButton;
    private Button signInButton;

    private User user;

    private FirebaseAuth mAuth;
    private FirebaseApp app= FirebaseApp.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String AES_MODE = "AES/GCM/NoPadding";



    @Nullable
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.login_fragment,container,false);
        setIDs(view);
        setListeners();
        readUserData();
        return  view;
    }
    public void setIDs(View view){
        mAuth = FirebaseAuth.getInstance(app);
        // Set up the login form.
        mEmailView = (EditText) view.findViewById(R.id.email);
        //populateAutoComplete();
        mPasswordView = (EditText) view.findViewById(R.id.password);
        mLoginFormView = (ConstraintLayout) view.findViewById(R.id.login_form);
        rememberMeCheckBox = (CheckBox) view.findViewById(R.id.rememberMeCheckBox);
        mProgressView = view.findViewById(R.id.login_progress);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin(false);
                    return true;
                }
                return false;
            }
        });
        registerButton = (Button) view.findViewById(R.id.register_button);
        signInButton = (Button) view.findViewById(R.id.email_sign_in_button);
    }
    public void setListeners(){
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Wifi.wifiIsAvailable()){
                    attemptLogin(true);
                }
                else{
                    Toast.makeText(getContext(),R.string.wifi_required,Toast.LENGTH_SHORT).show();
                }
            }
        });


        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Wifi.wifiIsAvailable()){
                    attemptLogin(false);

                }
                else{
                    Toast.makeText(getContext(),R.string.wifi_required,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void readUserData(){
        Encryption encryption = new Encryption();
        MainActivity.getMainActivity().readUserData();
        //if rememberMe is true
        if(Boolean.valueOf(UserDataServices.getInstance().getRememberMe())){
            mEmailView.setText(UserDataServices.getInstance().getEmail());
            mPasswordView.setText(encryption.decryptPassword());
            //if automateLogin is true
            if(Boolean.valueOf(UserDataServices.getInstance().getAutomateLogin())){
                signInButton.performClick();
            }
        }
    }
/*
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        if (VERSION.SDK_INT >= 14) {
            // Use ContactsContract.Profile (API 14+)
            // Use ContactsContract.Profile (API 14+)
            getLoaderManager().initLoader(0, null, this);
        } else if (VERSION.SDK_INT >= 8) {
            // Use AccountManager (API 8+)
            new SetupEmailAutoCompleteTask().execute(null, null);
        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }
    */

    /**
     * Callback received when a permissions request has been completed.
     */
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }
*/

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(boolean isRegistering) {
        if (mAuth == null) {
            return;
        }
        if(checkRequiredInfo()){
            // Reset errors.
            mEmailView.setError(null);
            mPasswordView.setError(null);

            // Store values at the time of the login attempt.
            final String email = mEmailView.getText().toString();
            final String password = mPasswordView.getText().toString();

            boolean cancel = false;
            View focusView = null;

        /*
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
*/
            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                showProgress(true);
                if (isRegistering) {
                    registerUser(email,password);
                } else {
                    signInUser(email,password);
                }
            }
        }
    }
    private void registerUser(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.getMainActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showProgress(false);
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.getMainActivity().getBaseContext(), "Could Not Register",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            MainActivity.getMainActivity().loadRegisterGetInfoFragment();
                        }
                    }
                });
    }
    private void signInUser(final String email, final String password){
        // do not put this in Main class because login could failed and this would never been run
        CategoryData.getInstance().getCategoriesFromDB();

        mAuth.signInWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e.toString().equals("com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.")){
                    Toast.makeText(MainActivity.getMainActivity().getBaseContext(),R.string.register_required,Toast.LENGTH_SHORT).show();
                    registerUser(email, password);
                }
                else if(e.toString().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.")){
                    Toast.makeText(MainActivity.getMainActivity().getBaseContext(),R.string.wrong_password,Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }
                else{
                    throw new RuntimeException(e.toString());
                }
            }
        })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        showProgress(false);
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users/"+getUid());
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getKey().equals(getUid())&&dataSnapshot.getValue()!=null){
                                    UserDataServices.getInstance().setUserNotInDatabase(false);
                                    User currentUser = getUserData(dataSnapshot,new User.Builder());
                                    MainActivity.getMainActivity().setCurrentUser(currentUser);
                                    MainActivity.getMainActivity().loadCategoryFragment();
                                    MainActivity.getMainActivity().loadNewFeedFragment();
                                }
                                if(UserDataServices.getInstance().isUserNotInDatabase()  ){
                                    MainActivity.getMainActivity().loadRegisterGetInfoFragment();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        MainActivity.getMainActivity().deleteAppFolder();
                        MainActivity.getMainActivity().saveUserData(email,rememberMeCheckBox.isChecked()+"",true+"");

                        Encryption encryption = new Encryption();
                        encryption.encryptPassword(password);

                        MainActivity.getMainActivity().setAuth(mAuth);
                    }
                });
    }
    public boolean checkRequiredInfo(){
        if(mEmailView.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.getMainActivity().getBaseContext(),"Please fill in email address!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mPasswordView.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.getMainActivity().getBaseContext(),"Please fill in password!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private synchronized User getUserData(DataSnapshot dataSnapshot, User.Builder userBuilder){
        for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
            switch(postSnapshot.getKey()){
                default:
                    break;
                case "firstName":
                    userBuilder.firstName(postSnapshot.getValue(String.class));
                    break;
                case "middleName":
                    userBuilder.middleName(postSnapshot.getValue(String.class));
                    break;
                case "lastName":
                    userBuilder.lastName(postSnapshot.getValue(String.class));
                    break;
                case "username":
                    userBuilder.username(postSnapshot.getValue(String.class));
                    break;
                case "profilePicsUrls":
                    GenericTypeIndicator<ArrayList<String>> profilePicsUrlIndicator = new GenericTypeIndicator<ArrayList<String>>() {
                    };
                    ArrayList<String> profilePicsUrl = postSnapshot.getValue(profilePicsUrlIndicator);
                    userBuilder.profilePicsUrls(profilePicsUrl);
                    break;
                case "friendUid":
                    GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {
                    };
                    ArrayList<String> retrieved = postSnapshot.getValue(t);
                    userBuilder.friendUid(retrieved);
                    break;
                case "categoryLevels":
                    GenericTypeIndicator<HashMap<String,Integer>> t2 = new GenericTypeIndicator<HashMap<String,Integer>>() {
                    };
                    HashMap<String,Integer> retrieved2 = postSnapshot.getValue(t2);
                    userBuilder.categoryLevels(retrieved2);
                    break;
            }
            if(!userBuilder.isNull()){
                User user =userBuilder.build();
                return user;
            }
        }
        return null;
    }
    private String getUid(){
        return mAuth.getCurrentUser().getUid();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
               @Override
               public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
/*
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }*/

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

       // addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
/*
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginFragment.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }
*/
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Use an AsyncTask to fetch the user's email addresses on a background thread, and update
     * the email text field with results on the main UI thread.
     */
    /*
    class SetupEmailAutoCompleteTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            ArrayList<String> emailAddressCollection = new ArrayList<>();

            // Get all emails from the user's contacts and copy them to a list.
            ContentResolver cr = getContentResolver();
            Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                    null, null, null);
            while (emailCur.moveToNext()) {
                String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract
                        .CommonDataKinds.Email.DATA));
                emailAddressCollection.add(email);
            }
            emailCur.close();

            return emailAddressCollection;
        }

        @Override
        protected void onPostExecute(List<String> emailAddressCollection) {
            addEmailsToAutoComplete(emailAddressCollection);
        }
    }
*/
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    /*
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            showProgress(false);
        }
    }*/
}

