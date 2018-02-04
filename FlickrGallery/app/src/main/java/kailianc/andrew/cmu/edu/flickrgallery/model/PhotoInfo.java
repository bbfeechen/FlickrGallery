package kailianc.andrew.cmu.edu.flickrgallery.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class PhotoInfo {

    @SerializedName("dateuploaded")
    public abstract String dateuploaded();

    @SerializedName("isfavorite")
    public abstract int isfavorite();

    @SerializedName("rotation")
    public abstract int rotation();

    @SerializedName("originalformat")
    public abstract String originalformat();

    @SerializedName("title")
    public abstract PhotoTitle title();

    @SerializedName("description")
    public abstract PhotoDescription description();

    public static TypeAdapter<PhotoInfo> typeAdapter(Gson gson) {
        return new AutoValue_PhotoInfo.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_PhotoInfo.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        abstract Builder setDateuploaded(String dateuploaded);
        abstract Builder setIsfavorite(int isfavorite);
        abstract Builder setRotation(int rotation);
        abstract Builder setOriginalformat(String originalformat);
        abstract Builder setTitle(PhotoTitle title);
        abstract Builder setDescription(PhotoDescription description);
        abstract PhotoInfo builder();
    }
}
