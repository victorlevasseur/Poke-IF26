package girard_levasseur.utt.fr.poke_if26.services.impl;

import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;
import javax.inject.Singleton;

import girard_levasseur.utt.fr.poke_if26.entities.PokemonInstance;
import girard_levasseur.utt.fr.poke_if26.services.PokeIF26Database;
import girard_levasseur.utt.fr.poke_if26.services.PokemonLocationsInitializerService;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by victor on 05/12/17.
 */
@Singleton
public class PokemonLocationsInitializerServiceImpl implements PokemonLocationsInitializerService {

    private PokeIF26Database db;

    private static final PokemonInstance[] defaultPokemonInstances = new PokemonInstance[]{
            new PokemonInstance(2, new LatLng(48.271, 4.06504), 1.0f),
            new PokemonInstance(6, new LatLng(48.2701, 4.0652), 0.5f),
            new PokemonInstance(8, new LatLng(48.2696, 4.06558), 0.2f)
    };

    @Inject
    public PokemonLocationsInitializerServiceImpl(PokeIF26Database db) {
        this.db = db;
    }

    @Override
    public Single<Object> initializeNewLocations() {
        return Single.create((subscriber) -> {
            // Delete all the not captured pokemons.
            db.pokemonInstanceDao().deleteNotCapturedPokemons();

            // Add all the default pokemons. If not satisfying the constraints, it means
            // that the pokemon has been captured. In this case, it's ignored and not added again
            // to the map.
            for (int i = 0; i < defaultPokemonInstances.length; ++i) {
                PokemonInstance instance = defaultPokemonInstances[i];
                try {
                    db.pokemonInstanceDao().insertPokemonInstance(instance);
                    Log.i(PokemonLocationsInitializerServiceImpl.class.getName(),
                            "PokemonInstance at " +
                                    instance.getLocation().toString() +
                                    " added.");
                } catch (SQLiteConstraintException e) {
                    // Just print a log message but ignore the "error".
                    Log.i(PokemonLocationsInitializerServiceImpl.class.getName(),
                            "PokemonInstance at " +
                                    instance.getLocation().toString() +
                                    " already captured, not readded to the map.");
                }
            }

            subscriber.onSuccess(new Object());
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
