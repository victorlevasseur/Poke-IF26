package girard_levasseur.utt.fr.poke_if26.activities.login;

import dagger.Subcomponent;

/**
 * Declare modules used only at the LoginActivity level.
 */
@Subcomponent
public interface LoginActivityComponent {

    @Subcomponent.Builder
    interface Builder {
        Builder loginModule(LoginActivityModule module);
        LoginActivityComponent build();
    }

}
