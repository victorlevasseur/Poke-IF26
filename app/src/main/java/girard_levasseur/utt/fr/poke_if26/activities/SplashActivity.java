package girard_levasseur.utt.fr.poke_if26.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import girard_levasseur.utt.fr.poke_if26.services.PokemonLocationsInitializerService;
import io.reactivex.Single;

public class SplashActivity extends AppCompatActivity {

    @Inject
    public PokemonLocationsInitializerService pokemonLocationsInitializerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        pokemonLocationsInitializerService.initializeNewLocations()
                .subscribe((empty) -> {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                });
    }
}
