package shultz.mary.websearch;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mary on 4/15/2017.
 */

public class DownloadMaterial extends AsyncTask<String, Void, String> {
    @Override
    public String doInBackground(String... params) {
        URL url;
        HttpURLConnection urlConnection;
        String result = "";

        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String s;
            while ((s = reader.readLine()) != null) {
                result += s;
            }
        } catch (MalformedURLException e) {
            Log.e("DownloadMaterialError", e.toString());
        } catch (IOException e) {
            Log.e("DownloadMaterialError", e.toString());
        }
        return result;
    }

}
