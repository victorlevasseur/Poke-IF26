package girard_levasseur.utt.fr.poke_if26.fragments.dummy;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import me.sargunvohra.lib.pokekotlin.model.NamedApiResource;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;
import me.sargunvohra.lib.pokekotlin.model.PokemonAbility;
import me.sargunvohra.lib.pokekotlin.model.PokemonHeldItem;
import me.sargunvohra.lib.pokekotlin.model.PokemonMove;
import me.sargunvohra.lib.pokekotlin.model.PokemonSpecies;
import me.sargunvohra.lib.pokekotlin.model.PokemonSprites;
import me.sargunvohra.lib.pokekotlin.model.PokemonStat;
import me.sargunvohra.lib.pokekotlin.model.PokemonType;
import me.sargunvohra.lib.pokekotlin.model.VersionGameIndex;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Pokemon> ITEMS = new ArrayList<Pokemon>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Pokemon> ITEM_MAP = new HashMap<String, Pokemon>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createItem(i));
        }
    }

    private static void addItem(Pokemon item) {
        ITEMS.add(item);
        String id = (new Integer(item.getId())).toString();
        ITEM_MAP.put(id, item);
    }

    private static Pokemon createItem(int position) {
        NamedApiResource species = new NamedApiResource("Pikachu", "Species", 1);
        PokemonSprites sprites = new PokemonSprites(null,null,null,
                null, null, null, null, null);
        return new Pokemon(position,
                "Pikachu",
                0,
                0,
                false,
                0,
                0,
                species,
                new ArrayList<PokemonAbility>(),
                new ArrayList<NamedApiResource>(),
                new ArrayList<VersionGameIndex>(),
                new ArrayList<PokemonHeldItem>(),
                new ArrayList<PokemonMove>(),
                new ArrayList<PokemonStat>(),
                new ArrayList<PokemonType>(),
                sprites
        );
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}
