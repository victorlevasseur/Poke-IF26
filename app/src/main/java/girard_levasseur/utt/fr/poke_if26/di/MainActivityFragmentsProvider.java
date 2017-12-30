package girard_levasseur.utt.fr.poke_if26.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import girard_levasseur.utt.fr.poke_if26.fragments.ExploreMapFragment;
import girard_levasseur.utt.fr.poke_if26.fragments.PokedexFragment;
import girard_levasseur.utt.fr.poke_if26.fragments.SettingsFragment;

/**
 * A Module that declares all the providers for the main fragments used by the MainActivity.
 * Created by victor on 28/11/17.
 */
@Module
public abstract class MainActivityFragmentsProvider {

    @ContributesAndroidInjector()
    abstract ExploreMapFragment provideMapFragmentFactory();

    @ContributesAndroidInjector()
    abstract PokedexFragment providePokedexFragmentFactory();

    @ContributesAndroidInjector()
    abstract SettingsFragment provideSettingsFragmentFactory();
}
