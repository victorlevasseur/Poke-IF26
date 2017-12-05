package girard_levasseur.utt.fr.poke_if26.services;

import java.util.List;

import girard_levasseur.utt.fr.poke_if26.dto.FetchedPokemonInstance;
import girard_levasseur.utt.fr.poke_if26.entities.PokemonInstance;
import io.reactivex.Single;

/**
 * Created by victor on 05/12/17.
 */

public interface PokemonsService {

    /**
     * Fetch a single pokemon from the API.
     * @param pokemonInstance the pokemon instance to fetch
     * @return an observable to the fetched pokemon instance. The observable will do
     * further operations on the UI thread.
     */
    Single<FetchedPokemonInstance> fetchPokemon(PokemonInstance pokemonInstance);

    /**
     * Get the available pokemons.
     * @return an observable to a list of available pokemons. The observable will do
     * further operations on the UI thread.
     */
    Single<List<PokemonInstance>> getAvailablePokemons();

    /**
     * Get a list of available pokemon with their pokemon data fetched from the PokeAPI.
     *
     * @return a single observable to a list of fetched pokemon instances. The observable will do
     * further operations on the UI thread.
     */
    Single<List<FetchedPokemonInstance>> getAvailableFetchedPokemons();

}
