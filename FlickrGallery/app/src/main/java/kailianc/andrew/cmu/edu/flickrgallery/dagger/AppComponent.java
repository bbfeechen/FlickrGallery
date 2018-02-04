package kailianc.andrew.cmu.edu.flickrgallery.dagger;

import dagger.Component;
import javax.inject.Singleton;
import kailianc.andrew.cmu.edu.flickrgallery.view.FeedFragment;
import kailianc.andrew.cmu.edu.flickrgallery.view.PhotoFragment;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    void inject(FeedFragment feedFragment);

    void inject(PhotoFragment photoFragment);
}
