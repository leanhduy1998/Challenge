package thegamers.duyle.gamers.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import thegamers.duyle.gamers.Fragments.AddNewHabitFragment;
import thegamers.duyle.gamers.Fragments.NewFeedFragment;
import thegamers.duyle.gamers.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addNewHabitButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, addNewHabit.class));
            }
        });

        String[] skills={"reading"};
        ListView newFeedList = (ListView) findViewById(R.id.newFeedList);


        ListAdapter adapter= new CustomAdapter(this,skills);
        newFeedList.setAdapter(adapter);

        newFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String skill = String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(MainActivity.this,skill,Toast.LENGTH_LONG).show();
            }
        });
*/
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
}
