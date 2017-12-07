package girard_levasseur.utt.fr.poke_if26.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dagger.android.AndroidInjection;
import girard_levasseur.utt.fr.poke_if26.R;

public class EncounterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encounter);
    }
}
