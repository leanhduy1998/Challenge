package thegamers.duyle.gamers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

/**
 * Created by Duy Le on 11/3/2016.
 */

public class CustomAdapter extends ArrayAdapter<String> {
    ImageView GIFView;

    ByteArrayOutputStream bytearrayoutputstream;
    byte[] BYTE;

    public CustomAdapter(Context context, String[] skills) {
        super(context,R.layout.new_feed_row, skills);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View newFeedView = inflater.inflate(R.layout.new_feed_row,parent,false);

        bytearrayoutputstream = new ByteArrayOutputStream();

        String skillName = getItem(position);
        TextView titleTextView = (TextView) newFeedView.findViewById(R.id.titleTextView);
        titleTextView.setText(skillName);



        ViewGroup.LayoutParams layout = newFeedView.getLayoutParams();
        layout.height = newFeedView.getWidth();
        newFeedView.setLayoutParams(layout);









        ImageView imageView1 = (ImageView) newFeedView.findViewById(R.id.imageView1);
        ImageView imageView2 = (ImageView) newFeedView.findViewById(R.id.imageView2);
        ImageView imageView3 = (ImageView) newFeedView.findViewById(R.id.imageView3);
        ImageView imageView4 = (ImageView) newFeedView.findViewById(R.id.imageView4);
       ImageView imageView5 = (ImageView) newFeedView.findViewById(R.id.imageView5);
        GIFView = (ImageView) newFeedView.findViewById(R.id.GIFView);
        GIFView.setImageResource(R.drawable.c1);


/*
        drawable1 = newFeedView.getResources().getDrawable(R.drawable.c1);
        //drawable2 = newFeedView.getResources().getDrawable(R.drawable.c2);
        //drawable3 = newFeedView.getResources().getDrawable(R.drawable.c3);
        //drawable4 = newFeedView.getResources().getDrawable(R.drawable.c4);
        //drawable5 = newFeedView.getResources().getDrawable(R.drawable.c5);

        bitmap1 = ((BitmapDrawable)drawable1).getBitmap();
         BYTE = bytearrayoutputstream.toByteArray();
        bitmap1= BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);
        imageView1.setImageBitmap(bitmap1);
*/


        imageView1.setImageResource(R.drawable.c1);
        imageView2.setImageResource(R.drawable.c2);
        imageView3.setImageResource(R.drawable.c3);
        imageView4.setImageResource(R.drawable.c4);
        imageView5.setImageResource(R.drawable.c5);



        return newFeedView;
    }
}
