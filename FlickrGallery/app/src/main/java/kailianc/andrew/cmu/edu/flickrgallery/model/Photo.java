package kailianc.andrew.cmu.edu.flickrgallery.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@AutoValue
public abstract class Photo implements Serializable {

    @SerializedName("id")
    public abstract String id();

    @SerializedName("owner")
    public abstract String owner();

    @SerializedName("secret")
    public abstract String secret();

    @SerializedName("server")
    public abstract String server();

    @SerializedName("farm")
    public abstract int farm();

    @SerializedName("title")
    public abstract String title();

    @SerializedName("ispublic")
    public abstract int ispublic();

    @SerializedName("isfriend")
    public abstract int isfriend();

    @SerializedName("isfamily")
    public abstract int isfamily();

    public static TypeAdapter<Photo> typeAdapter(Gson gson) {
        return new AutoValue_Photo.GsonTypeAdapter(gson);
    }

    public String url(){
        return "http://farm" + farm() + ".static.flickr.com/" + server()
            + "/" + id() + "_" + secret() + ".jpg";
    }

    public static Builder builder() {
        return new AutoValue_Photo.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        abstract Builder setId(String id);
        abstract Builder setOwner(String owner);
        abstract Builder setSecret(String secret);
        abstract Builder setServer(String server);
        abstract Builder setFarm(int farm);
        abstract Builder setTitle(String title);
        abstract Builder setIspublic(int ispublic);
        abstract Builder setIsfriend(int isFriend);
        abstract Builder setIsfamily(int isfamily);
        abstract Photo build();
    }
}
