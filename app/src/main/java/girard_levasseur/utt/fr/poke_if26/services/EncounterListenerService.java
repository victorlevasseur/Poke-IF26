package girard_levasseur.utt.fr.poke_if26.services;

import girard_levasseur.utt.fr.poke_if26.entities.PokemonInstance;
import io.reactivex.Observable;

/**
 * Created by victor on 07/12/17.
 */
public interface EncounterListenerService {

    Observable<PokemonInstance> onPokemonEncountered();

}
