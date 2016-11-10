package thegamers.duyle.gamers;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Duy Le on 11/5/2016.
 */

public class addNewHabit extends AppCompatActivity {
    Button takePictureB;
    ImageView imageView;
    Spinner typeOfLengthSpinner;
    static final int CAM_REQUEST=1;
    Button doneButton;
    private static String day="day";
    private static int counter=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //done button
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_habit);
        takePictureB=(Button) findViewById(R.id.takePicturebutton);

        doneButton = (Button) findViewById(R.id.doneButton);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(addNewHabit.this, MainActivity.class));
            }
        });
        //taking picture
        takePictureB.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent,CAM_REQUEST);
            }
        });
        // spinner for day/month/year
        typeOfLengthSpinner = (Spinner) findViewById(R.id.typeOfLengthSpinner);
            //spinner drop down elements
        List<String> typeOfLength = new ArrayList<String>();
        typeOfLength.add("Day(s)");
        typeOfLength.add("Month(s)");
        typeOfLength.add("Year(s)");
            //adapter for spinner
        ArrayAdapter dataAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,typeOfLength);
        typeOfLengthSpinner.setAdapter(dataAdapter);

    }
    private File getFile(){
        File folder = new File("/storage/external_SD/camera_app");

        if(!folder.exists()){
            folder.mkdir();
        }



        File image_file = new File(folder,day+counter+".jpg");
        if(image_file.exists()){
            counter++;
            image_file = new File(folder,day+counter+".jpg");
        }
        counter++;
        return image_file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path="/storage/external_SD/camera_app/"+day+counter+".jpg";
        imageView.setImageDrawable(Drawable.createFromPath(path));
    }
}
