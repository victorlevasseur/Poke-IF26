package girard_levasseur.utt.fr.poke_if26.services;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import girard_levasseur.utt.fr.poke_if26.entities.PokemonInstance;
import girard_levasseur.utt.fr.poke_if26.entities.converters.LatLngConverters;
import girard_levasseur.utt.fr.poke_if26.entities.dao.PokemonInstanceDao;
import girard_levasseur.utt.fr.poke_if26.entities.dao.UserDao;
import girard_levasseur.utt.fr.poke_if26.entities.User;

/**
 * Created by victor on 25/11/17.
 */
@Database(entities = {PokemonInstance.class, User.class}, version = 4)
@TypeConverters({LatLngConverters.class})
public abstract class PokeIF26Database extends RoomDatabase {

    public abstract PokemonInstanceDao pokemonInstanceDao();

    public abstract UserDao userDao();

}
