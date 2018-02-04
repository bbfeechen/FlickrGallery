package kailianc.andrew.cmu.edu.flickrgallery.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class GetPhotosResponse {

    @SerializedName("photos")
    public abstract Photos photos();

    public static TypeAdapter<GetPhotosResponse> typeAdapter(Gson gson) {
        return new AutoValue_GetPhotosResponse.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_GetPhotosResponse.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        abstract Builder setPhotos(Photos photos);
        abstract GetPhotosResponse build();
    }
}
