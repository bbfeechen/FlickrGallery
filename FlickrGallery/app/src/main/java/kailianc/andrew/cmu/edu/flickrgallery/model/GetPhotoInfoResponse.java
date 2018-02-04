package kailianc.andrew.cmu.edu.flickrgallery.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class GetPhotoInfoResponse {

    @SerializedName("photo")
    public abstract PhotoInfo photoInfo();

    public static TypeAdapter<GetPhotoInfoResponse> typeAdapter(Gson gson) {
        return new AutoValue_GetPhotoInfoResponse.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_GetPhotoInfoResponse.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        abstract Builder setPhotoInfo(PhotoInfo photoInfo);
        abstract GetPhotoInfoResponse build();
    }
}
