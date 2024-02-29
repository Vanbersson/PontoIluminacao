package iluminacao.com.pontoiluminacao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import iluminacao.com.pontoiluminacao.objetos.Usuario;
import iluminacao.com.pontoiluminacao.sqlite.UsuarioSQLite;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ActivityMapa extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMyLocationButtonClickListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    private static final int DEFAULT_ZOOM = 16;

    private  boolean gps_Ativo;

    private GoogleMap mMap;
    private UiSettings mUiSettings;

    private FusedLocationProviderClient mFusedLocationClient;

    private Usuario mUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mUsuario = (Usuario) getIntent().getSerializableExtra(LoginActivity.EXTRA_USUARIO);


       Button btn_exit = findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sairLogin();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPonto();
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        gps_Ativo = manager.isProviderEnabled( LocationManager.GPS_PROVIDER);

        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        getPermission();

        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);

        if(!gps_Ativo){
            Toast.makeText(this, "GPS Desativado", Toast.LENGTH_SHORT).show();
        }

    }

    private void getPermission(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Nova solicitação
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED){
                mMap.setMyLocationEnabled(true);
            }

        }else{
            mMap.setMyLocationEnabled(true);

        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permissão foi concedida

                    getPermission();
                }else {
                    // permissão foi negada

                }
                return;
            }


        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void sairLogin(){

        UsuarioSQLite usuarioSQLite = new UsuarioSQLite(this);
        usuarioSQLite.excluir();

        Intent it = new Intent(this,LoginActivity.class);
        startActivity(it);
        finishAffinity();

    }

    private void startPonto(){
        Intent it = new Intent(this,PontoIluminacaoActivity.class);
        startActivity(it);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                        location.getLatitude(),
                        location.getLongitude()),
                        DEFAULT_ZOOM));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
