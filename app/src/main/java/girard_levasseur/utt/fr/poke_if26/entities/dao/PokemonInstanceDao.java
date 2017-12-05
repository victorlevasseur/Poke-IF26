package girard_levasseur.utt.fr.poke_if26.entities.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import girard_levasseur.utt.fr.poke_if26.entities.PokemonInstance;
import io.reactivex.Single;

/**
 * Queries for PokemonInstances
 *
 * Created by victor on 05/12/17.
 */
@Dao
public interface PokemonInstanceDao {

    @Query("SELECT * FROM pokemoninstance WHERE captured_by_user_id = NULL")
    Single<List<PokemonInstance>> getNotCapturedPokemons();

    @Query("SELECT * FROM pokemoninstance WHERE captured_by_user_id = :userId")
    Single<List<PokemonInstance>> getPokemonsCapturedByUser(int userId);

}
