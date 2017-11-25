package girard_levasseur.utt.fr.poke_if26;

import android.app.Activity;
import android.app.Application;

import com.activeandroid.ActiveAndroid;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Custom application implementation.
 *
 * Allows Dagger to manage the activities as dependencies with DI.
 */
public class PokeIF26App extends Application implements HasActivityInjector {
    @Inject DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        DaggerPokeIF26AppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
