package com.example.googlemaplocationlab;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.example.googlemaplocationlab.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Marker currentMarker;

    private static final int LOCATION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Récupérer le fragment de la carte
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    // Cette méthode est appelée quand la carte est prête
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Marker initial exemple : Sydney
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        Toast.makeText(getApplicationContext(), "Map Ready", Toast.LENGTH_SHORT).show();

        // Démarrer la logique de localisation
        startLocationLogic();
    }

    // Vérifier permission puis écouter localisation
    private void startLocationLogic() {

        // Vérification permission runtime
        boolean permissionGranted =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        if (permissionGranted) {
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE
            );
        }
    }

    // Demander les mises à jour de position
    private void startLocationUpdates() {

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager == null) {
            return;
        }

        // Vérifier si le provider est activé
        boolean networkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        boolean gpsEnabled =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!networkEnabled && !gpsEnabled) {
            buildAlertMessageNoGps();
            return;
        }

        // Vérifier encore la permission avant requestLocationUpdates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Activer le bouton "ma position" sur la map
        mMap.setMyLocationEnabled(true);

        LocationListener listener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                Toast.makeText(
                        getApplicationContext(),
                        latitude + " : " + longitude,
                        Toast.LENGTH_SHORT
                ).show();

                // Déplacer un seul marker pour garder la carte propre
                LatLng pos = new LatLng(latitude, longitude);

                if (currentMarker == null) {
                    currentMarker = mMap.addMarker(new MarkerOptions()
                            .position(pos)
                            .title("Position actuelle"));
                } else {
                    currentMarker.setPosition(pos);
                }

                // Zoomer et centrer sur la nouvelle position
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // Méthode ancienne, gardée pour le lab
            }

            @Override
            public void onProviderEnabled(String provider) {
                // Provider activé
            }

            @Override
            public void onProviderDisabled(String provider) {
                // Si le provider est désactivé, proposer d'activer GPS/localisation
                buildAlertMessageNoGps();
            }
        };

        // NETWORK_PROVIDER : marche souvent en intérieur, moins précis que GPS
        if (networkEnabled) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    50,
                    listener
            );
        }

        // GPS_PROVIDER : plus précis, mais parfois plus lent
        if (gpsEnabled) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    50,
                    listener
            );
        }
    }

    // Boîte de dialogue si localisation/GPS désactivé
    private void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    // Résultat de la demande de permission
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Permission accordée", Toast.LENGTH_SHORT).show();

                // Relancer la logique après permission accordée
                startLocationLogic();

            } else {
                Toast.makeText(this, "Permission refusée", Toast.LENGTH_LONG).show();
            }
        }
    }
}
