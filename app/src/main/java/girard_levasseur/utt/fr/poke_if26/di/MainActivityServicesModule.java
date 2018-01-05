package girard_levasseur.utt.fr.poke_if26.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import girard_levasseur.utt.fr.poke_if26.activities.EncounterActivity;
import girard_levasseur.utt.fr.poke_if26.services.EncounterListenerService;
import girard_levasseur.utt.fr.poke_if26.services.GPSLocationService;
import girard_levasseur.utt.fr.poke_if26.services.NetworkErrorRetryerService;
import girard_levasseur.utt.fr.poke_if26.services.impl.EncounterListenerServiceImpl;
import girard_levasseur.utt.fr.poke_if26.services.impl.GPSLocationServiceImpl;
import girard_levasseur.utt.fr.poke_if26.services.impl.NetworkErrorRetryerServiceImpl;

/**
 * Created by victor on 29/11/17.
 */
@Module
public abstract class MainActivityServicesModule {

    @Provides
    @PerActivityScope
    static GPSLocationService providesGPSLocationService(GPSLocationServiceImpl impl) {
        return impl;
    }

    @Provides
    @PerActivityScope
    static EncounterListenerService provideEncounterListenerService(
            EncounterListenerServiceImpl impl) {
        return impl;
    }

    @Provides
    @PerActivityScope
    static NetworkErrorRetryerService provideNetworkRetryerService(
            NetworkErrorRetryerServiceImpl impl) {
        return impl;
    }
}
