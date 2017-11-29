package girard_levasseur.utt.fr.poke_if26.activities;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;
import girard_levasseur.utt.fr.poke_if26.R;
import girard_levasseur.utt.fr.poke_if26.exceptions.GPSLocationNotAvailable;
import girard_levasseur.utt.fr.poke_if26.exceptions.ImpossibleActionException;
import girard_levasseur.utt.fr.poke_if26.fragments.ExploreMapFragment;
import girard_levasseur.utt.fr.poke_if26.services.GPSLocationService;
import girard_levasseur.utt.fr.poke_if26.services.LoginService;

public class MainActivity extends AppCompatActivity
        implements HasFragmentInjector,
        ExploreMapFragment.OnExploreMapFragmentInteractionListener,
        OnRequestPermissionsResultCallback {

    @Inject
    public DispatchingAndroidInjector<Fragment> fragmentAndroidInjector;

    @Inject
    public LoginService loginService;

    @Inject
    public GPSLocationService gpsLocationService;

    private TextView mTextMessage;
    private FrameLayout mContentFrame;

    private Boolean canGetUserLocation = null;

    private ExploreMapFragment exploreMapFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_map:
                // Create the map fragment
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, exploreMapFragment)
                        .commit();
                return true;
            case R.id.navigation_pokedex:
                // TODO: Create the Pokedex fragment
                return true;
            case R.id.navigation_account:
                // TODO: Create the account fragment
                return true;
        }
        return false;
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                try {
                    loginService.logout();
                    startActivity(new Intent(this, LoginActivity.class));
                } catch (ImpossibleActionException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return fragmentAndroidInjector;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                canGetUserLocation = true;
                exploreMapFragment.displayAuthorizationPopup(false);
                startLocationUpdate();
            } else {
                canGetUserLocation = false;
                exploreMapFragment.displayAuthorizationPopup(true);
                // TODO: Not able to find pokemons.
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exploreMapFragment = new ExploreMapFragment();

        mTextMessage = findViewById(R.id.message);
        mContentFrame = findViewById(R.id.content_frame);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            canGetUserLocation = null;
            exploreMapFragment.displayAuthorizationPopup(true);
            // Will wait for the permissions to be accepted until going to the map fragment.
        } else {
            canGetUserLocation = true;
            exploreMapFragment.displayAuthorizationPopup(false);
            startLocationUpdate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdate();
    }

    @Override
    public void onPause() {
        stopLocationUpdate();
        super.onPause();
    }

    @Override
    public void onStop() {
        stopLocationUpdate();
        super.onStop();
    }

    @Override
    public void onAuthorizeButtonClicked() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 1);
    }

    private void startLocationUpdate() {
        try {
            gpsLocationService.enableLocationUpdates();
        } catch (GPSLocationNotAvailable gpsLocationNotAvailable) {
            gpsLocationNotAvailable.printStackTrace();
            // TODO: Display a label
        }
    }

    private void stopLocationUpdate() {
        gpsLocationService.disableLocationUpdates();
    }

}
