package girard_levasseur.utt.fr.poke_if26.services;

import android.location.Location;

import girard_levasseur.utt.fr.poke_if26.exceptions.GPSLocationNotAvailable;
import io.reactivex.Observable;

/**
 * Created by victor on 29/11/17.
 *
 * Note: Only usable in the MainActivity and its child fragments.
 */
public interface GPSLocationService {
    void enableLocationUpdates() throws GPSLocationNotAvailable;
    void disableLocationUpdates();
    Location getLastLocation();
    Observable<Location> getLocationUpdates();
    Observable<Float> getAzimutUpdates();
}
