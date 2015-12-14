package kailianc.andrew.cmu.edu.flickrgallery.view;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import kailianc.andrew.cmu.edu.flickrgallery.R;
import kailianc.andrew.cmu.edu.flickrgallery.control.UrlManager;
import kailianc.andrew.cmu.edu.flickrgallery.model.SuggestionProvider;

/**
 * Author  : KAILIANG CHEN
 * Version : 1.0
 * Date    : 12/13/15
 *
 * Main Screen for photo gallery pulled from Flickr website based on query keywords or
 * most recent photos(default).
 *
 * FeedFragment is used in this activity for two purposes:
 * 1) handle configuration changes (plus setRetainInstance(true) in fragment)
 * 2) handle multiple size screens
 *
 * Intent handling for search event injected from SearchView, then suggestion for search
 * will show and query keywords will be saved in SharedPreference for other classes.
 *
 */
public class FeedActivity extends AppCompatActivity {

    // tag for logcat
    public static final String TAG = FeedActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            // fragment initialization
            fragment = new FeedFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        } else {
            // fragment state restore during configuration change
            if (savedInstanceState != null) {
                ((FeedFragment)fragment).setState(savedInstanceState.getParcelable("state"));
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
            outState.putParcelable("state", ((FeedFragment)fragment).getState());
            Log.d(TAG, "restore");
        }
    }

    /**
     * function called for activities that set launchMode to "singleTop"
     * When startActivity is called, the activity will be re-launched
     * while at the top of the activity stack instead of a new instance
     * of the activity being started.
     *
     * onNewIntent() will be called on the existing instance with the
     * Intent that was used to re-launch it.
     *
     * @param intent : triggering intent
     */
    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    /**
     * internal function to handle search event
     * @param intent : triggering intent
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "Received a new search query: " + query);

            // show suggestion for search
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            // store search keyword
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(UrlManager.PREF_SEARCH_QUERY, query)
                    .commit();
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
            if(fragment != null) {
                // download and update photos according to search keyword
                ((FeedFragment)fragment).refresh();
            }
        }
    }
}
