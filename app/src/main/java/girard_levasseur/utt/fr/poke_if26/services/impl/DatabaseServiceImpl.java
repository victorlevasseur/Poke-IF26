package girard_levasseur.utt.fr.poke_if26.services.impl;

import javax.inject.Inject;

import girard_levasseur.utt.fr.poke_if26.db.PokeIF26Database;
import girard_levasseur.utt.fr.poke_if26.services.DatabaseService;

/**
 * Created by victor on 25/11/17.
 */

public class DatabaseServiceImpl implements DatabaseService {

    private PokeIF26Database database;

    @Inject
    public DatabaseServiceImpl(PokeIF26Database database) {
        this.database = database;
    }

}
