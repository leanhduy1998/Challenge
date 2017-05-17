package habit.duyle.habit.services;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import habit.duyle.habit.models.Feed;

/**
 * Created by Duy Le on 1/24/2017.
 */

public class Database {
    public static void uploadDataToDatabase(Feed feed, String uid, String publicity){
        DatabaseReference feedRef = FirebaseDatabase.getInstance().getReference("feed");
        DatabaseReference feedStatusRef = FirebaseDatabase.getInstance().getReference("feedStatus");
        feedRef.child(uid).child(feed.getCreatedTime()+"").setValue(feed);
        feedStatusRef.child(uid).child(feed.getCreatedTime()+"").setValue(publicity);

    }
    public static void updateUidMetadataForPic(String uid, StorageReference picRef,String publicity){
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .setCustomMetadata("publicity",publicity)
                .setCustomMetadata("uid",uid)
                .build();
        picRef.updateMetadata(metadata).addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                Log.i("TAG","Success on upload metadata");
            }
        });
    }


}
