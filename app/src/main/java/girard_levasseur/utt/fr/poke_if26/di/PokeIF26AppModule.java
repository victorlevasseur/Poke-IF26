package girard_levasseur.utt.fr.poke_if26.di;

import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import girard_levasseur.utt.fr.poke_if26.PokeIF26App;
import girard_levasseur.utt.fr.poke_if26.services.LoginService;
import girard_levasseur.utt.fr.poke_if26.services.PokeIF26Database;
import girard_levasseur.utt.fr.poke_if26.services.impl.LoginServiceImpl;

/**
 * Module providing the login service.
 */
@Module
public class PokeIF26AppModule {

    @Provides
    @Singleton // Only one instance shared accross dependant code.
    static LoginService provideLoginService(LoginServiceImpl loginServiceImpl) {
        // Return an instance of the login service impl,
        // instanciated by dagger itself to satisfy its deps.
        return loginServiceImpl;
    }

    @Provides
    @Singleton
    static PokeIF26Database providePokeIF26Database(PokeIF26App app) {
        // Provide the database as it needs to be configured by the Room lib
        // with the app context.
        return Room.databaseBuilder(app.getApplicationContext(),
                PokeIF26Database.class,
                "poke-if26-db.db").build();
    }

}
