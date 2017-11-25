package girard_levasseur.utt.fr.poke_if26.services;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import girard_levasseur.utt.fr.poke_if26.entities.dao.UserDao;
import girard_levasseur.utt.fr.poke_if26.entities.User;

/**
 * Created by victor on 25/11/17.
 */
@Database(entities = {User.class}, version = 1)
public abstract class PokeIF26Database extends RoomDatabase {

    public abstract UserDao userDao();

}
