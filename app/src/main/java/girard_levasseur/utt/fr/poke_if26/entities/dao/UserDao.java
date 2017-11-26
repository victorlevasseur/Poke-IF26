package girard_levasseur.utt.fr.poke_if26.entities.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.VisibleForTesting;

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
    Single<List<User>> getAll();

    @Query("SELECT * FROM user WHERE username = :username")
    Single<User> getUserByUsername(String username);

    @Query("SELECT * FROM user WHERE id = :id")
    Single<User> getUserById(long id);

    @Query("SELECT * FROM user")
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    List<User> getAllSync();

    @Query("SELECT * FROM user WHERE id = :id")
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    User getUserByIdSync(long id);

    @Insert
    long insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);
}
