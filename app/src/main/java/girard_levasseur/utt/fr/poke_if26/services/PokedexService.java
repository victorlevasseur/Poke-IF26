package girard_levasseur.utt.fr.poke_if26.services;

import java.util.List;

import girard_levasseur.utt.fr.poke_if26.dto.FetchedPokemonInstance;
import girard_levasseur.utt.fr.poke_if26.entities.PokemonInstance;
import io.reactivex.Single;

/**
 * Created by antoine on 12.12.17.
 */

public interface PokedexService {
    Single<List<PokemonInstance>> getCapturedPokemonInstances();
    Single<List<FetchedPokemonInstance>> getCapturedFetchedPokemonInstances();
}
