package girard_levasseur.utt.fr.poke_if26;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import girard_levasseur.utt.fr.poke_if26.di.ActivitiesBuilderModule;
import girard_levasseur.utt.fr.poke_if26.di.PokeIF26ApplicationModule;

/**
 * Base component for the app level di graph.
 * Created by victor on 23/11/17.
 */
@Component(modules = {
        AndroidSupportInjectionModule.class,
        // Modules provided at app level:
        ActivitiesBuilderModule.class, // the activities builder
        PokeIF26ApplicationModule.class
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
