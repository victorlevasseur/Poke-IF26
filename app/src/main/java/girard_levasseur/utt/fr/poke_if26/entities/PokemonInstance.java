package girard_levasseur.utt.fr.poke_if26.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

/**
 * Represents a spawn instance of a pokemon on the map that
 * may or may not have been captured.
 *
 * Created by victor on 05/12/17.
 */
@Entity(
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "captured_by_user_id"),
        indices = {
                @Index(value = {"lat_lng"}, unique = true)
        })
public class PokemonInstance {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "pokemon_id")
    private int pokemonId;

    @ColumnInfo(name = "lat_lng")
    private LatLng location;

    @ColumnInfo(name = "capturability")
    private float capturability;

    @ColumnInfo(name = "captured_by_user_id")
    private Integer capturedByUserId = null;

    public static PokemonInstance INVALID = new PokemonInstance();

    public PokemonInstance() {

    }

    @Ignore
    public PokemonInstance(int pokemonId, LatLng location, float capturability) {
        this.pokemonId = pokemonId;
        this.location = location;
        this.capturability = capturability;
        this.capturedByUserId = null;
    }

    @Ignore
    public PokemonInstance(int pokemonId, LatLng location, float capturability, Integer capturedByUserId) {
        this.pokemonId = pokemonId;
        this.location = location;
        this.capturability = capturability;
        this.capturedByUserId = capturedByUserId;
    }

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

    public float getCapturability() {
        return capturability;
    }

    public void setCapturability(float capturability) {
        this.capturability = capturability;
    }

    public Integer getCapturedByUserId() {
        return capturedByUserId;
    }

    public void setCapturedByUserId(Integer capturedByUserId) {
        this.capturedByUserId = capturedByUserId;
    }
}
