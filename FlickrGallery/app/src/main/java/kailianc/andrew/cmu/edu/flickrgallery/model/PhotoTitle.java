package kailianc.andrew.cmu.edu.flickrgallery.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
abstract class PhotoTitle {

    @SerializedName("_content")
    public abstract String content();

    public static TypeAdapter<PhotoTitle> typeAdapter(Gson gson) {
        return new AutoValue_PhotoTitle.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_PhotoTitle.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        abstract Builder setContent(String content);

        abstract PhotoTitle build();
    }
}
