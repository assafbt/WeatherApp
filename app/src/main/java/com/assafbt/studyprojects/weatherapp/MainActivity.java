/*
 * *
 *  * Created by assafbt on 05/01/2016
 *
 */

package com.assafbt.studyprojects.weatherapp;

import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    //ListView arrayView;
    TextView arrayView;
    ArrayAdapter arrayAdapter;
    ArrayAdapter adapter;
    private PermissionManager permissionManager;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location thisLocation;
    RequestQueue queue;
    Button getWeatherButton;
    String description,url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //arrayView = (ListView) findViewById(R.id.ArraylistViewrRight);
        arrayView = (TextView) findViewById(R.id.textView);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        // begin Array Adapter
        adapter = ArrayAdapter.createFromResource(this, R.array.myLocations, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        // end Array Adapter

        getWeatherButton = (Button) findViewById(R.id.getWeather);
        ArrayList list = new ArrayList();

        queue = Volley.newRequestQueue(this);
        // url = "http://api.openweathermap.org/data/2.5/forecast?id=524901&appid=2de143494c0b295cca9337e1e96b00e0";

        //viewTv.setText(jokeString);
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
                            // get temp
                            JSONObject jObjList0 = arrList.getJSONObject(0);
                            JSONObject jObjMain = jObjList0.getJSONObject("main");
                            double temp = jObjMain.getDouble("temp");

                            // get icon
                            JSONArray arrDesc = jObjList0.getJSONArray("weather");
                            JSONObject objDec = arrDesc.getJSONObject(0);
                            description = objDec.getString("description");
                            String icon = objDec.getString("icon");

                            //get time
                          /* JSONObject objTime = jObjList0.getJSONObject("sys");
                           String time = objTime.getString("dt_txt").toString();*/

                            //Time time = objTime.get;

                            // get city name
                            JSONObject jObjCity = response.getJSONObject("city");
                            String city = jObjCity.getString("name");


                            // jokeString= jokeObj.toString();
                            dialog.cancel();
                            // String thisLocation = arrayView.toString();
                            arrayView.setText("on time " + "WILL BE TIME" + " the weather at " + city + " is " + description + " with temp: " + temp + " with icon " + icon);
                            // Toast.makeText(getApplicationContext(), jokeString, Toast.LENGTH_SHORT).show();

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




    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        TextView myText = (TextView) view;
        String myLocation = myText.toString();
        Toast.makeText(this, "Weather in " + myText.getText(), Toast.LENGTH_LONG).show();
        url = "http://api.openweathermap.org/data/2.5/forecast?q="+ myText.getText() +"&units=metric&appid=894f115787195ed5935778bee54ac0c5";
        myText.invalidate();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //init1
}
