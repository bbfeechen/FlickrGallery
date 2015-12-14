package kailianc.andrew.cmu.edu.flickrgallery.model;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Author  : KAILIANG CHEN<br>
 * Version : 1.0<br>
 * Date    : 12/13/15<p>
 *
 * Customized Content Provider class for search suggestion function.
 *
 */
public class SuggestionProvider extends SearchRecentSuggestionsProvider {

    // part of content uri which is defined in AndroidManifest.xml
    public static final String AUTHORITY = "kailianc.andrew.cmu.edu.flickrgallery.model" +
            ".SuggestionProvider";

    // suggestion mode which gives recent queries
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
