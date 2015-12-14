package kailianc.andrew.cmu.edu.flickrgallery.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import kailianc.andrew.cmu.edu.flickrgallery.R;

/**
 * Author  : KAILIANG CHEN
 * Version : 1.0
 * Date    : 12/13/15
 *
 * Main Screen for single photo browsing, downloading and linking to official Flickr app.
 *
 * Similar to FeedFragment, PhotoFragment is used in this activity for two purposes:
 * 1) handle configuration changes (plus setRetainInstance(true) in fragment)
 * 2) handle multiple size screens
 *
 */
public class PhotoActivity extends AppCompatActivity {

    // tag for logcat
    public static final String TAG = PhotoFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            // fragment initialization
            fragment = new PhotoFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        } else {
            // fragment state restore during configuration change
            if (savedInstanceState != null) {
                ((PhotoFragment)fragment).setState(savedInstanceState.getParcelable("state"));
                Log.d(TAG, "save");
            }
        }
    }

    /**
     * function for state saving during configuration change
     * when the activity transits into a background state,
     * it will be called to allow you to save away any dynamic
     * instance state in your activity into the given Bundle.
     *
     * @param outState : bundle for containing state information
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if(fragment != null) {
            outState.putParcelable("state", ((PhotoFragment)fragment).getState());
            Log.d(TAG, "restore");
        }
    }
}
