package girard_levasseur.utt.fr.poke_if26.services.impl;

import android.arch.persistence.room.EmptyResultSetException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import girard_levasseur.utt.fr.poke_if26.dto.FetchedPokemonInstance;
import girard_levasseur.utt.fr.poke_if26.entities.PokemonInstance;
import girard_levasseur.utt.fr.poke_if26.entities.User;
import girard_levasseur.utt.fr.poke_if26.services.PokeIF26Database;
import girard_levasseur.utt.fr.poke_if26.services.PokemonsService;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
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
    public Single<Optional<PokemonInstance>> getPokemonInstanceById(int id) {
        return db.pokemonInstanceDao().getPokemonInstance(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Optional::of)
                .onErrorResumeNext((e) -> {
                    if (e instanceof EmptyResultSetException) {
                        return Single.just(Optional.empty());
                    } else {
                        return Single.error(e);
                    }
                });
    }

    @Override
    public Single<Optional<FetchedPokemonInstance>> getFetchedPokemonInstanceById(int id) {
        return getPokemonInstanceById(id)
                .flatMap((pokemonInstance -> {
                    if (pokemonInstance.isPresent()) {
                        return fetchPokemon(pokemonInstance.get())
                                .map(Optional::of);
                    } else {
                        return Single.just(Optional.empty());
                    }
                }));
    }

    @Override
    public Single<List<PokemonInstance>> getAvailablePokemons() {
        return db.pokemonInstanceDao().getNotCapturedPokemons()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<List<PokemonInstance>> getCapturedPokemonByUser(User user) {
        return db.pokemonInstanceDao().getPokemonsCapturedByUser(user.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Flowable<List<PokemonInstance>> flowAvailablePokemons() {
        return db.pokemonInstanceDao().flowNotCapturedPokemons();
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
                            .setCapturability(pokemonInstance.getCapturability())
                            .setCapturedByUserId(pokemonInstance.getCapturedByUserId())
                            .setPokemon(pokemonData)
                            .setPokemonImage(pokemonBitmap)
                            .build();
                })
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


    @Override
    public Single<List<FetchedPokemonInstance>> getCapturedFetchedPokemonsByUser(User user) {
        return this.getCapturedPokemonByUser(user)
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


    @Override
    public Single<Boolean> capturePokemonById(int pokemonId, User byUser) {
        return getPokemonInstanceById(pokemonId)
                .flatMap((pokemonInstanceOptional) -> {
                    if (pokemonInstanceOptional.isPresent()) {
                        if (Math.random() <= pokemonInstanceOptional.get().getCapturability()) {
                            pokemonInstanceOptional.get().setCapturedByUserId(byUser.getId());
                            return Single.<Boolean>create((single) -> {
                                db.pokemonInstanceDao().updatePokemonInstance(pokemonInstanceOptional.get());
                                single.onSuccess(Boolean.TRUE);
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());
                        } else {
                            return Single.just(Boolean.FALSE);
                        }
                    } else {
                        return Single.just(Boolean.FALSE);
                    }
                });
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
