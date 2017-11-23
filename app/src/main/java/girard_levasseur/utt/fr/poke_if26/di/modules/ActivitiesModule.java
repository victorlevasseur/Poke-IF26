package girard_levasseur.utt.fr.poke_if26.di.modules;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import girard_levasseur.utt.fr.poke_if26.activities.login.LoginActivity;
import girard_levasseur.utt.fr.poke_if26.activities.login.LoginActivityModule;

/**
 * A module that declares all the activities to the dependency injector
 * so it can create them.
 */
@Module
public abstract class ActivitiesModule {

    // Declare the
    @ContributesAndroidInjector(modules = LoginActivityModule.class)
    abstract LoginActivity provideLoginActivityFactory();

}
