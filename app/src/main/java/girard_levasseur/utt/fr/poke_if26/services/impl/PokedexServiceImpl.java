package girard_levasseur.utt.fr.poke_if26.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import girard_levasseur.utt.fr.poke_if26.dto.FetchedPokemonInstance;
import girard_levasseur.utt.fr.poke_if26.entities.PokemonInstance;
import girard_levasseur.utt.fr.poke_if26.entities.User;
import girard_levasseur.utt.fr.poke_if26.services.LoginService;
import girard_levasseur.utt.fr.poke_if26.services.PokeIF26Database;
import girard_levasseur.utt.fr.poke_if26.services.PokedexService;
import girard_levasseur.utt.fr.poke_if26.services.PokemonsService;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by antoine on 12.12.17.
 */

@Singleton
public class PokedexServiceImpl implements PokedexService {
    private LoginService loginService;
    private PokemonsService pokemonsService;
    private PokeIF26Database database;

    @Inject
    public PokedexServiceImpl(LoginService loginService, PokemonsService pokemonsService, PokeIF26Database database) {
        this.loginService = loginService;
        this.pokemonsService = pokemonsService;
        this.database = database;
    }

    public Single<List<PokemonInstance>> getCapturedPokemonInstances() {
        User user = loginService.getConnectedUser();
        return database.pokemonInstanceDao().getPokemonsCapturedByUser(user.getId());
    }

    public Single<List<FetchedPokemonInstance>> getCapturedFetchedPokemonInstances() {
        return getCapturedPokemonInstances()
                .observeOn(Schedulers.io())
                .flatMap((pokemons) -> {
                    if (pokemons.size() == 0) {
                        return Single.just(new ArrayList<FetchedPokemonInstance>());
                    }

                    List<Single<FetchedPokemonInstance>> fetchedPokemonInstanceSinglesList =
                            new ArrayList<>();
                    for (Iterator<PokemonInstance> it = pokemons.iterator(); it.hasNext();) {
                        // Map the PokemonInstance to a Single<FetchedPokemonInstance.>
                        fetchedPokemonInstanceSinglesList.add(pokemonsService.fetchPokemon(it.next()));
                    }
                    // Zip all the observables into an observable of a list of all the results.
                    return Single.zip(
                            fetchedPokemonInstanceSinglesList,
                            (array) -> Arrays.asList(Arrays.copyOf(
                                    array,
                                    array.length,
                                    FetchedPokemonInstance[].class))
                    );
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}

