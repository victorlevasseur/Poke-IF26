package girard_levasseur.utt.fr.poke_if26.services.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import girard_levasseur.utt.fr.poke_if26.entities.PokemonInstance;
import girard_levasseur.utt.fr.poke_if26.services.PokeIF26Database;
import girard_levasseur.utt.fr.poke_if26.services.PokemonLocationsService;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by victor on 05/12/17.
 */
@Singleton
public class PokemonLocationsServiceImpl implements PokemonLocationsService {

    private PokeIF26Database db;

    @Inject
    public PokemonLocationsServiceImpl(PokeIF26Database db) {
        this.db = db;
    }

    @Override
    public Single<List<PokemonInstance>> getAvailablePokemons() {
        return db.pokemonInstanceDao().getNotCapturedPokemons()
                .subscribeOn(Schedulers.io());
    }
}
