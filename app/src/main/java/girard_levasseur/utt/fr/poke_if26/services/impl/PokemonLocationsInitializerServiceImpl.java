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
            new PokemonInstance(1, new LatLng(37.41, -122.14)),
            new PokemonInstance(3, new LatLng(37.425, -122.09))
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
