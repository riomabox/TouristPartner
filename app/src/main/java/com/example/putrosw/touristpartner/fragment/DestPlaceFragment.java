package com.example.putrosw.touristpartner.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.putrosw.touristpartner.R;
import com.eyro.mesosfer.FindCallback;
import com.eyro.mesosfer.GetCallback;
import com.eyro.mesosfer.MesosferData;
import com.eyro.mesosfer.MesosferException;
import com.eyro.mesosfer.MesosferObject;
import com.eyro.mesosfer.MesosferQuery;
import com.eyro.mesosfer.MesosferUser;
import com.eyro.mesosfer.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DestPlaceFragment extends AppCompatActivity {

    AutoCompleteTextView atvPlaces;
    PlacesTask placesTask;
    ParserTask parserTask;
    ImageButton searchDest;

    private ProgressDialog loading;
    private AlertDialog dialog;
    private MesosferData data;
    String nickname;

    private ListView listview;
    //private SimpleAdapter adapter;
    private ArrayAdapter<String> adapter;
    private List<MesosferData> listData;

    private final List<Map<String, String>> mapDataList = new ArrayList<>();
    private List<String> searchUser = new ArrayList<>();
    //private static final int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
    //private static final String[] from = new String[] { "id", "data" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dest_place);
        getSupportActionBar().setTitle("Pencarian Destinasi");

        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
        atvPlaces.setThreshold(1);

        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchUser);
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);

        loading = new ProgressDialog(this);
        loading.setIndeterminate(true);
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);

        updateAndShowDataList();

        searchDest = (ImageButton)findViewById(R.id.btnSearch);
        searchDest.setOnClickListener(operation);
    }

    View.OnClickListener operation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnSearch:sembunyikanKeyBoard(v);
                    handleSearchDest();break;
            }
        }
    };

    public void handleSearchDest() {

        loading.setMessage("Search Destination ...");
        loading.show();

        final String destination = atvPlaces.getText().toString();

        final MesosferUser user = MesosferUser.getCurrentUser();
        if (user != null) {
            user.fetchAsync(new GetCallback<MesosferUser>() {
                @Override
                public void done(MesosferUser mesosferUser, MesosferException e) {
                    // hide progress dialog loading

                    // check if there is an exception happen
                    if (e != null) {
                        // setup alert dialog builder
                        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                        builder.setNegativeButton(android.R.string.ok, null);
                        builder.setTitle("Error Happen");
                        builder.setMessage(
                                String.format(Locale.getDefault(), "Error code: %d\nDescription: %s",
                                        e.getCode(), e.getMessage())
                        );
                        return;
                    }
                    //updateView(user);
                    MesosferObject dat = user.getData();
                    if (dat != null) {
                        nickname = dat.optString("nickname");
                        System.out.println(nickname);
                        if (data == null) {
                            data = MesosferData.createData("LastPosition");
                        }
                        // set data
                        System.out.println(nickname);
                        data.setData("user", nickname);
                        data.setData("posisi", destination);
                        data.setData("timestamp", new Date());
                        // execute save data
                        data.saveAsync(new SaveCallback() {
                            @Override
                            public void done(MesosferException e) {
                                // hide progress dialog loading
                                loading.dismiss();

                                AlertDialog.Builder builder = new AlertDialog.Builder(DestPlaceFragment.this);
                                builder.setNegativeButton(android.R.string.ok, null);

                                // check if there is an exception happen
                                if (e != null) {
                                    builder.setTitle("Error Happen");
                                    builder.setMessage(
                                            String.format(Locale.getDefault(), "Error code: %d\nDescription: %s",
                                                    e.getCode(), e.getMessage())
                                    );
                                    dialog = builder.show();
                                    return;
                                }

                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        });

                    }
                }
            });
        }

        Intent intent = new Intent(getBaseContext(), ViewDestFragment.class);
        intent.putExtra("destination",destination);
        System.out.println(destination);
        startActivity(intent);
    }

    private void sembunyikanKeyBoard(View v){
        InputMethodManager a = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        a.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    private void updateAndShowDataList() {
        MesosferQuery<MesosferData> query = MesosferData.getQuery("LastPosition");

        // showing a progress dialog loading
        loading.setMessage("Load Data...");
        loading.show();

        query.findAsync(new FindCallback<MesosferData>() {
            @Override
            public void done(List<MesosferData> list, MesosferException e) {
                // hide progress dialog loading
                loading.dismiss();

                // check if there is an exception happen
                if (e != null) {
                    // setup alert dialog builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(DestPlaceFragment.this);
                    builder.setNegativeButton(android.R.string.ok, null);
                    builder.setTitle("Error Happen");
                    builder.setMessage(
                            String.format(Locale.getDefault(), "Error code: %d\nDescription: %s",
                                    e.getCode(), e.getMessage())
                    );
                    dialog = builder.show();
                    return;
                }

                // clear all data list
                mapDataList.clear();
                for (MesosferData data : list) {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", "ID : " + data.getObjectId());
                    try {
                        map.put("data", data.toJSON().toString(4));
                    } catch (JSONException e1) {
                        map.put("data", data.toJSON().toString());
                    }

                    mapDataList.add(map);
                    String json = map.get("data");
                    System.out.println(json);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String temp = jsonObject.getString("posisi");
                        System.out.println(temp);
                        searchUser.add(temp);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    /*try {
                        String temp = jsonObject.getJSONObject("data").getString("posisi").toString();
                        System.out.println(temp);
                        searchUser.add(temp);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }*/
                    //String temp = map.get("data");
                    //searchUser.add(temp);
                }
                Collections.reverse(searchUser);
                adapter.notifyDataSetChanged();
            }
        });
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyDer2cJKSmgzXgdQKz9JYcueZNXmO41Nps";

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }


            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from web service in background
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }


    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            atvPlaces.setAdapter(adapter);
        }
    }
}
