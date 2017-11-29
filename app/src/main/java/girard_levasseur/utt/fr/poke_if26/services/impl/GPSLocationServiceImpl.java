package girard_levasseur.utt.fr.poke_if26.services.impl;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import girard_levasseur.utt.fr.poke_if26.PokeIF26App;
import girard_levasseur.utt.fr.poke_if26.activities.MainActivity;
import girard_levasseur.utt.fr.poke_if26.di.PerActivityScope;
import girard_levasseur.utt.fr.poke_if26.exceptions.GPSLocationNotAvailable;
import girard_levasseur.utt.fr.poke_if26.services.GPSLocationService;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by victor on 29/11/17.
 */
@PerActivityScope
public class GPSLocationServiceImpl implements GPSLocationService {

    private Context context;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private LocationCallback locationCallback;

    private BehaviorSubject<Location> locationSubject;

    private boolean updatesEnabled;

    @Inject
    public GPSLocationServiceImpl(MainActivity activity) {
        context = activity;
        locationSubject = BehaviorSubject.create();

        // Initialize the location provider and its callback.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult.getLastLocation() != null &&
                        locationResult.getLastLocation().getAccuracy() < 25) {
                    locationSubject.onNext(locationResult.getLastLocation());
                }
            }
        };
    }

    @Override
    public void enableLocationUpdates() throws GPSLocationNotAvailable {
        if (updatesEnabled) {
            return;
        }

        Log.i(GPSLocationServiceImpl.class.getCanonicalName(), "Location updates started.");
        LocationRequest locationRequest = new LocationRequest();

        locationRequest.setMaxWaitTime(100);
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(100);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient
                    .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener((location) -> {
                        if (location != null && location.getAccuracy() > 25) {
                            Log.i(toString(), "Initial position received.");
                            locationSubject.onNext(location);
                        }
                    });

            updatesEnabled = true;
        } else {
            updatesEnabled = false;
            throw new GPSLocationNotAvailable("Application not authorized to get the user location!");
        }
    }

    @Override
    public void disableLocationUpdates() {
        if (!updatesEnabled) {
            return;
        }

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        Log.i(toString(), "Location updates stopped.");
        updatesEnabled = false;
    }

    @Override
    public Location getLastLocation() {
        return locationSubject.getValue();
    }

    @Override
    public Observable<Location> getLocationUpdates() {
        return locationSubject;
    }
}
