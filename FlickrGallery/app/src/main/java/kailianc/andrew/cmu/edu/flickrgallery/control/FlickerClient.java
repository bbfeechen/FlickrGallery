package kailianc.andrew.cmu.edu.flickrgallery.control;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.util.HashSet;
import java.util.Set;
import kailianc.andrew.cmu.edu.flickrgallery.model.GetPhotoInfoResponse;
import kailianc.andrew.cmu.edu.flickrgallery.model.GetPhotosResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlickerClient {

    public static final String API_KEY = "3e7cc266ae2b0e0d78e279ce8e361736";
    public static final String PREF_SEARCH_QUERY ="searchQuery";

    private static final int HTTP_REQUEST_SUCCESS = 200;

    private static final String FLICKR_URL = "http://flickr.com/photo.gne?id=%s";
    private static final String METHOD_GETRECENT = "flickr.photos.getRecent";
    private static final String METHOD_SEARCH = "flickr.photos.search";
    private static final String METHOD_GETINFO = "flickr.photos.getInfo";

    private static final String RESPONSE_FORMAT = "json";
    private static final String RESPONSE_NO_CALLBACK = "1";

    private final FlickerApi mFlickerApi;

    @Nullable private Set<Listener> mListeners = new HashSet<>();

    public FlickerClient(FlickerApi flickerApi) {
        mFlickerApi = flickerApi;
    }

    public void setListener(Listener listener) {
        mListeners.add(listener);
    }

    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }

    public void query(@Nullable String text, int page) {
        Call<GetPhotosResponse> call;
        if (TextUtils.isEmpty(text)) {
            call = mFlickerApi.queryRecent(
                    METHOD_GETRECENT,
                    API_KEY,
                    RESPONSE_FORMAT,
                    RESPONSE_NO_CALLBACK,
                    String.valueOf(page));
        } else {
            call = mFlickerApi.queryByText(
                    METHOD_SEARCH,
                    API_KEY,
                    RESPONSE_FORMAT,
                    RESPONSE_NO_CALLBACK,
                    text,
                    String.valueOf(page));
        }

        call.enqueue(new Callback<GetPhotosResponse>() {
            @Override
            public void onResponse(Call<GetPhotosResponse> call, Response<GetPhotosResponse> response) {
                if (response.code() == HTTP_REQUEST_SUCCESS) {
                    for (Listener listener : mListeners) {
                        listener.onQuerySuccess(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetPhotosResponse> call, Throwable t) {
                for (Listener listener : mListeners) {
                    listener.onQueryFailure(t.getMessage());
                }
            }
        });
    }

    public void getPhotoInfo(String photoId) {
        Call<GetPhotoInfoResponse> call = mFlickerApi.getPhotoInfo(
                METHOD_GETINFO,
                API_KEY,
                RESPONSE_FORMAT,
                RESPONSE_NO_CALLBACK,
                photoId);

        call.enqueue(new Callback<GetPhotoInfoResponse>() {
            @Override
            public void onResponse(Call<GetPhotoInfoResponse> call, Response<GetPhotoInfoResponse> response) {
                if (response.code() == HTTP_REQUEST_SUCCESS) {
                    for (Listener listener : mListeners) {
                        listener.onGetInfoSuccess(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetPhotoInfoResponse> call, Throwable t) {
                for (Listener listener : mListeners) {
                    listener.onGetInfoFailure(t.getMessage());
                }
            }
        });
    }

    /**
     * function to create a url for viewing photo in Flickr app<br>
     * @param id : single photo id<br>
     * @return : url for viewing photo in Flickr flagship app<p>
     */
    public static String getFlickrUrl(String id) {
        return String.format(FLICKR_URL, id);
    }

    public interface Listener {

        void onQuerySuccess(GetPhotosResponse response);

        void onQueryFailure(String message);

        void onGetInfoSuccess(GetPhotoInfoResponse response);

        void onGetInfoFailure(String message);
    }
}
