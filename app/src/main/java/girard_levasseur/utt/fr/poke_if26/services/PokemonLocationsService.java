package girard_levasseur.utt.fr.poke_if26.services;

import java.util.List;

import girard_levasseur.utt.fr.poke_if26.dto.FetchedPokemonInstance;
import io.reactivex.Single;

/**
 * Created by victor on 05/12/17.
 */

public interface PokemonLocationsService {

    /**
     * Get a list of available pokemon with their pokemon data fetched from the PokeAPI.
     *
     * @return a single observable to a list of fetched pokemon instances. The observable will do
     * further operations on the UI thread.
     */
    Single<List<FetchedPokemonInstance>> getAvailablePokemons();

}
