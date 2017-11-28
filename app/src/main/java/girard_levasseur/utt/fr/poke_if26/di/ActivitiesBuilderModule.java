package girard_levasseur.utt.fr.poke_if26.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import girard_levasseur.utt.fr.poke_if26.activities.LoginActivity;
import girard_levasseur.utt.fr.poke_if26.activities.MainActivity;
import girard_levasseur.utt.fr.poke_if26.activities.SignUpActivity;

/**
 * A module that declares all the activities to the dependency injector
 * so it can create them.
 */
@Module
public abstract class ActivitiesBuilderModule {

    @ContributesAndroidInjector()
    abstract LoginActivity provideLoginActivityFactory();

    @ContributesAndroidInjector()
    abstract SignUpActivity provideSignUpActivityFactory();

    @ContributesAndroidInjector()
    abstract MainActivity provideMainActivityFactory();

}
