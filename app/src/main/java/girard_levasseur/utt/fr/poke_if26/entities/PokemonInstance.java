package girard_levasseur.utt.fr.poke_if26.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

/**
 * Represents a spawn instance of a pokemon on the map that
 * may or may not have been captured.
 *
 * Created by victor on 05/12/17.
 */
@Entity(foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "id",
        childColumns = "captured_by_user_id"))
public class PokemonInstance {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "pokemon_id")
    private int pokemonId;

    @ColumnInfo(name = "lat_lng")
    private LatLng location;

    @ColumnInfo(name = "captured_by_user_id")
    private int capturedByUserId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public int getCapturedByUserId() {
        return capturedByUserId;
    }

    public void setCapturedByUserId(int capturedByUserId) {
        this.capturedByUserId = capturedByUserId;
    }
}