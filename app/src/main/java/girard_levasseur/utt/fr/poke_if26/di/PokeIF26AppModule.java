package girard_levasseur.utt.fr.poke_if26.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import girard_levasseur.utt.fr.poke_if26.services.DatabaseService;
import girard_levasseur.utt.fr.poke_if26.services.LoginService;
import girard_levasseur.utt.fr.poke_if26.services.impl.DatabaseServiceImpl;
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
    static DatabaseService provideDatabaseService(DatabaseServiceImpl databaseServiceImpl) {
        return databaseServiceImpl;
    }

}
