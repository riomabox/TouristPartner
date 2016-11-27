package com.example.putrosw.touristpartner.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.putrosw.touristpartner.PlaceDest;
import com.example.putrosw.touristpartner.PlaceMapActivity;
import com.example.putrosw.touristpartner.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by Putro SW on 26-Nov-16.
 */
public class ViewDestFragment extends AppCompatActivity implements android.location.LocationListener, OnMapReadyCallback {

    private GoogleMap mMap;
    Location location;
    String destinasi;
    Button btnShowOnMap;
    double latitude, longitude, lintang, bujur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.activity_view_dest);
        Intent intent = getIntent();
        destinasi = intent.getStringExtra("destination");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(bestProvider);

        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

        LinearLayout restaurant = (LinearLayout)findViewById(R.id.laysearch_restaurant);
        LinearLayout spbu = (LinearLayout)findViewById(R.id.laysearch_spbu);
        LinearLayout hotel = (LinearLayout)findViewById(R.id.laysearch_hotel);
        LinearLayout hospital = (LinearLayout)findViewById(R.id.laysearch_hospital);
        LinearLayout terminal = (LinearLayout)findViewById(R.id.laysearch_bus);
        LinearLayout stasiun = (LinearLayout)findViewById(R.id.laysearch_train);
        LinearLayout semua = (LinearLayout)findViewById(R.id.laysearch_all);
        btnShowOnMap = (Button) findViewById(R.id.btnShowOnMap);

        btnShowOnMap.setOnClickListener(operation);
        restaurant.setOnClickListener(operation);
        spbu.setOnClickListener(operation);
        hotel.setOnClickListener(operation);
        hospital.setOnClickListener(operation);
        terminal.setOnClickListener(operation);
        stasiun.setOnClickListener(operation);
        semua.setOnClickListener(operation);
        /*Button go = (Button) findViewById(R.id.btnGo);
        go.setOnClickListener(op);

        Button cari = (Button) findViewById(R.id.btnCari);
        cari.setOnClickListener(op);*/
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

    View.OnClickListener operation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnShowOnMap:sembunyikanKeyBoard(v);
                    handleMap();break;
                case R.id.laysearch_restaurant:sembunyikanKeyBoard(v);
                    handleRestaurant();break;
                case R.id.laysearch_spbu:sembunyikanKeyBoard(v);
                    handleSpbu();break;
                case R.id.laysearch_hotel:sembunyikanKeyBoard(v);
                    handleHotel();break;
                case R.id.laysearch_hospital:sembunyikanKeyBoard(v);
                    handleHospital();break;
                case R.id.laysearch_bus:sembunyikanKeyBoard(v);
                    handleTerminal();break;
                case R.id.laysearch_train:sembunyikanKeyBoard(v);
                    handleStasiun();break;
                case R.id.laysearch_all:sembunyikanKeyBoard(v);
                    handleSemua();break;
            }
        }
    };

    /** Button click event for shown on map */
        /*btnShowOnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(),
                        PlacesMapActivity.class);
                // Sending user current geo location
                i.putExtra("user_latitude", Double.toString(gps.getLatitude()));
                i.putExtra("user_longitude", Double.toString(gps.getLongitude()));

                // passing near places to map activity
                i.putExtra("near_places", nearPlaces);
                // staring activity
                startActivity(i);
            }
        });*/
    public void handleMap() {
        Intent i = new Intent(getBaseContext(), PlaceMapActivity.class);
        // Sending user current geo location
        String item = "food|gas_station|lodging|hospital|bus_station|train_station|zoo|park|museum|art_gallery";
        i.putExtra("lat", lintang);
        i.putExtra("lng", bujur);
        i.putExtra("item", item);

        // passing near places to map activity
        //i.putExtra("near_places", nearPlaces);
        // staring activity
        startActivity(i);
    }

    public void handleRestaurant() {
        Intent intent = new Intent(getBaseContext(), PlaceDest.class);
        String item = "food";
        intent.putExtra("item",item);
        intent.putExtra("lat",lintang);
        intent.putExtra("lng",bujur);

        System.out.println(item);
        startActivity(intent);
    }

    public void handleSpbu() {
        Intent intent = new Intent(getBaseContext(), PlaceDest.class);
        String item = "gas_station";
        intent.putExtra("item",item);
        intent.putExtra("lat",lintang);
        intent.putExtra("lng",bujur);
        System.out.println(item);
        startActivity(intent);
    }

    public void handleHotel() {
        Intent intent = new Intent(getBaseContext(), PlaceDest.class);
        String item = "lodging";
        intent.putExtra("item",item);
        intent.putExtra("lat",lintang);
        intent.putExtra("lng",bujur);
        System.out.println(item);
        startActivity(intent);
    }

    public void handleHospital() {
        Intent intent = new Intent(getBaseContext(), PlaceDest.class);
        String item = "hospital";
        intent.putExtra("item",item);
        intent.putExtra("lat",lintang);
        intent.putExtra("lng",bujur);
        System.out.println(item);
        startActivity(intent);
    }

    public void handleTerminal() {
        Intent intent = new Intent(getBaseContext(), PlaceDest.class);
        String item = "bus_station";
        intent.putExtra("item",item);
        intent.putExtra("lat",lintang);
        intent.putExtra("lng",bujur);
        System.out.println(item);
        startActivity(intent);
    }

    public void handleStasiun() {
        Intent intent = new Intent(getBaseContext(), PlaceDest.class);
        String item = "train_station";
        intent.putExtra("item",item);
        intent.putExtra("lat",lintang);
        intent.putExtra("lng",bujur);
        System.out.println(item);
        startActivity(intent);
    }

    public void handleSemua() {
        Intent intent = new Intent(getBaseContext(), PlaceDest.class);
        //String item = "food|gas_station|lodging|hospital|bus_station|train_station";
        String item = "zoo|park|museum|art_gallery";
        intent.putExtra("item",item);
        intent.putExtra("lat",lintang);
        intent.putExtra("lng",bujur);
        System.out.println(item);
        startActivity(intent);
    }

    private void sembunyikanKeyBoard(View v){
        InputMethodManager a = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        a.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (location != null) {
            onLocationChanged(location);
        }
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera

        goCari();;

        /*LatLng dest = new LatLng(-7.2819705, 112.795323);

        mMap.addMarker(new MarkerOptions().position(ITS).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ITS,15));*/
    }
    private void goCari() {
        Geocoder g = new Geocoder(getBaseContext());
        try {
            List<Address> daftar = g.getFromLocationName(destinasi.toString(),1);
            Address alamat = daftar.get(0);
            String nemuAlamat =  alamat.getAddressLine(0);
            lintang = alamat.getLatitude();
            bujur = alamat.getLongitude();

            Toast.makeText(getBaseContext(),"Ketemu " + nemuAlamat,Toast.LENGTH_LONG).show();

            //Float dblzoom = Float.parseFloat(zoom.getText().toString());

            gotoPeta(lintang, bujur, 15);
            hitungJarak(latitude, longitude, lintang, bujur);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void gotoPeta(Double lat, Double lng, float z){
        LatLng Lokasibaru = new LatLng(lat,lng);
        mMap.addMarker(new MarkerOptions().position(Lokasibaru).title("Marker in " + lat + ":" + lng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Lokasibaru,z));
    }

    private void hitungJarak(double latAsal, double lngAsal, double latTujuan, double lngTujuan){
        Location asal = new Location("asal");
        Location tujuan = new Location("tujuan");
        tujuan.setLatitude(latTujuan);
        tujuan.setLongitude(lngTujuan);
        asal.setLatitude(latAsal);
        asal.setLongitude(lngAsal);
        float jarak = (float) asal.distanceTo(tujuan)/1000;
        String jaraknya = String.valueOf(jarak);
        Toast.makeText(getBaseContext(), "jarak : " + jaraknya +" km ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        /*LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
