package shultz.mary.websearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private final String WEBSITE_LINK = "http://mobile.pcauthority.com.au/News/170181,top-10-computer-games-of-all-time.aspx";
    private final int NUM_CHOICES = 4;
    private int numRounds = 0;
    private String[] gameNames;
    private DownloadMaterial downloadMaterial;
    private Random rand;
    private WebPageParser parser;
    private String currentAnswer;
    private TextView[] views;
    private ImageView gameImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameImage = (ImageView) findViewById(R.id.gameImage);
        initializeApplication();
    }

    private void initializeApplication() {
        downloadMaterial = new DownloadMaterial();
        parser = new WebPageParser(getWebSource());
        populateTextViewList();
        rand = new Random();
        gameNames = new String[parser.getGameNames().size()];
        gameNames = parser.getGameNames().toArray(gameNames);
        setGame();
    }

    private void setGame() {
        int[] choices = getRandomInformation();
        setAnswer(gameNames[rand.nextInt(choices.length)]);
        gameImage.setImageBitmap(parser.getGameInformation().get(currentAnswer));
        for (int i = 0; i < choices.length; i++) {
            views[i].setText(gameNames[choices[i]]);
        }
    }

    private void setAnswer(String answer) {
        this.currentAnswer = answer;
    }

    private int[] getRandomInformation() {
        int[] choices = {-1,-1,-1,-1};
        for (int i = 0; i < choices.length; ++i) {
            int currentNum = -1;
            do {
                currentNum = rand.nextInt(parser.getGameNames().size());

            } while (containsNum(choices, currentNum));
            choices[i] = currentNum;
        }
        return choices;
    }

    private boolean containsNum(int[] choices, int currentChoice) {
        boolean contains = false;
        for (int i: choices) {
            if (i == currentChoice)
                contains = true;
        }
        return contains;
    }

    private String getWebSource() {
        String content = "";
        try {
            content = downloadMaterial.execute(WEBSITE_LINK).get();
        } catch (NullPointerException e) {
            Log.e("OnCreateError", e.toString());
        } catch (InterruptedException e) {
            Log.e("OnCreateError", e.toString());
        } catch (ExecutionException e) {
            Log.e("OnCreateError", e.toString());
        }
        return content;
    }

    private void populateTextViewList() {
        views = new TextView[NUM_CHOICES];
        views[0] = (TextView) findViewById(R.id.gameZero);
        views[1] = (TextView) findViewById(R.id.gameOne);
        views[2] = (TextView) findViewById(R.id.gameTwo);
        views[3] = (TextView) findViewById(R.id.gameThree);
    }

    public void onClick(View view) {

    }
}



