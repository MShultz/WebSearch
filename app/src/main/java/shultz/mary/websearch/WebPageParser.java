package shultz.mary.websearch;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mary on 4/13/2017.
 */

public class WebPageParser {
    private final String content;
    private HashMap<String, Bitmap> gameInformation = new HashMap<String, Bitmap>();
    private final String informationPattern = "\\d{1,2}+\\.\\s(?<GameName>[^<]*).+?href=\"(?<ImageLink>.+?)\"";
    private final Pattern p = Pattern.compile(informationPattern);
    private final int GAME_NAME = 1;
    private final int IMAGE_LINK = 2;

    public WebPageParser(String content){
        this.content = content;
        populateMap();
    }

    private void addGame(String game, String imageLocation){
        try {
            gameInformation.put(game, downloadImage(imageLocation));
        } catch (ExecutionException e) {
            Log.e("DownloadImageError", e.toString());
        } catch (InterruptedException e) {
            Log.e("DownloadImageError", e.toString());
        }
    }

    private void populateMap(){
        Matcher m = p.matcher(content);
        while(m.find()) {
           addGame(m.group(GAME_NAME),m.group(IMAGE_LINK));
        }
    }

    public HashMap<String, Bitmap> getGameInformation(){
        return gameInformation;
    }

    public Set<String> getGameNames(){
        return gameInformation.keySet();
    }

    private Bitmap downloadImage(String imageLocation) throws ExecutionException, InterruptedException {
        return new DownloadImage().execute(imageLocation).get();
    }

}
