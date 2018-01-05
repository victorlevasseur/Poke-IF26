package girard_levasseur.utt.fr.poke_if26.exceptions;

import girard_levasseur.utt.fr.poke_if26.R;
import girard_levasseur.utt.fr.poke_if26.fragments.ChangeLoginDialogFragment;

/**
 * Exception thrown when there's a conflict with an existing username.
 *
 * Created by victor on 26/11/17.
 */
public class AlreadyExistingUsernameException extends Exception
        implements ChangeLoginDialogFragment.LoginListener.LoginChangeDialogError {

    public AlreadyExistingUsernameException(String msg) {
        super(msg);
    }

    @Override
    public int getLoginChangeDialogErrorStringId() {
        return R.string.change_login_already_exist_error;
    }
}
