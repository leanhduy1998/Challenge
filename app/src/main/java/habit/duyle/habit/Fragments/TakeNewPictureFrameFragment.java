package habit.duyle.habit.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;

import habit.duyle.habit.Activities.MainActivity;
import habit.duyle.habit.R;
import habit.duyle.habit.services.FeedDataService;

/**
 * Created by Duy Le on 12/15/2016.
 */

public class TakeNewPictureFrameFragment extends Fragment {
    private Button takePictureB;
   // private ImageView imageView;

    private Button retakePictureButton;
    private Button addMorePictureButton;

    private static final int CAM_REQUEST=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.take_new_picture_frame_fragment,container,false);
        setIDs(view);
        setListener();
        setVisibility();
        return view;
    }
    public void setIDs(View view){
        takePictureB=(Button) view.findViewById(R.id.takePictureButton);
        takePictureB.getBackground().setAlpha(64);
     //  imageView = (ImageView) view.findViewById(R.id.imageView);
        addMorePictureButton = (Button) view.findViewById(R.id.addMorePictureButton);
    }
    public void setVisibility(){
        addMorePictureButton.setVisibility(View.GONE);
    }
    public void setListener(){
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

        addMorePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent,CAM_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==0){

        }
        else{
            addMorePictureButton.setVisibility(View.VISIBLE);
            takePictureB.setText("Retake Picture");
            File[] picShow = new File(MainActivity.getMainActivity().getTrashFolder().getAbsolutePath()).listFiles();
            File file = new File(MainActivity.getMainActivity().getTrashFolder().getAbsolutePath()+File.separator+picShow[0].getName());
         //   Picasso.with(getContext()).load(file).resize(400,200).into(imageView);
        }
    }
    private File getFile(){
        File trashFolder = new File(MainActivity.getMainActivity().getTrashFolder().getAbsolutePath()) ;
        if(!trashFolder.exists()){
            trashFolder.mkdir();
        }
        File image_file = new File(trashFolder, FeedDataService.getInstance().getTimeInMillisNow()+".jpg");
        return image_file;
    }
}
