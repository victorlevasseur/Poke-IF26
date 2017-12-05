package girard_levasseur.utt.fr.poke_if26.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import girard_levasseur.utt.fr.poke_if26.dto.FetchedPokemonInstance;
import girard_levasseur.utt.fr.poke_if26.entities.PokemonInstance;
import girard_levasseur.utt.fr.poke_if26.services.PokeIF26Database;
import girard_levasseur.utt.fr.poke_if26.services.PokemonLocationsService;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.sargunvohra.lib.pokekotlin.client.PokeApi;

/**
 * Created by victor on 05/12/17.
 */
@Singleton
public class PokemonLocationsServiceImpl implements PokemonLocationsService {

    private PokeApi pokeApi;

    private PokeIF26Database db;

    @Inject
    public PokemonLocationsServiceImpl(PokeApi pokeApi, PokeIF26Database db) {
        this.pokeApi = pokeApi;
        this.db = db;
    }

    @Override
    public Single<List<FetchedPokemonInstance>> getAvailablePokemons() {
        return db.pokemonInstanceDao().getNotCapturedPokemons()
                .subscribeOn(Schedulers.io())
                .map((pokemons) -> {
                    List<FetchedPokemonInstance> fetchedPokemonInstanceList = new ArrayList<>();
                    for (Iterator<PokemonInstance> it = pokemons.iterator(); it.hasNext();) {
                        // Map the PokemonInstance to a FetchedPokemonInstance.
                        PokemonInstance pokemonInstance = it.next();
                        FetchedPokemonInstance fetchedPokemonInstance =
                                new FetchedPokemonInstance.Builder()
                                        .setId(pokemonInstance.getId())
                                        .setLocation(pokemonInstance.getLocation())
                                        .setCapturedByUserId(pokemonInstance.getCapturedByUserId())
                                        .setPokemon(pokeApi.getPokemon(pokemonInstance.getPokemonId()))
                                        .build();

                        fetchedPokemonInstanceList.add(fetchedPokemonInstance);
                    }
                    return fetchedPokemonInstanceList;
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}
