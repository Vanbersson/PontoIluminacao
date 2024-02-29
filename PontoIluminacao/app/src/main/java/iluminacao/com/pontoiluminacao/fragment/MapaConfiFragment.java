package iluminacao.com.pontoiluminacao.fragment;


import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import iluminacao.com.pontoiluminacao.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapaConfiFragment extends Fragment implements OnMapReadyCallback,LocationSource, GoogleMap.OnMapLongClickListener {

    private static MapaConfiFragment instancia;

    private OnFragmentInteractionListener mListener;

    private static final int DEFAULT_ZOOM = 16;
    private static final int DEFAULT_ZOOM1 = 20;

    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private Location mLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    private boolean gps_Ativo;

    public static MapaConfiFragment getInstancia(){
        if(instancia == null){
            instancia = new MapaConfiFragment();
        }

        return instancia;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.mapa_confi_fragment, container, false);

         mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapconfir);
        mapFragment.getMapAsync(this);

         return  view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );
        gps_Ativo = manager.isProviderEnabled( LocationManager.GPS_PROVIDER);

        if(gps_Ativo){
            mMap = googleMap;
            mUiSettings = mMap.getUiSettings();

            mUiSettings.setRotateGesturesEnabled(true);

            mMap.setOnMapLongClickListener(this);

            getLocation();
        }else {
            Toast.makeText(getActivity(), "GPS Desativado", Toast.LENGTH_SHORT).show();
        }

    }

    private void getLocation(){

        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity() ,new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mLocation = location;

                LatLng latLng  = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());

                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Posição capturada"));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(),mLocation.getLongitude()),
                        DEFAULT_ZOOM));

                onButtonPressed(mLocation);
            }
        });
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );
        gps_Ativo = manager.isProviderEnabled( LocationManager.GPS_PROVIDER);
        if(gps_Ativo){

            Location location = new Location("LongPressLocationProvider");
            location.setLatitude(latLng.latitude);
            location.setLongitude(latLng.longitude);
            location.setAccuracy(100);

            mLocation = location;

            mMap.clear();

            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Posição Informada"));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude),
                    DEFAULT_ZOOM1));

            onButtonPressed(mLocation);
        }else {
            Toast.makeText(getActivity(), "GPS Desativado", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void activate(OnLocationChangedListener listener) {

    }

    @Override
    public void deactivate() {

    }

    public void onButtonPressed(Location location) {
        if (mListener != null) {
            mListener.onFragmentInteraction(location);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Location location);
    }

}
