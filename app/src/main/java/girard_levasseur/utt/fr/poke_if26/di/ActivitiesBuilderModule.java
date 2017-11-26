package girard_levasseur.utt.fr.poke_if26.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import girard_levasseur.utt.fr.poke_if26.activities.login.LoginActivity;
import girard_levasseur.utt.fr.poke_if26.activities.signup.SignUpActivity;

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

}
