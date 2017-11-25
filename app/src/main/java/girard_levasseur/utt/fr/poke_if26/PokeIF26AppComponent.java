package girard_levasseur.utt.fr.poke_if26;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import girard_levasseur.utt.fr.poke_if26.di.ActivitiesBuilderModule;
import girard_levasseur.utt.fr.poke_if26.di.PokeIF26AppModule;

/**
 * Base component for the app level di graph.
 * Created by victor on 23/11/17.
 */
@Component(modules = {
        AndroidSupportInjectionModule.class,
        // Modules provided at app level:
        ActivitiesBuilderModule.class, // the activities builder
        PokeIF26AppModule.class
})
@Singleton
public interface PokeIF26AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance Builder application(PokeIF26App application);
        PokeIF26AppComponent build();
    }
    void inject(PokeIF26App app);

}
