package com.example.dailyquotes;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    private LinearLayout belowLayout;
    private TextView quote;
    private ImageView image;
    private CardView aboveLayout,btn;
    private int height;
    private boolean isFlipped=false;
    private OvershootInterpolator interpolator=new OvershootInterpolator();
    private final int duration=300;
    private final String quoteUrl = "http://192.168.28.8:8080/quote";
    int[] images = {
            R.drawable.motiv,
            R.drawable.tate_a,
            R.drawable.andrew_tate_hr,
            R.drawable.arnold_schwarzenegger,
            R.drawable.conor_mcgregor,
            R.drawable.arnold_schwarzenegger_2,
            R.drawable.conor_mcgregor_pose,
            R.drawable.jocko_willink,
            R.drawable.khabib_nurmagomedov_2,
            R.drawable.mike_tyson,
            R.drawable.mohamed_ali,
            R.drawable.jon_jones,
            R.drawable.david,
            R.drawable.tristan
    };
    String[] motivationalQuotes = {
            "Life is like a fight. You need to be tough and determined to win.",
            "Success comes to those who refuse to give up, no matter how many times they fall.",
            "Don't wait for opportunities, create them.",
            "The only limits that exist are the ones you place on yourself.",
            "Every setback is a setup for a comeback.",
            "You're not a failure until you quit trying.",
            "Your destiny is determined by your decisions.",
            "Success is not an accident; it's a result of relentless effort and dedication.",
            "Champions are made when no one is watching.",
            "The harder you work, the luckier you get.",
            "Your dreams are valid, but they won't become a reality without hard work.",
            "Failure is not fatal unless you let it defeat you.",
            "Success is the sum of small efforts repeated day in and day out.",
            "The difference between winners and losers is that winners keep moving forward.",
            "Excuses will always be there for you, but opportunities won't.",
            "Don't fear failure; fear being in the same place next year as you are today.",
            "Believe in yourself even when no one else does.",
            "Success is not about luck; it's about making the right choices and taking action.",
            "If you want to be great, you have to be willing to be misunderstood.",
            "The only way to predict the future is to create it."
    };

    private HttpRequestTask requestQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialize
        belowLayout = findViewById(R.id.belowLayout);
        quote = findViewById(R.id.quote);
        aboveLayout = findViewById(R.id.aboveLayout);
        btn = findViewById(R.id.btn);
        image = findViewById(R.id.motiv_image);

        aboveLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //fetch height
                height=(aboveLayout.getHeight())/2;
            }
        });

        //button click listener
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                quote.setText(motivationalQuotes[getRandomNumber()]);
                HttpRequestTask httpRequestTask = new HttpRequestTask();
                Future<String> future = httpRequestTask.makeHttpRequest(quoteUrl);

                try {
                    String result = future.get(); // This will block until the result is available
                    if (result != null) {
                        quote.setText(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    httpRequestTask.shutdown();
                }


                btn.animate().setInterpolator(interpolator).translationY(50).start();

                aboveLayout.animate().setDuration(duration).setInterpolator(interpolator).translationY(height).start();
                belowLayout.animate().setDuration(duration).setInterpolator(interpolator).translationY(-1*height)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {

                                if(!isFlipped) {
                                    aboveLayout.setTranslationZ(-50);
                                    belowLayout.setTranslationZ(0);
                                    btn.animate().setInterpolator(interpolator).rotation(45).start();
                                    // Change image
                                    image.setImageResource(images[getRandomNumber()]);
                                }else {
                                    aboveLayout.setTranslationZ(0);
                                    belowLayout.setTranslationZ(-50);
                                    btn.animate().setInterpolator(interpolator).rotation(0).start();
                                }

                                aboveLayout.animate().setDuration(duration).setInterpolator(interpolator).translationY(0).start();
                                belowLayout.animate().setDuration(duration).setInterpolator(interpolator).translationY(0).start();
                                btn.animate().setInterpolator(interpolator).translationY(0).start();


                                isFlipped=!isFlipped;
                            }
                        })
                        .start();
            }
        });
    }

    // Function to get a random quote
    private int getRandomNumber() {
        // Create a Random object
        Random random = new Random();

        // Return the quote at the random index
        return random.nextInt(images.length);
    }
}