package girard_levasseur.utt.fr.poke_if26.entities.converters;

import android.arch.persistence.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by victor on 05/12/17.
 */

public class LatLngConverters {

    @TypeConverter
    public static LatLng fromString(String string) throws IllegalArgumentException {
        if (string == null) {
            return null;
        }

        String[] parts = string.split(";");
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Can\'t parse \"" + string + "\" into a LatLng instance!");
        }

        try {
            double lat = Double.parseDouble(parts[0]);
            double lng = Double.parseDouble(parts[1]);

            return new LatLng(lat, lng);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Can\'t parse \"" + string + "\" into a LatLng instance!");
        }
    }

    @TypeConverter
    public static String latLngToString(LatLng latLng) {
        if (latLng == null) {
            return null;
        }

        return new StringBuilder()
                .append(latLng.latitude)
                .append(';')
                .append(latLng.longitude)
                .toString();
    }

}
