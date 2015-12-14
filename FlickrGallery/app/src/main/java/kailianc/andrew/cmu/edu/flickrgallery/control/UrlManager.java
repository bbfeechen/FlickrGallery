package kailianc.andrew.cmu.edu.flickrgallery.control;

import android.net.Uri;
import android.util.Log;

import kailianc.andrew.cmu.edu.flickrgallery.model.Credentials;

/**
 * Author  : KAILIANG CHEN
 * Version : 1.0
 * Date    : 12/13/15
 *
 * UrlManager is a Singleton class that controls the url conversion for feeds, photo downloading.
 * It maintains the information of Flickr link, API related methods and response file format
 * (JSON/XML) and photo units(page) every time downloaded.
 *
 */
public class UrlManager {
    // tag for logcat
    public static final String TAG = UrlManager.class.getSimpleName();

    // key of SharedPreference for maintaining query words
    public static final String PREF_SEARCH_QUERY ="searchQuery";

    // url for Flickr link and api
    private static final String FLICKR_URL = "http://flickr.com/photo.gne?id=%s";
    private static final String ENDPOINT = "https://api.flickr.com/services/rest/";
    private static final String METHOD_GETRECENT = "flickr.photos.getRecent";
    private static final String METHOD_SEARCH = "flickr.photos.search";
    private static final String METHOD_GETINFO = "flickr.photos.getInfo";

    // singleton in thread-safe style
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

    /**
     * function to create a url for downloading feeds
     * @param query : search keyword in SearchView
     * @param page : requested page number of all related photos
     * @return : url for downloading all feeds(information for photos)
     */
    public static String getFeedsUrl(String query, int page) {
        String url;
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

    /**
     * function to create a url for viewing photo in Flickr app
     * @param id : single photo id
     * @return : url for viewing photo in Flickr flagship app
     */
    public static String getFlickrUrl(String id) {
        return String.format(FLICKR_URL, id);
    }

    /**
     * function to create a url for obtaining photo description text
     * @param id : single photo id
     * @return : url for corresponding photo id
     */
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
