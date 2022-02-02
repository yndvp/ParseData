package com.example.parsedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    RequestQueue queue;
    String url = "https://www.google.com";
    String apiUrl = "https://jsonplaceholder.typicode.com/todos";
    String getApiUrl = "https://jsonplaceholder.typicode.com/todos/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textView);

//        queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = getJsonArrayRequest();
        JsonObjectRequest jsonObjectRequest = getJsonObjectRequest(textView);
        // Use singleton class
        queue = MySingleton.getInstance(this.getApplicationContext())
                .getRequestQueue();

        queue.add(jsonArrayRequest);
        queue.add(jsonObjectRequest);

        getString(queue);

    }

    @NonNull
    private JsonObjectRequest getJsonObjectRequest(TextView textView) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getApiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    textView.setText(response.getString("title"));
                    Log.d("jsonObj", "onCreate: " + response.getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("jsonObj", "onCreate: Failed!");

            }
        });
        return jsonObjectRequest;
    }

    @NonNull
    private JsonArrayRequest getJsonArrayRequest() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Log.d("JSON", "onCreate: " + jsonObject.getString("userId"));
                                // In case that values are not saved as string, getString() will not will work.
                                // Make sure to use the method that matches with the type of field

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSON", "onCreate: Failed!");

            }
        });
        return jsonArrayRequest;
    }

    private void getString(RequestQueue queue) {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 100 characters of the response string.
                        Log.d("Main", "Response is: " + response.substring(0, 100));
                    }
                }, new Response.ErrorListener() {
            @Override
            //In case of any error while fetching data we display this
            public void onErrorResponse(VolleyError error) {
                Log.d("Main", "That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}