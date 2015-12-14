package kailianc.andrew.cmu.edu.flickrgallery.control;

import android.net.Uri;
import android.util.Log;

import kailianc.andrew.cmu.edu.flickrgallery.model.Credentials;

/**
 * Author  : KAILIANG CHEN
 * Version : 1.0
 * Date    : 12/13/15
 */
public class UrlManager {
    public static final String TAG = UrlManager.class.getSimpleName();

    private static final String FLICKR_URL = "http://flickr.com/photo.gne?id=%s";
    private static final String ENDPOINT =
            "https://api.flickr.com/services/rest/";
    private static final String METHOD_GETRECENT = "flickr.photos.getRecent";
    private static final String METHOD_SEARCH = "flickr.photos.search";
    private static final String METHOD_GETINFO = "flickr.photos.getInfo";
    public static final String PREF_SEARCH_QUERY ="searchQuery";

    private static volatile UrlManager instance = null;
    private UrlManager() {
    }

    public static UrlManager getInstance() {
        if(instance == null) {
            synchronized (UrlManager.class) {
                if(instance == null) {
                    instance = new UrlManager();
                }
            }
        }
        return instance;
    }

    public static String getFeedsUrl(String query, int page) {
        String url = null;
        if(query != null) {
            url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_SEARCH)
                    .appendQueryParameter("api_key", Credentials.API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("text", query)
                    .appendQueryParameter("page", String.valueOf(page))
                    .build().toString();
        } else {
            url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter("method", METHOD_GETRECENT)
                    .appendQueryParameter("api_key", Credentials.API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("page", String.valueOf(page))
                    .build().toString();
        }
        Log.d(TAG, url);
        return url;
    }

    public static String getFlickrUrl(String id) {
        return String.format(FLICKR_URL, id);
    }

    public static String getPhotoInfoUrl(String id) {
        return Uri.parse(ENDPOINT).buildUpon()
                .appendQueryParameter("method", METHOD_GETINFO)
                .appendQueryParameter("api_key", Credentials.API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("photo_id", id)
                .build().toString();
    }
}
