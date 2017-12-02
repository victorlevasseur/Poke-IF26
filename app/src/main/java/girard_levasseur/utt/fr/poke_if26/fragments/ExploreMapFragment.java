package girard_levasseur.utt.fr.poke_if26.fragments;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import girard_levasseur.utt.fr.poke_if26.R;
import girard_levasseur.utt.fr.poke_if26.services.GPSLocationService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * The fragment displaying the map focused on the user
 * location.
 */
public class ExploreMapFragment extends Fragment {

    @Inject
    public GPSLocationService gpsLocationService;

    private OnExploreMapFragmentInteractionListener mListener;

    private GoogleMap googleMap;

    private MapFragment mapFragment;

    private Marker currLocationMarker = null;

    private Disposable positionUpdateDisposable = null;

    private Disposable cameraUpdateDisposable = null;

    private ConstraintLayout tooltipConstraintLayout;

    private boolean displayTooltip = false;

    public ExploreMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_explore_map, container, false);
        mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync((GoogleMap map) -> {
            googleMap = map;
            setupGoogleMap();
        });

        tooltipConstraintLayout = v.findViewById(R.id.info_popup_layout);
        displayAuthorizationPopup(displayTooltip);

        Button tooltipAuthorizeButton = v.findViewById(R.id.info_popup_button);
        tooltipAuthorizeButton.setOnClickListener((view) -> mListener.onAuthorizeButtonClicked());

        return v;
    }

    @Override
    public void onAttach(Context context) {
        AndroidInjection.inject(this);
        super.onAttach(context);
        if (context instanceof OnExploreMapFragmentInteractionListener) {
            mListener = (OnExploreMapFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        this.positionUpdateDisposable =
                gpsLocationService.getLocationUpdates().subscribe(this::updateUserPosition);

        this.cameraUpdateDisposable =
                // Combine the updates from the gps position and the azimut changes
                Observable.combineLatest(
                        gpsLocationService.getLocationUpdates(),
                        // Only consider a new value if the azimut has changed by pi/16 at least.
                        gpsLocationService.getAzimutUpdates()
                                .distinctUntilChanged((Float first, Float second) ->
                                        Math.abs(first.floatValue() - second.floatValue()) > (float)(Math.PI / 16)),
                        (location, azimut) -> Pair.create(location, azimut))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((pair) -> {
                            updateCameraPosition(pair.first, pair.second.floatValue());
                        });
    }

    public void onDetach() {
        if (this.cameraUpdateDisposable != null) {
            this.cameraUpdateDisposable.dispose();
        }
        if (this.positionUpdateDisposable != null) {
            this.positionUpdateDisposable.dispose();
        }

        super.onDetach();
    }

    public void updateCameraPosition(Location location, float azimut) {
        if (googleMap != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            googleMap.moveCamera(
                    CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(latLng)
                                    .bearing(azimut * 360 / (2 * (float)Math.PI))
                                    .tilt(45)
                                    .zoom(18)
                                    .build()
                    ));
        }
    }

    public void updateUserPosition(Location location) {
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }

        if (googleMap != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Votre position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = googleMap.addMarker(markerOptions);
        }
    }

    public void displayAuthorizationPopup(boolean display) {
        if (tooltipConstraintLayout != null) {
            tooltipConstraintLayout.setVisibility(display ? View.VISIBLE : View.GONE);
        }
        displayTooltip = display;
    }

    private void setupGoogleMap() {
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnExploreMapFragmentInteractionListener {

        void onAuthorizeButtonClicked();

    }
}
