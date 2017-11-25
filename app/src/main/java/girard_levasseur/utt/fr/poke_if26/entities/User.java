package girard_levasseur.utt.fr.poke_if26.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Represents an user.
 *
 * Created by victor on 23/11/17.
 */
@Entity
public class User {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "password_hash")
    public String passwordHash;

    public User() {
        super();
    }

    // TODO: Redo ctor
    @Ignore
    public User(String username, String passwordHash) {
        super();
        this.username = username;
        this.passwordHash = passwordHash;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}