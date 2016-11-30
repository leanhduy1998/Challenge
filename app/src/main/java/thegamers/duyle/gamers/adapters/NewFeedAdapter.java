package thegamers.duyle.gamers.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import thegamers.duyle.gamers.R;
import thegamers.duyle.gamers.holders.FeedViewHolder;
import thegamers.duyle.gamers.models.Feed;

/**
 * Created by Duy Le on 11/3/2016.
 */

public class NewFeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {
    public NewFeedAdapter(ArrayList<Feed> habits) {
        this.habits = habits;
    }

    private ArrayList<Feed> habits;
    private ImageView GIFView;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;

/*
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
*/

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View feedCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_card, parent, false);
        return new FeedViewHolder(feedCard);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        final Feed feed = habits.get(position);
        holder.updateUI(feed);
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }
}
