package kailianc.andrew.cmu.edu.flickrgallery.dagger;

import android.app.Application;

public class FlickerApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .networkModule(new NetworkModule())
            .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
