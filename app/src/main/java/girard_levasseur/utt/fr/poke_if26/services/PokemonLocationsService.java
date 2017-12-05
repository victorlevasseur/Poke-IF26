package girard_levasseur.utt.fr.poke_if26.services;

import java.util.List;

import girard_levasseur.utt.fr.poke_if26.entities.PokemonInstance;
import io.reactivex.Single;

/**
 * Created by victor on 05/12/17.
 */

public interface PokemonLocationsService {

    Single<List<PokemonInstance>> getAvailablePokemons();

}
