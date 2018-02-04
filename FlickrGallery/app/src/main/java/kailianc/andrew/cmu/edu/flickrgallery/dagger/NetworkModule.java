package kailianc.andrew.cmu.edu.flickrgallery.dagger;

import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import kailianc.andrew.cmu.edu.flickrgallery.control.FlickerApi;
import kailianc.andrew.cmu.edu.flickrgallery.control.FlickerClient;
import kailianc.andrew.cmu.edu.flickrgallery.model.AutoValueGsonFactory;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
class NetworkModule {

    private static final String NAME_BASE_URL = "NAME_BASE_URL";
    private static final String BASE_URL = "https://api.flickr.com/";

    @Provides
    @Named(NAME_BASE_URL)
    String provideBaseUrlString() {
        return BASE_URL;
    }

    @Provides
    @Singleton
    Converter.Factory provideGsonConverter() {
        return GsonConverterFactory.create(
            new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create())
                .create()
        );
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Converter.Factory converter, @Named(NAME_BASE_URL) String baseUrl) {
        return new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converter)
            .build();
    }

    @Provides
    @Singleton
    FlickerApi provideFlickerApi(Retrofit retrofit) {
        return retrofit.create(FlickerApi.class);
    }

    @Provides
    @Singleton
    FlickerClient provideFlickerClient(FlickerApi flickerApi) {
        return new FlickerClient(flickerApi);
    }
}
