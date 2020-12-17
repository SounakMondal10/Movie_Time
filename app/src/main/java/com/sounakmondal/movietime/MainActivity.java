package com.sounakmondal.movietime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //API Link - https://api.themoviedb.org/3/movie/550?api_key=0a365a15df03a57aa2c9e9c547c3bbc3
    //API Mocky Link - https://run.mocky.io/v3/13e07359-e552-4cb1-941d-b8925f1b73e9
    //API Key (V3) - 0a365a15df03a57aa2c9e9c547c3bbc3
    //API ReadAccessToken (V4) - eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwYTM2NWExNWRmMDNhNTdhYTJjOWU5YzU0N2MzYmJjMyIsInN1YiI6IjVmZGFkOTc3ODU4Njc4MDAzZmY1YzhmNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.j7cm0KcsKod8xSNpDirzcY3h9EYOGe1EN-TjoRWD47U
    //For images, Add before path - https://image.tmdb.org/t/p/original/

    public static String JSON_URL = "https://run.mocky.io/v3/13e07359-e552-4cb1-941d-b8925f1b73e9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
