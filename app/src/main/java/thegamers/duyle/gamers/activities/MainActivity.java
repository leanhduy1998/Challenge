package thegamers.duyle.gamers.Activities;

import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.kinesis.model.HashKeyRange;


import java.util.HashMap;

import thegamers.duyle.gamers.Fragments.AddNewHabitFragment;
import thegamers.duyle.gamers.Fragments.NewFeedFragment;
import thegamers.duyle.gamers.Fragments.NewFeedRowFragment;
import thegamers.duyle.gamers.R;


public class MainActivity extends AppCompatActivity {

    public static  People people=new People();
    private static MainActivity mainActivity;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    private static void setMainActivity(MainActivity mainActivity) {
        MainActivity.mainActivity = mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.setMainActivity(this);

        //Toast.makeText(getBaseContext(),selectedPeople.toString(),Toast.LENGTH_LONG).show();

/*
      //////////////test
        TextView test = (TextView) findViewById(R.id.testView);

        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here

                CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                        getBaseContext(), // Context
                        "us-east-1:0c67b780-c220-47d9-b361-fb192062f8a7", // Identity Pool ID
                        Regions.US_EAST_1 // Region]

                );

                AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
                final DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
                //People selectedPeople = mapper.load(People.class,"admin","24-Nov-2016 07:59:22 PM");
                //setResult(selectedPeople);

                GetItemRequest request = new GetItemRequest();
                request.withTableName("FeedData");
                AttributeValue username = new AttributeValue()
                        .withS("admin");
                HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
                key.put("username",username);
               request.withKey(key);

                GetItemResult response = ddbClient.getItem(request);

            }

        };
        Thread mythread = new Thread(runnable);
        mythread.start();


       // people=getResult();
       // String te=people.getTypeOfLength() +"POP";
       // Toast.makeText(getBaseContext(),te,Toast.LENGTH_LONG).show();



        ////////////test
*/



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if(fragment==null){
            fragment=new NewFeedFragment();
            FragmentTransaction transaction= manager.beginTransaction();
            transaction.add(R.id.fragment_container,fragment);
            transaction.commit();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }

    public void loadAddHabitFragment(){
        Fragment addHabitFragment = new AddNewHabitFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,addHabitFragment).addToBackStack(null).commit();
    }
    public void loadNewFeedFragment(){
        Fragment loadNewFeedFragment = new NewFeedFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,loadNewFeedFragment).addToBackStack(null).commit();
    }
    public void loadFeedRowFragment(){
        Fragment loadFeedRowFragment = new NewFeedRowFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.horizontalFeedFragmentContainter,loadFeedRowFragment).addToBackStack(null).commit();
    }

    public void setResult(People people){
        this.people=people;
    }
    public People getResult(){
        return people;
    }

}
