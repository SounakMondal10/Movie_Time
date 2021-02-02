package com.sounakmondal.movietime;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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

import static com.sounakmondal.movietime.Adaptery.getMovieListUpdatedCopy;
import static com.sounakmondal.movietime.MainActivity.JSON_URL;
import static com.sounakmondal.movietime.MainActivity.isMovie;

public class ItemPage extends AppCompatActivity {
    ImageView backdrop;
    TextView name, date, language, overview, similar;
    Integer id;
    public static MovieModelClass item;
    List<MovieModelClass> similarItemsList;
    RecyclerView similarRecyclerView;
    GetSimilarData getSimilarData;

    String base_URL = "https://api.themoviedb.org/3/tv/";
    String id_URL ="";
    String extension_URL = "/similar?api_key=0a365a15df03a57aa2c9e9c547c3bbc3&language=en-US&page=1";
    String finalURL;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_page);

        backdrop = findViewById(R.id.item_page_backdropIV);
        name = findViewById(R.id.item_page_ItemName);
        date = findViewById(R.id.item_page_releaseDateTV);
        language = findViewById(R.id.item_page_language);
        overview = findViewById(R.id.item_page_overview);
        similar = findViewById(R.id.similar_recyclerViewText);
        similarRecyclerView = findViewById(R.id.similar_recyclerView);
        id_URL = item.getId();
        finalURL = base_URL+id_URL+extension_URL;
        id = Integer.parseInt(item.getId());
        similarItemsList = new ArrayList<>();


        try
        {
            Glide.with(getApplicationContext()).load(item.getBackdrop()).into(backdrop);
            name.setText(item.getName());
            date.setText(item.getReleaseDate().substring(0,4));
            language.setText(item.getOriginalLanguage());
            overview.setText(item.getOverview());
            getSimilarData = new GetSimilarData();
            getSimilarData.execute();

            if(isMovie==true)
            {
                similar.setText("Similar Movies");
            }
            else{similar.setText("Similar TV Shows");}
            Log.i("ID",id.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public class GetSimilarData extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String current = "";

            try {
                URL url;
                HttpsURLConnection urlConnection = null;
                try {
                    url = new URL(finalURL);
                    urlConnection = (HttpsURLConnection) url.openConnection();
                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = isr.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isr.read();
                    }
                    return current;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
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
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    //Log.i("JSON object id", jsonObject1.getString("id"));
                    MovieModelClass model = new MovieModelClass();
                    if (isMovie == true) {
                        model.setName(jsonObject1.getString("original_title"));
                        model.setReleaseDate(jsonObject1.getString("release_date"));
                    } else {
                        model.setName(jsonObject1.getString("original_name"));
                        model.setReleaseDate(jsonObject1.getString("first_air_date"));
                    }
                    model.setId(jsonObject1.getString("id"));
                    model.setRating(jsonObject1.getString("vote_average"));
                    model.setImg("https://image.tmdb.org/t/p/original/" + jsonObject1.getString("poster_path"));
                    model.setBackdrop("https://image.tmdb.org/t/p/original/" + jsonObject1.getString("backdrop_path"));
                    model.setOriginalLanguage(jsonObject1.getString("original_language"));
                    model.setOverview(jsonObject1.getString("overview"));

                    try {
                        if(similarItemsList.isEmpty()==false)
                        {
                            similarItemsList.clear();
                        }
                        similarItemsList.add(model);
                        putDataIntoRecyclerView(similarItemsList);
                    }catch (Exception e){e.printStackTrace();}
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void putDataIntoRecyclerView(List<MovieModelClass> movieList)
    {
        Adaptery adaptery = new Adaptery(this, movieList);
        similarRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        similarRecyclerView.setAdapter(adaptery);
    }


    public static void setItem(MovieModelClass item) {
        ItemPage.item = item;
    }
}
