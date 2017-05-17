package habit.duyle.habit.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by leanh on 3/19/2017.
 */

public class ThumbnailCompressor {

    public static File compressPicture(final File picFile){
        try {
            return new AsyncTask<Void, Void, File>() {
                @Override
                protected File doInBackground(Void... params) {
                    try {
                        Bitmap rotatedThumbnailFormat = createRotatedScaledBitmapFormat(picFile);
                        FileOutputStream fos = new FileOutputStream(picFile);
                        rotatedThumbnailFormat.compress(Bitmap.CompressFormat.JPEG, 50
                                , fos);
                        fos.close();

                    } catch (FileNotFoundException e) {
                        Log.d("TAG", "File not found: " + e.getMessage());
                    } catch (IOException e) {
                        Log.d("TAG", "Error accessing file: " + e.getMessage());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(File aVoid) {
                    super.onPostExecute(aVoid);
                }
            }.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return picFile;
    }
    private static Bitmap createRotatedScaledBitmapFormat(final File localpictureNameTargetFile) throws ExecutionException, InterruptedException {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap bitmap = BitmapFactory.decodeFile(localpictureNameTargetFile.toString());
        if(bitmap.getWidth()>bitmap.getHeight()){
            Bitmap preThumbnailFormat= ThumbnailUtils.extractThumbnail(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2);
            Bitmap rotatedThumbnailFormat =Bitmap.createBitmap(preThumbnailFormat , 0, 0, preThumbnailFormat .getWidth(), preThumbnailFormat .getHeight(), matrix, true);
            return  rotatedThumbnailFormat;
        }
        else{
            return bitmap;
        }
    }
}
