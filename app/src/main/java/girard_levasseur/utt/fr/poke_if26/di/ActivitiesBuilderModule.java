package girard_levasseur.utt.fr.poke_if26.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import girard_levasseur.utt.fr.poke_if26.activities.LoginActivity;
import girard_levasseur.utt.fr.poke_if26.activities.MainActivity;
import girard_levasseur.utt.fr.poke_if26.activities.SignUpActivity;
import girard_levasseur.utt.fr.poke_if26.activities.SplashActivity;

/**
 * A module that declares all the activities to the dependency injector
 * so it can create them.
 */
@Module
public abstract class ActivitiesBuilderModule {

    @ContributesAndroidInjector()
    abstract SplashActivity provideSplashActivityFactory();

    @ContributesAndroidInjector()
    abstract LoginActivity provideLoginActivityFactory();

    @ContributesAndroidInjector()
    abstract SignUpActivity provideSignUpActivityFactory();

    // Allow the main activity to inject its main fragments.
    @ContributesAndroidInjector(modules = {
            MainActivityFragmentsProvider.class,
            MainActivityServicesModule.class
    })
    @PerActivityScope
    abstract MainActivity provideMainActivityFactory();

}
