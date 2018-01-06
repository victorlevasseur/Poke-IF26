package girard_levasseur.utt.fr.poke_if26.services;

import android.view.KeyCharacterMap;

import java.util.List;
import java.util.Optional;

import girard_levasseur.utt.fr.poke_if26.dto.FetchedPokemonInstance;
import girard_levasseur.utt.fr.poke_if26.entities.PokemonInstance;
import girard_levasseur.utt.fr.poke_if26.entities.User;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by victor on 05/12/17.
 */

public interface PokemonsService {

    /**
     * Get a pokemon instance by its instance ID.
     * @param id the id
     * @return a single that emits an optional PokemonInstance.
     */
    Single<Optional<PokemonInstance>> getPokemonInstanceById(int id);

    /**
     * Get a fetched pokemon instance by its instance ID.
     * @param id the id
     * @return a single that emits an optional PokemonInstance.
     */
    Single<Optional<FetchedPokemonInstance>> getFetchedPokemonInstanceById(int id);

    /**
     * Get the available pokemons.
     * @return an observable to a list of available pokemons. The observable will do
     * further operations on the UI thread.
     */
    Single<List<PokemonInstance>> getAvailablePokemons();

    /**
     * Get the pokemons captured by an user.
     * @return an observable to a list of captured pokemons. The observable will do
     * further operations on the UI thread.
     */
    Single<List<PokemonInstance>> getCapturedPokemonByUser(User user);

    /**
     * Get the available pokemons and the updates of the available pokemons.
     * @return a flowable to a list of available pokemons. Each time the available pokemons change,
     * the flowable emits a new value.
     */
    Flowable<List<PokemonInstance>> flowAvailablePokemons();

    /**
     * Fetch a single pokemon from the API.
     * @param pokemonInstance the pokemon instance to fetch
     * @return an observable to the fetched pokemon instance. The observable will do
     * further operations on the UI thread.
     */
    Single<FetchedPokemonInstance> fetchPokemon(PokemonInstance pokemonInstance);

    /**
     * Get a list of available pokemon with their pokemon data fetched from the PokeAPI.
     *
     * @return a single observable to a list of fetched pokemon instances. The observable will do
     * further operations on the UI thread.
     */
    Single<List<FetchedPokemonInstance>> getAvailableFetchedPokemons();

    /**
     * Get the list of pokemons captured by the user with their pokemon data fetched from the PokeAPI.
     *
     * @return a single observable to a list of fetched pokemon instances. The observable will do
     * further operations on the UI thread.
     */
    Single<List<FetchedPokemonInstance>> getCapturedFetchedPokemonsByUser(User user);

    /**
     * Try to capture a pokemon.
     * @param pokemonId the pokemon instance id
     * @param byUser the user that captured the pokemon
     * @return a single observable to a boolean telling if the capture succeeded or not.
     */
    Single<Boolean> capturePokemonById(int pokemonId, User byUser);

}
