package kailianc.andrew.cmu.edu.flickrgallery.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class PhotoDescription {

    @SerializedName("_content")
    public abstract String content();


    public static TypeAdapter<PhotoDescription> typeAdapter(Gson gson) {
        return new AutoValue_PhotoDescription.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_PhotoDescription.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        abstract Builder setContent(String content);
        abstract PhotoDescription build();
    }
}
