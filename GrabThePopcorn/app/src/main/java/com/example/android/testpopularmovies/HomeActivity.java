package com.example.android.testpopularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    Button mMovies;
    Button mShows;
    Button mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        mMovies = findViewById(R.id.movies);
        mMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMovies();
            }
        });

        mShows = findViewById(R.id.tv_shows);
        mShows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTVShows();
            }
        });

        mSearch = findViewById(R.id.search);
        mSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showSearch();
            }
        });
    }

    public void showMovies() {
        Intent intent = new Intent(this, MovieDisplayActivity.class);
        intent.putExtra("type", "movies");
        startActivity(intent);
    }

    public void showTVShows() {
        Intent intent = new Intent(this, MovieDisplayActivity.class);
        intent.putExtra("type", "shows");
        startActivity(intent);
    }

    public void showSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
