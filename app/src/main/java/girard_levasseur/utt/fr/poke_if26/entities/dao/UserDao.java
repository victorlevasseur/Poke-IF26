package girard_levasseur.utt.fr.poke_if26.entities.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import girard_levasseur.utt.fr.poke_if26.entities.User;
import io.reactivex.Single;

/**
 * Define the queries for the User objects in the db.
 *
 * Created by victor on 25/11/17.
 */
@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    public Single<List<User>> getAll();

    @Query("SELECT * FROM user WHERE username = :username")
    public Single<User> getUserByUsername(String username);

}
