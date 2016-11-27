package com.example.putrosw.touristpartner;

/**
 * Created by Putro SW on 26-Nov-16.
 */
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.putrosw.touristpartner.R;
import com.example.putrosw.touristpartner.fragment.GooglePlacesReadTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class PlaceMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String GOOGLE_API_KEY = "AIzaSyDer2cJKSmgzXgdQKz9JYcueZNXmO41Nps";
    private GoogleMap mMap;
    EditText placeText;
    String type = null;
    double currentLat;
    double currentLng;
    private int PROXIMITY_RADIUS = 50000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.fragment_near_dest);
        Intent intent = getIntent();
        type = intent.getStringExtra("item");
        currentLat = intent.getDoubleExtra("lat", 0);
        currentLng = intent.getDoubleExtra("lng", 0);
        /*placeText = (EditText) findViewById(R.id.placeText);
        Button btnFind = (Button) findViewById(R.id.btnFind);*/
        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        fragment.getMapAsync(this);
        //googleMap.setMyLocationEnabled(true);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(currentLat, currentLng);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker Your Destination"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        searchDestination();
    }

    public void searchDestination() {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + currentLat + "," + currentLng);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=" + type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-7.2819705,112.795323&radius=5000&types=atm&key=AIzaSyDZ6kYnnN2XwN2QPPwQ8Q8dMNogPMIp8lA
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-7.2819705,112.79533&radius=5000&types=food&key=AIzaSyDZ6kYnnN2XwN2QPPwQ8Q8dMNogPMIp8lA
        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
        Object[] toPass = new Object[2];
        toPass[0] = mMap;
        toPass[1] = googlePlacesUrl.toString();
        googlePlacesReadTask.execute(toPass);
    }
}

