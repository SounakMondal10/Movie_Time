package com.sounakmondal.movietime;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.sounakmondal.movietime.Adaptery.getMovieListUpdatedCopy;

public class ItemPage extends AppCompatActivity {
    ImageView backdrop;
    TextView name, date, language, overview;
    Integer id;
    public static MovieModelClass item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_page);

        backdrop = findViewById(R.id.item_page_backdropIV);
        name = findViewById(R.id.item_page_ItemName);
        date = findViewById(R.id.item_page_releaseDateTV);
        language = findViewById(R.id.item_page_language);
        overview = findViewById(R.id.item_page_overview);
        id = Integer.parseInt(item.getId());

        try
        {
            Glide.with(getApplicationContext()).load(item.getBackdrop()).into(backdrop);
            name.setText(item.getName());
            date.setText(item.getReleaseDate().substring(0,4));
            language.setText(item.getOriginalLanguage());
            overview.setText(item.getOverview());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void setItem(MovieModelClass item) {
        ItemPage.item = item;
    }
}
