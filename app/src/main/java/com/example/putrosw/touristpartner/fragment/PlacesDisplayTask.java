package com.example.putrosw.touristpartner.fragment;

/**
 * Created by Putro SW on 25-Nov-16.
 */
import android.os.AsyncTask;
import android.util.Log;

import com.example.putrosw.touristpartner.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

    JSONObject googlePlacesJson;
    GoogleMap googleMap;

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

        List<HashMap<String, String>> googlePlacesList = null;
        Places placeJsonParser = new Places();

        try {
            googleMap = (GoogleMap) inputObj[0];
            googlePlacesJson = new JSONObject((String) inputObj[1]);
            System.out.println(googlePlacesJson);
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return googlePlacesList;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
        googleMap.clear();
        for (int i = 0; i < list.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = list.get(i);
            System.out.println(list.get(i));
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            for (int j=1;j<=googlePlace.size()-5;j++){
                //listtypes.add(types.get(i).toString());
                System.out.println(googlePlace.get("type"+j));
                if(googlePlace.get("type"+j).toString().equals("food")){
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.fastfood));
                }
                else if(googlePlace.get("type"+j).toString().equals("gas_station")){
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.fillingstation));
                }
                else if(googlePlace.get("type"+j).toString().equals("lodging")){
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.apartment));
                }
                else if(googlePlace.get("type"+j).toString().equals("hospital")){
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital));
                }
                else if(googlePlace.get("type"+j).toString().equals("bus_station")){
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus));
                }
                else if(googlePlace.get("type"+j).toString().equals("train_station")){
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.train));
                }
            }
            googleMap.addMarker(markerOptions);
        }
    }
}