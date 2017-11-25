package girard_levasseur.utt.fr.poke_if26.entities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Represents an user.
 *
 * Created by victor on 23/11/17.
 */
@Table(name = "Users")
public class User extends Model {

    @Column()
    public String username;

    @Column()
    public String passwordHash;

    /*
     * Example of relationship with one-to-many
	public List<Item> items() {
		return getMany(Item.class, "Category");
	}
     */

    public User() {
        super();
    }

    // TODO: Redo ctor
    public User(String username, String passwordHash) {
        super();
        this.username = username;
        this.passwordHash = passwordHash;
    }

}
