package kailianc.andrew.cmu.edu.flickrgallery.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import kailianc.andrew.cmu.edu.flickrgallery.R;

/**
 * Author  : KAILIANG CHEN<br>
 * Version : 1.0<br>
 * Date    : 12/13/15<p>
 *
 * Main Screen for single photo browsing, downloading and linking to official Flickr app.<p>
 *
 * Similar to FeedFragment, PhotoFragment is used in this activity for two purposes:<br>
 * 1) handle configuration changes (plus setRetainInstance(true) in fragment)<br>
 * 2) handle multiple size screens<p>
 *
 */
public class PhotoActivity extends AppCompatActivity {

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
     * function for state saving during configuration change<br>
     * when the activity transits into a background state,<br>
     * it will be called to allow you to save away any dynamic<br>
     * instance state in your activity into the given Bundle.<p>
     *
     * @param outState : bundle for containing state information<p>
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
