package girard_levasseur.utt.fr.poke_if26.services.impl;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.apache.commons.lang3.ArrayUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import girard_levasseur.utt.fr.poke_if26.activities.MainActivity;
import girard_levasseur.utt.fr.poke_if26.di.PerActivityScope;
import girard_levasseur.utt.fr.poke_if26.exceptions.GPSLocationNotAvailable;
import girard_levasseur.utt.fr.poke_if26.services.GPSLocationService;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by victor on 29/11/17.
 */
@PerActivityScope
public class GPSLocationServiceImpl implements GPSLocationService {

    private Context context;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private SensorManager sensorManager;

    private LocationCallback locationCallback;

    private SensorEventListener sensorEventListener;

    private BehaviorSubject<Location> locationSubject;

    private BehaviorSubject<Float[]> accelerometerValuesSubject;

    private BehaviorSubject<Float[]> magneticFieldValuesSubject;

    private Observable<Float> azimutObservable;

    private boolean updatesEnabled;

    @Inject
    public GPSLocationServiceImpl(MainActivity activity) {
        context = activity;
        locationSubject = BehaviorSubject.create();
        accelerometerValuesSubject = BehaviorSubject.create();
        magneticFieldValuesSubject = BehaviorSubject.create();
        azimutObservable = Observable.combineLatest(
                accelerometerValuesSubject
                        .sample(Observable.interval(500, TimeUnit.MILLISECONDS)),
                magneticFieldValuesSubject
                        .sample(Observable.interval(500, TimeUnit.MILLISECONDS)),
                (Float[] accelerometerValues, Float[] magneticFieldValues) -> {
                    float[] rotationMatrix = new float[9];
                    boolean success = SensorManager.getRotationMatrix(
                            rotationMatrix,
                            null,
                            ArrayUtils.toPrimitive(accelerometerValues),
                            ArrayUtils.toPrimitive(magneticFieldValues));
                    if (!success) {
                        // Can't compute the rotation matrix, output a null value
                        // that will be filtered in the next step.
                        return null;
                    }

                    float[] result = new float[3];
                    SensorManager.getOrientation(
                            rotationMatrix,
                            result);

                    return Float.valueOf(result[0]);
                })
                .filter((azimut) -> azimut != null);

        // Initialize the location provider and its callback.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult.getLastLocation() != null &&
                        locationResult.getLastLocation().getAccuracy() < 10) {
                    locationSubject.onNext(locationResult.getLastLocation());
                }
            }
        };

        // Initialize the sensor event listener
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    accelerometerValuesSubject.onNext(ArrayUtils.toObject(event.values));
                } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    magneticFieldValuesSubject.onNext(ArrayUtils.toObject(event.values));
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager =
                (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
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
                        if (location != null && location.getAccuracy() < 10) {
                            Log.i(toString(), "Initial position received.");
                            locationSubject.onNext(location);
                        }
                    });


            Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            Sensor magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

            if (accelerometerSensor != null || magneticFieldSensor != null) {
                sensorManager.registerListener(
                        sensorEventListener,
                        accelerometerSensor,
                        SensorManager.SENSOR_DELAY_NORMAL);
                sensorManager.registerListener(
                        sensorEventListener,
                        magneticFieldSensor,
                        SensorManager.SENSOR_DELAY_NORMAL);
            }

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

        // Stop GPS updates.
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

        // Stop orientation updates.
        if (sensorEventListener != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }

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

    @Override
    public Observable<Float> getAzimutUpdates() {
        return azimutObservable;
    }
}
