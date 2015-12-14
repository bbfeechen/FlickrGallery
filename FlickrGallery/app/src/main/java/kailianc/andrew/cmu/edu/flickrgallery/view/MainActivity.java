package kailianc.andrew.cmu.edu.flickrgallery.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import kailianc.andrew.cmu.edu.flickrgallery.R;


/**
 * Author  : KAILIANG CHEN<br>
 * Version : 1.0<br>
 * Date    : 12/13/15<p>
 *
 * Main Entry Activity uses a fade in/fade out animation with a background picture.<p>
 *
 * MainAcitivity -> FeedActivity(FeedFragment) -> PhotoAcitivty(PhotoFragment)<p>
 *
 */
public class MainActivity extends Activity {

    // tag for logcat
    public static final String TAG = PhotoFragment.class.getSimpleName();

    // time span for entry animation
    private final static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
