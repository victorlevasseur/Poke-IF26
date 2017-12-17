package girard_levasseur.utt.fr.poke_if26.dto;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import me.sargunvohra.lib.pokekotlin.model.Pokemon;

/**
 * A PokemonInstance with the Pokemon fetched from the PokeAPI.
 *
 * Created by victor on 05/12/17.
 */
public class FetchedPokemonInstance {

    private int id;

    private Pokemon pokemon;

    private Bitmap pokemonImage;

    private LatLng location;

    private float capturability;

    private Integer capturedByUserId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
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

    public Bitmap getPokemonImage() {
        return pokemonImage;
    }

    public void setPokemonImage(Bitmap pokemonImage) {
        this.pokemonImage = pokemonImage;
    }

    private FetchedPokemonInstance(
            int id, Pokemon pokemon, LatLng location, float capturability, Integer capturedByUserId, Bitmap pokemonImage) {
        this.id = id;
        this.pokemon = pokemon;
        this.location = location;
        this.capturability = capturability;
        this.capturedByUserId = capturedByUserId;
        this.pokemonImage = pokemonImage;
    }

    static public class Builder {
        private int id;
        private Pokemon pokemon;
        private LatLng location;
        private float capturability;
        private Integer capturedByUserId = null;
        private Bitmap pokemonBitmap = null;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setPokemon(Pokemon pokemon) {
            this.pokemon = pokemon;
            return this;
        }

        public Builder setLocation(LatLng location) {
            this.location = location;
            return this;
        }

        public Builder setCapturedByUserId(Integer capturedByUserId) {
            this.capturedByUserId = capturedByUserId;
            return this;
        }

        public Builder setPokemonImage(Bitmap image) {
            this.pokemonBitmap = image;
            return this;
        }

        public Builder setCapturability(float capturability) {
            this.capturability = capturability;
            return this;
        }

        public FetchedPokemonInstance build() {
            return new FetchedPokemonInstance(
                    id, pokemon, location, capturability, capturedByUserId, pokemonBitmap);
        }
    }
}
