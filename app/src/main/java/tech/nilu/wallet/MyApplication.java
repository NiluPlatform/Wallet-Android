package tech.nilu.wallet;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import io.fabric.sdk.android.Fabric;
import tech.nilu.wallet.injection.AppInjector;

/**
 * Created by Navid on 12/28/17.
 */

public class MyApplication extends Application implements HasActivityInjector, HasServiceInjector {
    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;
    @Inject
    DispatchingAndroidInjector<Service> serviceDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        /*if (LeakCanary.isInAnalyzerProMcess(this))
            return;
        LeakCanary.install(this);
        Stetho.initializeWithDefaults(this);*/

        Fabric.with(this, new Crashlytics());
        AppInjector.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return serviceDispatchingAndroidInjector;
    }
}
