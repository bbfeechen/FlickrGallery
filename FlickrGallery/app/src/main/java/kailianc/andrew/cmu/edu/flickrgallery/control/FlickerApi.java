package kailianc.andrew.cmu.edu.flickrgallery.control;

import kailianc.andrew.cmu.edu.flickrgallery.model.GetPhotoInfoResponse;
import kailianc.andrew.cmu.edu.flickrgallery.model.GetPhotosResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickerApi {

    String ENDPOINT = "services/rest/";

    @GET(ENDPOINT)
    Call<GetPhotosResponse> queryByText(
            @Query("method") String method,
            @Query("api_key") String apiKey,
            @Query("format") String format,
            @Query("nojsoncallback") String nojsoncallback,
            @Query("text") String text,
            @Query("page") String page);

    @GET(ENDPOINT)
    Call<GetPhotosResponse> queryRecent(
            @Query("method") String method,
            @Query("api_key") String apiKey,
            @Query("format") String format,
            @Query("nojsoncallback") String nojsoncallback,
            @Query("page") String page);

    @GET(ENDPOINT)
    Call<GetPhotoInfoResponse> getPhotoInfo(
            @Query("method") String method,
            @Query("api_key") String apiKey,
            @Query("format") String format,
            @Query("nojsoncallback") String nojsoncallback,
            @Query("photo_id") String photoId);
}
