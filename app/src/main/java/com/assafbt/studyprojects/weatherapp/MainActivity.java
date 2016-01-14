/*
 * *
 *  * Created by assafbt on 05/01/2016
 *
 */

package com.assafbt.studyprojects.weatherapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SimpleAdapter schedule;
    Spinner spinner;
    ArrayList<HashMap<String, String>> arrayList;
    ListView arrayView;
    ArrayAdapter arrayAdapter;
    ArrayAdapter adapter;
    private PermissionManager permissionManager;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location thisLocation;
    RequestQueue queue;
    Button getWeatherButton;
    String description, url;
    Location GPSlocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //arrayView = (ListView) findViewById(R.id.ArraylistViewrRight);
        arrayView = (ListView) findViewById(R.id.textView);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        // begin Array Adapter
        adapter = ArrayAdapter.createFromResource(this, R.array.myLocations, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);


        // end Array Adapter

        getWeatherButton = (Button) findViewById(R.id.getWeather);
        //ArrayList list = new ArrayList();

        queue = Volley.newRequestQueue(this);
        // url = "http://api.openweathermap.org/data/2.5/forecast?id=524901&appid=2de143494c0b295cca9337e1e96b00e0";


        final ProgressDialog dialog = new ProgressDialog(this);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            // go to Settings View
            public void onClick(final View view) {
                dialog.show();

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            JSONArray arrList = response.getJSONArray("list");
                            arrayList = new ArrayList<HashMap<String, String>>();
                            for (int i = 0; i < arrList.length(); i++) {
                                // get temp
                                JSONObject jObjList0 = arrList.getJSONObject(i);
                                JSONObject jObjMain = jObjList0.getJSONObject("main");
                                double temp = jObjMain.getDouble("temp");

                                //get time
                                // JSONObject objTime = jObjList0.getJSONObject("sys");
                                String time1 = jObjList0.getString("dt_txt");


                                // get icon
                                JSONArray arrDesc = jObjList0.getJSONArray("weather");
                                JSONObject objDec = arrDesc.getJSONObject(0);
                                description = objDec.getString("description");
                                String icon = objDec.getString("icon");

                                // get city name
                                JSONObject jObjCity = response.getJSONObject("city");
                                String city = jObjCity.getString("name");


                                // jokeString= jokeObj.toString();
                                dialog.cancel();

                                // String thisLocation = arrayView.toString();
                                //String myFormat = "on time " + time1 + " the weather at " + city + " is " + description + " with temp: " + temp + " with icon " + icon;

                                // adding myFormat to list

                                HashMap<String, String> hMap = new HashMap<String, String>();
                                hMap.put("time_date", time1);
                                hMap.put("temperature", String.valueOf(temp) + 'c');
                                hMap.put("descriptionView", description);
                                //String iconURL = "http://openweathermap.org/img/w/" + icon + ".png";
                                hMap.put("iconView", "http://openweathermap.org/img/w/" + icon + ".png");
                                arrayList.add(hMap);

                                schedule = new SimpleAdapter(getApplicationContext(), arrayList, R.layout.listview_row, new String[]{"time_date", "temperature", "descriptionView", "iconView"}, new int[]{R.id.time_date, R.id.temperature, R.id.descriptionView, R.id.iconView});


                            }//for
                            arrayView.setAdapter(schedule);

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(request);

            }


        });


    }//onCreate

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        showData(view);

    }

    private void showData (View view) {
        TextView myText = (TextView) view;
        String myLocation = (String) myText.getText();
        String checkLocation = "Current Location";

        Toast.makeText(this, "Weather in " + myText.getText(), Toast.LENGTH_SHORT).show();
        if (myLocation.equals(checkLocation)) {
            Log.e("equals: ", "true");

          /*  locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.e("onLocationChanged", "GPS location");
                    GPSlocation = new Location(location);
                    //GPSlocation = location;
                    int lon = (int) GPSlocation.getLongitude();
                    int lat = (int) GPSlocation.getLatitude();
                    Log.e("getLongitude", lon + "");
                    Log.e("getLatitude", lat + "");

                    url = "api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon;
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };*/
            double[] loc;
            loc = getGPS();
            Log.e("lat, lon == ", loc[0] + ", " + loc[1]);
            //http://api.openweathermap.org/data/2.5/forecast?lat=31.74706861   &lon=35.2160851  &appid=894f115787195ed5935778bee54ac0c5
            url = "http://api.openweathermap.org/data/2.5/forecast?lat=" + loc[0] + "&lon=" + loc[1]+"&units=metric&appid=894f115787195ed5935778bee54ac0c5";
            Log.e("url = ", url);



        }//if Current Location
        else {
            Log.e("onLocationChanged", myText.getText() + "");
            url = "http://api.openweathermap.org/data/2.5/forecast?q=" + myText.getText() + "&units=metric&appid=894f115787195ed5935778bee54ac0c5";
        }


        //myText.invalidate();

    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /*@Override
    protected void onStop() {
        super.onStop();
        try {
            locationManager.removeUpdates(locationListener);

        } catch (SecurityException se) {
            se.printStackTrace();
        }
    }*/

    private double[] getGPS() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

/* Loop over the array backwards, and if you get an accurate location, then break                 out the loop*/
        Location l = null;

        for (int i = providers.size() - 1; i >= 0; i--) {
            /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                double[] grantResults = {1};
                return grantResults;
            }*/
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        double[] gps = new double[2];
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        }
        return gps;
    }

}
