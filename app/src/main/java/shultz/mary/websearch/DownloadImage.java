package shultz.mary.websearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mary on 4/14/2017.
 */

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    @Override
    public Bitmap doInBackground(String... params) {
        URL url;
        HttpURLConnection urlConnection;
        Bitmap bitmap = null;

        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();

            bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (Exception e) {
            Log.e("DownloadImageError", e.toString());
        }
        return bitmap;
    }
}