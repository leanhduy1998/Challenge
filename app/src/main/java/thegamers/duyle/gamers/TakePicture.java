package thegamers.duyle.gamers;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;


/**
 * Created by Duy Le on 11/5/2016.
 */

public class TakePicture extends AppCompatActivity {
    Button takePictureB;
    ImageView imageView;
    static final int CAM_REQUEST=1;
    Button doneButton;
    private static String day="day";
    private static int counter=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_picture_fragment);
        takePictureB=(Button) findViewById(R.id.takePicturebutton);

        doneButton = (Button) findViewById(R.id.doneButton);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TakePicture.this, MainActivity.class));
            }
        });

        takePictureB.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent,CAM_REQUEST);
            }
        });
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
