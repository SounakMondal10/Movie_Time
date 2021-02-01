package com.sounakmondal.movietime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    //API Link - https://api.themoviedb.org/3/movie/550?api_key=0a365a15df03a57aa2c9e9c547c3bbc3
    //API Mocky Link - https://run.mocky.io/v3/40ff61ef-e8c9-47d6-8e1b-b25f3d704be0 -> 3xFIGHT CLUB
    //Now Playing Link - https://api.themoviedb.org/3/movie/now_playing?api_key=0a365a15df03a57aa2c9e9c547c3bbc3&language=en-US&page=1
    //Example Link - http://run.mocky.io/v3/79f722b0-a730-42a0-99aa-36029861f115
    //API Key (V3) - 0a365a15df03a57aa2c9e9c547c3bbc3
    //API ReadAccessToken (V4) - eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwYTM2NWExNWRmMDNhNTdhYTJjOWU5YzU0N2MzYmJjMyIsInN1YiI6IjVmZGFkOTc3ODU4Njc4MDAzZmY1YzhmNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.j7cm0KcsKod8xSNpDirzcY3h9EYOGe1EN-TjoRWD47U
    //For images, Add before path - https://image.tmdb.org/t/p/original/

    public static String JSON_URL; //add "1" in the end to revert
    public static String JSON_URL_Movies = "https://api.themoviedb.org/3/movie/now_playing?api_key=0a365a15df03a57aa2c9e9c547c3bbc3&language=en-US&page=";
    public static String JSON_URL_TVSeries = "https://api.themoviedb.org/3/tv/popular?api_key=0a365a15df03a57aa2c9e9c547c3bbc3&language=en-US&page=";
    List<MovieModelClass> movieList;
    RecyclerView recyclerView;
    Button loadMoreButton;
    public int lastMovieSeenPosition = 0;
    boolean mHasReachedBottom = false;
    Snackbar loadingSnackbar;
    Toolbar main_toolbar;
    Spinner main_spinner;
    GetData getData;
    Boolean isMovie;


    public static int currentPage = 1;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JSON_URL = JSON_URL_Movies;
        isMovie = true;

        main_toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);

        main_toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        main_toolbar.setSubtitleTextColor(Color.parseColor("#ffffff"));

        main_spinner = findViewById(R.id.mainActivity_spinner);
//        main_spinner.setBackgroundColor(Color.parseColor("#ffffff"));

        ArrayList<String> watchOptions = new ArrayList<String>();
        watchOptions.add("Movies");
        watchOptions.add("TV Series");

        //spinner adapter setup
        //ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, watchOptions);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.spinner_item, watchOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        main_spinner.setAdapter(spinnerAdapter);


        movieList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);


        JSON_URL = JSON_URL + currentPage;

        getData = new GetData();
        getData.execute();


        main_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
//                0 for movies, 1 for TV Series
                if(position == 1)
                {
                    getData.cancel(true);
                    JSON_URL = JSON_URL_TVSeries;
                    currentPage = 1;
                    JSON_URL+=currentPage;
                    movieList = new ArrayList<>();
                    loadingSnackbar = Snackbar.make(recyclerView,"Loading TV Series", Snackbar.LENGTH_LONG);
                    loadingSnackbar.setTextColor(getResources().getColor(R.color.colorAccent)).show();
                    getData = new GetData();
                    getData.execute();
                    isMovie = false;

                }
                else
                {
                    getData.cancel(true);
                    JSON_URL = JSON_URL_Movies;
                    currentPage = 1;
                    JSON_URL+=currentPage;
                    movieList = new ArrayList<>();
                    if(isMovie == false)
                    {
                        loadingSnackbar = Snackbar.make(recyclerView,"Loading Movies", Snackbar.LENGTH_LONG);
                        loadingSnackbar.setTextColor(getResources().getColor(R.color.colorAccent)).show();
                    }
                    getData = new GetData();
                    getData.execute();
                    isMovie = true;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Auto-refresh data when at the end of the list
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && !mHasReachedBottom) {

                    currentPage+=1;

                    loadingSnackbar = Snackbar.make(recyclerView,"Loading More", Snackbar.LENGTH_INDEFINITE);
                    loadingSnackbar.setTextColor(getResources().getColor(R.color.colorAccent)).show();

                    if(currentPage<=10)
                    {
                        JSON_URL = JSON_URL.substring(0, JSON_URL.length() - 1);
                    }
                    else JSON_URL = JSON_URL.substring(0, JSON_URL.length() - 2);
                    JSON_URL = JSON_URL + currentPage;
                    Log.i("JSON URL BUTTON",JSON_URL);
                    GetData getData = new GetData();
                    getData.execute();

                    //staying at the last movie to continue scrolling
                    lastMovieSeenPosition = movieList.size();

                    mHasReachedBottom = true;
                }
                else if (recyclerView.canScrollVertically(1))
                {
                    mHasReachedBottom = false;
                }
            }
        });

        //custom recyclerview item onClickListener
        try{
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ItemPage.setItem(movieList.get(position));
                Intent intent = new Intent(MainActivity.this,ItemPage.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));}catch (Exception e)
        {
            e.printStackTrace();
        }

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
            //Log.i("Returned String", s);
            try {
                lastMovieSeenPosition = movieList.size();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    //Log.i("JSON object id", jsonObject1.getString("id"));
                    MovieModelClass model = new MovieModelClass();
                    if(isMovie == true)
                    {
                        model.setName(jsonObject1.getString("original_title"));
                        model.setReleaseDate(jsonObject1.getString("release_date"));
                    }
                    else
                    {
                        model.setName(jsonObject1.getString("original_name"));
                        model.setReleaseDate(jsonObject1.getString("first_air_date"));
                    }
                    model.setId(jsonObject1.getString("id"));
                    model.setRating(jsonObject1.getString("vote_average"));
                    model.setImg("https://image.tmdb.org/t/p/original/" + jsonObject1.getString("poster_path"));
                    model.setBackdrop("https://image.tmdb.org/t/p/original/" + jsonObject1.getString("backdrop_path"));
                    model.setOriginalLanguage(jsonObject1.getString("original_language"));
                    model.setOverview(jsonObject1.getString("overview"));
                    movieList.add(model);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            putDataIntoRecyclerView(movieList);


            recyclerView.scrollToPosition(lastMovieSeenPosition -7);
            Adaptery.setMovieListUpdatedCopy(movieList);//adapter gets it's copy of the updated movieList

                try{
                    if(currentPage!=1)
                    {
                        loadingSnackbar.dismiss();
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

        }

    }

    private void putDataIntoRecyclerView(List<MovieModelClass> movieList)
    {
        Adaptery adaptery = new Adaptery(this, movieList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adaptery);
    }




}
