package tech.nilu.wallet.injection;

import android.app.Application;
import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Navid on 12/28/17.
 */

@Module(includes = {DatabaseModule.class, NetworkModule.class, ViewModelModule.class, WebServiceModule.class})
class AppModule {
    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    FirebaseAnalytics provideFirebaseAnalytics(Application application) {
        return FirebaseAnalytics.getInstance(application.getApplicationContext());
    }
}
