package tech.nilu.wallet.injection;

import android.app.Application;

import javax.inject.Singleton;

import tech.nilu.wallet.MyApplication;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by Navid on 12/28/17.
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        ActivityBuilderModule.class,
        ServiceBuilderModule.class
})
public interface AppComponent {
    void inject(MyApplication niluApplication);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}
