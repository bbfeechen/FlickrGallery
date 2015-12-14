package kailianc.andrew.cmu.edu.flickrgallery.model;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Author  : KAILIANG CHEN
 * Version : 1.0
 * Date    : 12/13/15
 */
public class SuggestionProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "kailianc.andrew.cmu.edu.flickrgallery.model" +
            ".SuggestionProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
