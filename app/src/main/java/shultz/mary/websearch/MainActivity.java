package shultz.mary.websearch;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private final DecimalFormat format = new DecimalFormat("00");
    private final int TOTAL_ROUNDS = 10;
    private final String TIME_PROMPT = "Your total time was: ";
    private final String ROUND_PROMPT = "Rounds Played: ";
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
    private GridLayout gameScreen;
    private LinearLayout gameOverScreen;
    private Chronometer stopWatch;
    private long countUp;
    private TextView timer;
    private TableLayout loadingScreen;
    private TextView timeView;
    private TextView roundCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameImage = (ImageView) findViewById(R.id.gameImage);
        timer = (TextView) findViewById(R.id.timer);
        loadingScreen = (TableLayout) findViewById(R.id.loadingScreen);
        gameScreen = (GridLayout) findViewById(R.id.gameScreen);
        gameOverScreen = (LinearLayout) findViewById(R.id.endGameScreen);
        timeView = (TextView) findViewById(R.id.timeView);
        roundCount = (TextView) findViewById(R.id.roundCount);
        populateTextViewList();
    }

    @Override
    public void onStart() {
        super.onStart();

        Handler delayApplicationInitialization = new Handler();
        delayApplicationInitialization.postDelayed(new Runnable() {
            public void run() {
                initializeApplication();
            }
        }, 1000);
    }


    public void initializeApplication() {
        downloadMaterial = new DownloadMaterial();
        parser = new WebPageParser(getWebSource());
        rand = new Random();
        gameNames = new String[parser.getGameNames().size()];
        gameNames = parser.getGameNames().toArray(gameNames);
        setGame();
        findViewById(R.id.startButton).setVisibility(View.VISIBLE);
    }

    private void setGame() {
        int[] choices = getRandomInformation();
        setAnswer(gameNames[choices[rand.nextInt(choices.length)]]);
        gameImage.setImageBitmap(parser.getGameInformation().get(currentAnswer));
        for (int i = 0; i < choices.length; i++) {
            views[i].setText(gameNames[choices[i]]);
        }
    }

    private void setAnswer(String answer) {
        this.currentAnswer = answer;
    }

    private int[] getRandomInformation() {
        int[] choices = {-1, -1, -1, -1};
        for (int i = 0; i < choices.length; ++i) {
            int currentNum;
            do {
                currentNum = rand.nextInt(parser.getGameNames().size());

            } while (containsNum(choices, currentNum));
            choices[i] = currentNum;
        }
        return choices;
    }

    private boolean containsNum(int[] choices, int currentChoice) {
        boolean contains = false;
        for (int i : choices) {
            if (i == currentChoice)
                contains = true;
        }
        return contains;
    }

    private String getWebSource() {
        String content = "";
        try {
            content = downloadMaterial.execute(WEBSITE_LINK).get();
        } catch (NullPointerException | InterruptedException | ExecutionException e) {
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

    public void startGame(View view) {
        loadingScreen.setVisibility(View.GONE);
        gameScreen.setVisibility(View.VISIBLE);
        startTimer();
    }

    private void startTimer() {
        stopWatch = (Chronometer) findViewById(R.id.chrono);
        stopWatch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer arg0) {
                countUp = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;
                String asText = format(countUp / 60) + ":" + format(countUp % 60);
                timer.setText(asText);
            }
        });
        stopWatch.setBase(SystemClock.elapsedRealtime());
        stopWatch.start();
    }

    private String format(long seconds) {
        return format.format(seconds);
    }

    public void handleChoice(View view) {
        Button userAnswer = (Button) view;
        if (!currentAnswer.equalsIgnoreCase(userAnswer.getText().toString())) {
            Toast.makeText(this, "This answer was incorrect! Try again.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "This answer was correct!", Toast.LENGTH_SHORT).show();
            ++numRounds;
            roundCount.setText(ROUND_PROMPT + Integer.toString(numRounds));
            if (numRounds == TOTAL_ROUNDS){
                endGame();
            }else{
                setGame();
            }
        }
    }

    private void endGame(){
        stopWatch.stop();
        gameScreen.setVisibility(View.INVISIBLE);
        timeView.setText(TIME_PROMPT + timer.getText().toString());
        gameOverScreen.setVisibility(View.VISIBLE);
    }

    public void playAgain(View view){
        gameOverScreen.setVisibility(View.INVISIBLE);
        numRounds = 0;
        setGame();
        gameScreen.setVisibility(View.VISIBLE);
        startTimer();
    }
}




