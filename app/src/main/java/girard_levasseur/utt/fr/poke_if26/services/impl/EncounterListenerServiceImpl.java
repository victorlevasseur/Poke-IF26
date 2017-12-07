package girard_levasseur.utt.fr.poke_if26.services.impl;

import android.location.Location;

import javax.inject.Inject;

import girard_levasseur.utt.fr.poke_if26.di.PerActivityScope;
import girard_levasseur.utt.fr.poke_if26.entities.PokemonInstance;
import girard_levasseur.utt.fr.poke_if26.services.EncounterListenerService;
import girard_levasseur.utt.fr.poke_if26.services.GPSLocationService;
import girard_levasseur.utt.fr.poke_if26.services.PokemonsService;
import io.reactivex.Observable;

/**
 * Created by victor on 07/12/17.
 */
@PerActivityScope
public class EncounterListenerServiceImpl implements EncounterListenerService {

    private GPSLocationService gpsLocationService;

    private PokemonsService pokemonsService;

    @Inject
    public EncounterListenerServiceImpl(GPSLocationService gpsLocationService,
                                        PokemonsService pokemonsService) {
        this.gpsLocationService = gpsLocationService;
        this.pokemonsService = pokemonsService;
    }

    @Override
    public Observable<PokemonInstance> onPokemonEncountered() {
        return Observable.combineLatest(
                pokemonsService.flowAvailablePokemons().toObservable(),
                gpsLocationService.getLocationUpdates(),
                (pokemons, location) -> {
                    for (PokemonInstance pokemon : pokemons) {
                        // Compare the distance between the pokemon and the player
                        float[] distanceResult = new float[1];
                        Location.distanceBetween(
                                pokemon.getLocation().latitude,
                                pokemon.getLocation().longitude,
                                location.getLatitude(),
                                location.getLongitude(),
                                distanceResult);
                        if (distanceResult[0] < 10) {
                            return pokemon;
                        }
                    }
                    return PokemonInstance.INVALID;
                })
                .filter(pokemonInstance -> pokemonInstance != PokemonInstance.INVALID)
                .distinctUntilChanged();
    }

}
