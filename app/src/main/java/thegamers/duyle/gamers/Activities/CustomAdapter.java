package thegamers.duyle.gamers.Activities;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import thegamers.duyle.gamers.R;

/**
 * Created by Duy Le on 11/3/2016.
 */

public class CustomAdapter extends ArrayAdapter<String> {
    ImageView GIFView;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;


    ByteArrayOutputStream bytearrayoutputstream;
    byte[] BYTE;

    public CustomAdapter(Context context, ArrayList habits) {
        super(context, R.layout.new_feed_row, habits);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File saveFolder = new File(Environment.getExternalStorageDirectory().toString()+"/camera_app/save/");
        File[] listFolders = saveFolder.listFiles();

        File[] listFiles = new File (Environment.getExternalStorageDirectory().toString()+"/camera_app/save/"+listFolders[position].getName()).listFiles();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View newFeedView = inflater.inflate(R.layout.new_feed_row,parent,false);

        String skillName = getItem(position);
        TextView titleTextView = (TextView) newFeedView.findViewById(R.id.titleTextView);
        titleTextView.setText(skillName);

        imageView1 = (ImageView) newFeedView.findViewById(R.id.imageView1);
        imageView2 = (ImageView) newFeedView.findViewById(R.id.imageView2);
        imageView3 = (ImageView) newFeedView.findViewById(R.id.imageView3);
        imageView4 = (ImageView) newFeedView.findViewById(R.id.imageView4);
        imageView5 = (ImageView) newFeedView.findViewById(R.id.imageView5);
        GIFView = (ImageView) newFeedView.findViewById(R.id.GIFView);

        ImageView[] imageList = {imageView1,imageView2,imageView3,imageView4,imageView5};
        //Toast.makeText(getContext(),""+Environment.getExternalStorageDirectory().toString()+"/camera_app/save/"+listFolders[position].getName()+"/"+listFiles[0].getName(), Toast.LENGTH_LONG).show();
        if(listFolders[position].exists()&&listFiles[0].exists()){
            Picasso.with(parent.getContext()).load(new File(Environment.getExternalStorageDirectory().toString()+"/camera_app/save/"+listFolders[position].getName()+"/"+listFiles[0].getName())).resize(50,50).into(imageView1);
        }

        if(position>0) {
            for(int i=1;i<5;i++){
                Picasso.with(parent.getContext()).load(new File(Environment.getExternalStorageDirectory().toString()+"/camera_app/save/"+listFolders[position].getName()+"/"+listFiles[i].getName())).resize(50,50).into(imageList[i]);
            }
        }







        //Picasso.with(parent.getContext()).load(R.drawable.unnamed).resize(25,25).into(imageView1);


        return newFeedView;
    }


}
