package girard_levasseur.utt.fr.poke_if26.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import girard_levasseur.utt.fr.poke_if26.activities.login.LoginActivity;

/**
 * A module that declares all the activities to the dependency injector
 * so it can create them.
 */
@Module
public abstract class ActivitiesBuilderModule {

    // Declare the
    @ContributesAndroidInjector()
    abstract LoginActivity provideLoginActivityFactory();

}
