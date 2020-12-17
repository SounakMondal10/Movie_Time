package com.sounakmondal.movietime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    //API Link - https://api.themoviedb.org/3/movie/550?api_key=0a365a15df03a57aa2c9e9c547c3bbc3
    //API Mocky Link - https://run.mocky.io/v3/40ff61ef-e8c9-47d6-8e1b-b25f3d704be0 -> 3xFIGHT CLUB
    //Example Link - http://run.mocky.io/v3/79f722b0-a730-42a0-99aa-36029861f115
    //API Key (V3) - 0a365a15df03a57aa2c9e9c547c3bbc3
    //API ReadAccessToken (V4) - eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwYTM2NWExNWRmMDNhNTdhYTJjOWU5YzU0N2MzYmJjMyIsInN1YiI6IjVmZGFkOTc3ODU4Njc4MDAzZmY1YzhmNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.j7cm0KcsKod8xSNpDirzcY3h9EYOGe1EN-TjoRWD47U
    //For images, Add before path - https://image.tmdb.org/t/p/original/

    public static String JSON_URL = "https://run.mocky.io/v3/40ff61ef-e8c9-47d6-8e1b-b25f3d704be0";
    List<MovieModelClass> movieList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);

        GetData getData = new GetData();
        getData.execute();
    }

    public class GetData extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String current ="";

            try {
                URL url;
                HttpsURLConnection urlConnection = null;
                try {
                    url = new URL(JSON_URL);
                    urlConnection = (HttpsURLConnection) url.openConnection();
                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = isr.read();
                    while(data != -1)
                    {
                        current += (char)data;
                        data = isr.read();
                    }
                    return current;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (urlConnection != null)
                    {
                        urlConnection.disconnect();
                    }
            }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return current;
        }

        @Override
        protected void onPostExecute(String s) //string 's' is the string 'current' returned
        {
            Log.i("Returned String", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("movies");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    //Log.i("JSON object id", jsonObject1.getString("id"));
                    MovieModelClass model = new MovieModelClass();
                    model.setId(jsonObject1.getString("id"));
                    model.setName(jsonObject1.getString("original_title"));
                    model.setImg("https://image.tmdb.org/t/p/original/" + jsonObject1.getString("poster_path"));
                    model.setDescription(jsonObject1.getString("overview"));
                    movieList.add(model);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            putDataIntoRecyclerView(movieList);
        }
    }

    private void putDataIntoRecyclerView(List<MovieModelClass> movieList)
    {
        Adaptery adaptery = new Adaptery(this, movieList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptery);
    }



}
