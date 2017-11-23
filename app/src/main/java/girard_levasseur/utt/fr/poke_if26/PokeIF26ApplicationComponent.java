package girard_levasseur.utt.fr.poke_if26;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import girard_levasseur.utt.fr.poke_if26.di.modules.ActivitiesModule;
import girard_levasseur.utt.fr.poke_if26.di.modules.LoginModule;

/**
 * Base component for the app level di graph.
 * Created by victor on 23/11/17.
 */
@Component(modules = {
        AndroidSupportInjectionModule.class,
        // Modules provided at app level:
        ActivitiesModule.class, // the activities builder
        LoginModule.class
})
@Singleton
public interface PokeIF26ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance Builder application(PokeIF26Application application);
        PokeIF26ApplicationComponent build();
    }
    void inject(PokeIF26Application app);

}
