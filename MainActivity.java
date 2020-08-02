package com.example.testing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*Configure for the app to communicate with the server
*in build.gradle(Module: app)
* implementation 'com.android.volley:volley:1.1.1'
*
* in res/(make a directory) xml/(make xml file)security.xml
*   <network-security-config>
*        <base-config cleartextTrafficPermitted="true" />
*   </network-security-config>
*
* in AndroidManifest.xml
* <uses-permission android:name="android.permission.INTERNET"></uses-permission>
* same but in <Application>
* android:networkSecurityConfig="@xml/security"
*
 */
public class MainActivity extends AppCompatActivity {
    TextView temp_textView, city_textView, date, description_textView;
    public FloatingActionButton refreshButton;
    private RelativeLayout parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temp_textView = findViewById(R.id.temp_id);
        city_textView = findViewById(R.id.city_id);
        description_textView = findViewById(R.id.description_id);
        parent = findViewById(R.id.parent);
        getWeatherData();
        refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeatherData();
            }
        });

    }

    private void getWeatherData() {
        String city = "minneapolis";
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=imperial&appid=7ae190be13829e2a333b53eb3ee18432";


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //json information from web gets assinged here
                    city_textView.setText(response.getString("name"));
                    JSONObject webDataMain = response.getJSONObject("main");
                    JSONArray webDataWeather = response.getJSONArray("weather");
                    JSONObject object = webDataWeather.getJSONObject(0);
                    String description_weather = object.getString("description");

                    //gets the string and removes anything after the period
                    String temp_string = webDataMain.getString("temp");
                    if(temp_string.contains(".")){
                        temp_string = temp_string.substring(0, temp_string.indexOf("."));
                    }
                    temp_textView.setText(temp_string + "\u00B0"+"F");


                    description_textView.setText(description_weather);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);

    }
}