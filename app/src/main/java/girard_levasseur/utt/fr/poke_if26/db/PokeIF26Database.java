package girard_levasseur.utt.fr.poke_if26.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;

import girard_levasseur.utt.fr.poke_if26.PokeIF26App;

/**
 * Created by victor on 25/11/17.
 */

public class PokeIF26Database extends SQLiteOpenHelper {

    @Inject
    public PokeIF26Database(PokeIF26App app) {
        // TODO: Externalize DB version and name in conf files.
        super(app.getApplicationContext(), "PokeIF26-db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
