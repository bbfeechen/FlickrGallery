package kailianc.andrew.cmu.edu.flickrgallery.control;

import android.net.Uri;
import android.util.Log;

import kailianc.andrew.cmu.edu.flickrgallery.model.Credentials;

/**
 * Author  : KAILIANG CHEN<br>
 * Version : 1.0<br>
 * Date    : 12/13/15<p>
 *
 * UrlManager is a Singleton class that controls the url conversion for feeds, photo downloading.<br>
 * It maintains the information of Flickr link, API related methods and response file format<br>
 * (JSON/XML) and photo units(page) every time downloaded.<p>
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
     * function to create a url for downloading feeds<br>
     * @param query : search keyword in SearchView<br>
     * @param page : requested page number of all related photos<br>
     * @return : url for downloading all feeds(information for photos)<p>
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
     * function to create a url for viewing photo in Flickr app<br>
     * @param id : single photo id<br>
     * @return : url for viewing photo in Flickr flagship app<p>
     */
    public static String getFlickrUrl(String id) {
        return String.format(FLICKR_URL, id);
    }

    /**
     * function to create a url for obtaining photo description text<br>
     * @param id : single photo id<br>
     * @return : url for corresponding photo id<p>
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
