package habit.duyle.habit.services;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import habit.duyle.habit.Activities.MainActivity;
import habit.duyle.habit.Fragments.AddNewHabitFragment;
import habit.duyle.habit.Fragments.NewFeedFragment;
import habit.duyle.habit.models.DayDesciption;
import habit.duyle.habit.models.Feed;
import habit.duyle.habit.models.FeedSinglePicture;

/**
 * Created by leanh on 3/19/2017.
 */

public class PictureUploader {
    private static String thumbnail = "THUMBNAIL";

    public static void addNewPictureToDatabaseAndReturnUri(final String filePath, final String uid, final String habitName) throws ExecutionException, InterruptedException {
        final Uri file = Uri.fromFile(new File(filePath));

        final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference picRef = storageRef.child("data/" + uid + "/" + habitName + "/" + file.getLastPathSegment());

        final UploadTask uploadTask = picRef.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("TAG", "Failed on upload, " + exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ThumbnailCompressor.compressPicture(new File(filePath));
                final StorageReference thumbnailRef = storageRef.child("data/" + uid + "/" + habitName + "/" + thumbnail + "/" + file.getLastPathSegment());
                final UploadTask thumbnailTask = thumbnailRef.putFile(file);
                thumbnailTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("TAG", "Failed to upload " + e.toString());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        NewFeedFragment.getNewFeedFragment().refreshData();
                        MainActivity.getMainActivity().deleteTrashFolder();
                        MainActivity.getMainActivity().hideProgressBar();
                    }
                });
            }
        });
    }
}
