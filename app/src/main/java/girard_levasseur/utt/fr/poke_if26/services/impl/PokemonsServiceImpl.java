package girard_levasseur.utt.fr.poke_if26.services.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import girard_levasseur.utt.fr.poke_if26.dto.FetchedPokemonInstance;
import girard_levasseur.utt.fr.poke_if26.entities.PokemonInstance;
import girard_levasseur.utt.fr.poke_if26.services.PokeIF26Database;
import girard_levasseur.utt.fr.poke_if26.services.PokemonsService;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;

/**
 * Created by victor on 05/12/17.
 */
@Singleton
public class PokemonsServiceImpl implements PokemonsService {

    private PokeApi pokeApi;

    private PokeIF26Database db;

    @Inject
    public PokemonsServiceImpl(PokeApi pokeApi, PokeIF26Database db) {
        this.pokeApi = pokeApi;
        this.db = db;
    }

    @Override
    public Single<FetchedPokemonInstance> fetchPokemon(PokemonInstance pokemonInstance) {
        return Single.just(new Object())
                .observeOn(Schedulers.io())
                .map((useless) -> {
                    Pokemon pokemonData = pokeApi.getPokemon(pokemonInstance.getPokemonId());
                    Bitmap pokemonBitmap = null;
                    if (pokemonData.getSprites().getFrontDefault() != null) {
                        pokemonBitmap = getBitmapFromURL(
                                pokemonData.getSprites().getFrontDefault());
                        pokemonBitmap = Bitmap.createScaledBitmap(
                                pokemonBitmap,
                                pokemonBitmap.getWidth() * 2,
                                pokemonBitmap.getHeight() * 2,
                                false);
                    }

                    return new FetchedPokemonInstance.Builder()
                            .setId(pokemonInstance.getId())
                            .setLocation(pokemonInstance.getLocation())
                            .setCapturedByUserId(pokemonInstance.getCapturedByUserId())
                            .setPokemon(pokemonData)
                            .setPokemonImage(pokemonBitmap)
                            .build();
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<PokemonInstance>> getAvailablePokemons() {
        return db.pokemonInstanceDao().getNotCapturedPokemons()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<FetchedPokemonInstance>> getAvailableFetchedPokemons() {
        return getAvailablePokemons()
                .observeOn(Schedulers.io())
                .flatMap((pokemons) -> {
                    if (pokemons.size() == 0) {
                        return Single.just(new ArrayList<FetchedPokemonInstance>());
                    }

                    List<Single<FetchedPokemonInstance>> fetchedPokemonInstanceSinglesList =
                            new ArrayList<>();
                    for (Iterator<PokemonInstance> it = pokemons.iterator(); it.hasNext();) {
                        // Map the PokemonInstance to a Single<FetchedPokemonInstance.>
                        fetchedPokemonInstanceSinglesList.add(fetchPokemon(it.next()));
                    }
                    // Zip all the observables into an observable of a list of all the results.
                    return Single.zip(
                            fetchedPokemonInstanceSinglesList,
                            (array) -> Arrays.asList(Arrays.copyOf(
                                            array,
                                            array.length,
                                            FetchedPokemonInstance[].class)));
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // TODO: Fix exception
            e.printStackTrace();
            return null;
        }
    }
}
