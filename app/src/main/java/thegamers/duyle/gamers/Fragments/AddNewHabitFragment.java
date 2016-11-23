package thegamers.duyle.gamers.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import thegamers.duyle.gamers.R;


/**
 * Created by Duy Le on 11/5/2016.
 */

public class AddNewHabitFragment extends Fragment {
    Button takePictureB;
    ImageView imageView;
    Spinner typeOfLengthSpinner;
    Spinner publicitySpinner;
    EditText habitEditText;
    EditText amountOfDaysEditText;
    EditText descriptionEditText;

    static final int CAM_REQUEST=1;
    Button doneButton;
    private static String day="day";
    private static int counter=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_habit_fragment,container,false);
        takePictureB=(Button) view.findViewById(R.id.takePicturebutton);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        doneButton = (Button) view.findViewById(R.id.doneButton);

        habitEditText=(EditText) view.findViewById(R.id.habitEditText);
        amountOfDaysEditText=(EditText)view.findViewById(R.id.amountOfDaysEditText);
        descriptionEditText=(EditText) view.findViewById(R.id.descriptionEditText);


        Picasso.with(view.getContext()).load(new File(Environment.getExternalStorageDirectory().toString()+"/camera_app/day1.jpg")).resize(50,50).into(imageView);

        //imageView.setImageDrawable(Drawable.createFromPath("/storage/external_SD/camera_app/day1.jpg"));
        //done button
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean passCondition=false;
                if(!(habitEditText.getText().toString().isEmpty())){
                    if(!amountOfDaysEditText.getText().toString().isEmpty()){
                        if(!descriptionEditText.getText().toString().isEmpty()){
                            //startActivity(new Intent(addNewHabit.this, MainActivity.class));
                        }
                        else{
                            Toast.makeText(getContext(),"Please fill in the description!",Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getContext(),"Please fill in the amount!",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getContext(),"Please fill in the habit name!",Toast.LENGTH_LONG).show();
                }





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
        typeOfLengthSpinner = (Spinner) view.findViewById(R.id.typeOfLengthSpinner);
        //spinner drop down elements
        List<String> typeOfLength = new ArrayList<String>();
        typeOfLength.add("Day(s)");
        typeOfLength.add("Month(s)");
        typeOfLength.add("Year(s)");
        //adapter for spinner
        ArrayAdapter typeOfLengthAdapter = new ArrayAdapter<String>(view.getContext(),R.layout.support_simple_spinner_dropdown_item,typeOfLength);
        typeOfLengthSpinner.setAdapter(typeOfLengthAdapter);

        //spinner for public/private
        publicitySpinner = (Spinner) view.findViewById(R.id.publicitySpinner);
        List<String> publicity = new ArrayList<String>();
        publicity.add("Public");
        publicity.add("Private");
        //adapter for publicity spinner
        ArrayAdapter publicityAdapter = new ArrayAdapter<String>(view.getContext(),R.layout.support_simple_spinner_dropdown_item,publicity);
        publicitySpinner.setAdapter(publicityAdapter);

        return view;
    }
    private File getFile(){
        File folder = new File(Environment.getExternalStorageDirectory().toString()+"/camera_app");

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
        //String path="/storage/external_SD/camera_app/"+day+counter+".jpg";
        //imageView.setImageDrawable(Drawable.createFromPath(path));
    }
}

