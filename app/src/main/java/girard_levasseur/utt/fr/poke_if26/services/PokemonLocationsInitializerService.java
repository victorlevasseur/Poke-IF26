package girard_levasseur.utt.fr.poke_if26.services;

import io.reactivex.Single;

/**
 * Created by victor on 05/12/17.
 */

public interface PokemonLocationsInitializerService {

    /**
     * Initialize the pokemons locations.
     *
     * It removes all the non-captured pokemons and add the new one if not already captured or if
     * none is present at the same location.
     *
     * @return a single observable emitting when the initialization is done.
     */
    Single<Object> initializeNewLocations();

}
