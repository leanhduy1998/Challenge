package thegamers.duyle.gamers.Activities;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

    ByteArrayOutputStream bytearrayoutputstream;
    byte[] BYTE;

    public CustomAdapter(Context context, ArrayList habits) {
        super(context, R.layout.new_feed_row, habits);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View newFeedView = inflater.inflate(R.layout.new_feed_row,parent,false);


        String skillName = getItem(position);
        TextView titleTextView = (TextView) newFeedView.findViewById(R.id.titleTextView);
        titleTextView.setText(skillName);




        ImageView imageView1 = (ImageView) newFeedView.findViewById(R.id.imageView1);
        ImageView imageView2 = (ImageView) newFeedView.findViewById(R.id.imageView2);
        ImageView imageView3 = (ImageView) newFeedView.findViewById(R.id.imageView3);
        ImageView imageView4 = (ImageView) newFeedView.findViewById(R.id.imageView4);
       ImageView imageView5 = (ImageView) newFeedView.findViewById(R.id.imageView5);
        GIFView = (ImageView) newFeedView.findViewById(R.id.GIFView);
       // GIFView.setImageResource(R.drawable.c1);





        Picasso.with(parent.getContext()).load(new File(Environment.getExternalStorageDirectory().toString()+"/camera_app/day1.jpg")).resize(50,50).into(imageView1);

        //Picasso.with(parent.getContext()).load(R.drawable.unnamed).resize(25,25).into(imageView1);


        return newFeedView;
    }
}
