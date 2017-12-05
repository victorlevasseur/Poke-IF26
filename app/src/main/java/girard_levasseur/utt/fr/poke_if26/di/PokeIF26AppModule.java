package girard_levasseur.utt.fr.poke_if26.di;

import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import me.sargunvohra.lib.pokekotlin.client.PokeApi;

import dagger.Module;
import dagger.Provides;
import girard_levasseur.utt.fr.poke_if26.PokeIF26App;
import girard_levasseur.utt.fr.poke_if26.services.LoginService;
import girard_levasseur.utt.fr.poke_if26.services.PokeIF26Database;
import girard_levasseur.utt.fr.poke_if26.services.PokemonsService;
import girard_levasseur.utt.fr.poke_if26.services.UserService;
import girard_levasseur.utt.fr.poke_if26.services.impl.LoginServiceImpl;
import girard_levasseur.utt.fr.poke_if26.services.impl.PokemonsServiceImpl;
import girard_levasseur.utt.fr.poke_if26.services.impl.UserServiceImpl;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;

/**
 * Module providing the login service.
 */
@Module
public class PokeIF26AppModule {

    @Provides
    @Singleton
    static PokeIF26Database providePokeIF26Database(PokeIF26App app) {
        // Provide the database as it needs to be configured by the Room lib
        // with the app context.
        return Room.databaseBuilder(app.getApplicationContext(),
                PokeIF26Database.class,
                "poke-if26-db.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    static LoginService provideLoginService(LoginServiceImpl impl) {
        // Return an instance of the login service impl,
        // instanciated by dagger itself to satisfy its deps.
        return impl;
    }

    @Provides
    @Singleton
    static PokemonsService providePokemonLocationsService(
            PokemonsServiceImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    static UserService provideUserService(UserServiceImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    static PokeApi providePokeApi() {
        return new PokeApiClient();
    }

}
