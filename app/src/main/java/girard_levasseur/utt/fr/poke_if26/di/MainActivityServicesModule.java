package girard_levasseur.utt.fr.poke_if26.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import girard_levasseur.utt.fr.poke_if26.services.GPSLocationService;
import girard_levasseur.utt.fr.poke_if26.services.impl.GPSLocationServiceImpl;

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
}
