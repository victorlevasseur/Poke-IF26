package girard_levasseur.utt.fr.poke_if26.activities;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import girard_levasseur.utt.fr.poke_if26.R;
import girard_levasseur.utt.fr.poke_if26.services.LoginService;
import girard_levasseur.utt.fr.poke_if26.services.PokemonsService;
import girard_levasseur.utt.fr.poke_if26.services.UserService;
import io.reactivex.disposables.Disposable;

public class EncounterActivity extends AppCompatActivity {

    public static final String POKEMON_INSTANCE_ID = "pokemon_id";

    @Inject
    public PokemonsService pokemonsService;

    @Inject
    public LoginService loginService;

    private Disposable fetchPokemonDisposable;

    private ImageView imageView;

    private TextView pokemonNameTextView;

    private Button captureButton;

    private ProgressBar loadingProgressBar;

    private int pokemonInstanceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encounter);

        imageView = findViewById(R.id.imageView);
        pokemonNameTextView = findViewById(R.id.pokemonNameLabel);
        captureButton = findViewById(R.id.captureButton);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        pokemonInstanceId = getIntent().getIntExtra(POKEMON_INSTANCE_ID, -1);
        if (pokemonInstanceId == -1) {
            throw new IllegalArgumentException(
                    "Can't launch EncounterActivity without a POKEMON_INSTANCE_ID extra in the intent bundle!");
        }
        fetchPokemonDisposable = pokemonsService.getFetchedPokemonInstanceById(pokemonInstanceId)
                .subscribe((pokemon) -> {
                    if (pokemon.isPresent()) {
                        imageView.setImageBitmap(pokemon.get().getPokemonImage());
                        imageView.setVisibility(View.VISIBLE);
                        pokemonNameTextView.setText(pokemon.get().getPokemon().getName());
                        captureButton.setVisibility(View.VISIBLE);
                    } else {
                        pokemonNameTextView.setText(R.string.unavailable_pokemon_label);
                    }

                    pokemonNameTextView.setVisibility(View.VISIBLE);
                    loadingProgressBar.setVisibility(View.GONE);
                });

        captureButton.setOnClickListener((view) -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            captureButton.setEnabled(false);
            pokemonsService.capturePokemonById(pokemonInstanceId, loginService.getConnectedUser())
                    .subscribe((result) -> {
                        // TODO: Implement result depend callback
                        if (result) {
                            new AlertDialog.Builder(this)
                                    .setTitle(R.string.capture_success_title)
                                    .setMessage(R.string.capture_success_label)
                                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                        dialog.dismiss();
                                        startActivity(new Intent(this, MainActivity.class));
                                    })
                                    .create()
                                    .show();
                        } else {
                            loadingProgressBar.setVisibility(View.GONE);
                            captureButton.setEnabled(true);
                            new AlertDialog.Builder(this)
                                    .setTitle(R.string.capture_failed_title)
                                    .setMessage(R.string.capture_failed_label)
                                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                        dialog.dismiss();
                                    })
                                    .create()
                                    .show();
                        }
                    });
        });
    }

    @Override
    protected void onStop() {
        fetchPokemonDisposable.dispose();
        super.onStop();
    }
}
