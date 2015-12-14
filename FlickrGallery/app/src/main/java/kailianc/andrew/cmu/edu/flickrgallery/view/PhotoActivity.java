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
            fragment = new PhotoFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        } else {
            if (savedInstanceState != null) {
                ((PhotoFragment)fragment).setState(savedInstanceState.getParcelable("state"));
                Log.d(TAG, "save");
            }
        }
    }

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
