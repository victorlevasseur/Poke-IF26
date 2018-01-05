package girard_levasseur.utt.fr.poke_if26.entities.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import girard_levasseur.utt.fr.poke_if26.entities.PokemonInstance;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Queries for PokemonInstances
 *
 * Created by victor on 05/12/17.
 */
@Dao
public interface PokemonInstanceDao {

    @Query("SELECT * FROM pokemoninstance WHERE captured_by_user_id IS NULL")
    Single<List<PokemonInstance>> getNotCapturedPokemons();

    @Query("SELECT * FROM pokemoninstance WHERE captured_by_user_id IS NULL")
    Flowable<List<PokemonInstance>> flowNotCapturedPokemons();

    @Query("SELECT * FROM pokemoninstance WHERE captured_by_user_id = :userId")
    Single<List<PokemonInstance>> getPokemonsCapturedByUser(int userId);

    @Query("SELECT * FROM pokemoninstance WHERE id = :id")
    Single<PokemonInstance> getPokemonInstance(int id);

    /**
     * Release the pokemons captured by an user.
     * @param userId the id of the user to release the pokemons of.
     */
    @Query("UPDATE pokemoninstance SET captured_by_user_id = NULL WHERE captured_by_user_id = :userId")
    void releasePokemonsOfUser(int userId);

    @Insert
    void insertPokemonInstance(PokemonInstance instance);

    @Update
    void updatePokemonInstance(PokemonInstance instance);

    @Query("DELETE FROM pokemoninstance WHERE captured_by_user_id IS NULL")
    void deleteNotCapturedPokemons();
}
