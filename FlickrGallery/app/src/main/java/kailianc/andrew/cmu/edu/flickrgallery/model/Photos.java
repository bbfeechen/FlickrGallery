package kailianc.andrew.cmu.edu.flickrgallery.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class Photos {

    @SerializedName("page")
    public abstract int page();

    @SerializedName("pages")
    public abstract int pages();

    @SerializedName("perpage")
    public abstract int perpage();

    @SerializedName("total")
    public abstract String total();

    @SerializedName("photo")
    public abstract List<Photo> photos();

    public static TypeAdapter<Photos> typeAdapter(Gson gson) {
        return new AutoValue_Photos.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_Photos.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        abstract Builder setPage(int page);
        abstract Builder setPages(int pages);
        abstract Builder setPerpage(int perpage);
        abstract Builder setTotal(String total);
        abstract Builder setPhotos(List<Photo> photos);
        abstract Photos builder();
    }
}
