package com.example.android.testpopularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.testpopularmovies.adapters.EpisodesAdapter;
import com.example.android.testpopularmovies.models.EpisodeTVShow;
import com.example.android.testpopularmovies.models.SeasonTVShow;
import com.example.android.testpopularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;

public class SeasonDetailActivity extends AppCompatActivity {

    String episodesURL;
    ArrayList<EpisodeTVShow> episodes;
    String myApiKey = BuildConfig.API_KEY;
    SeasonTVShow season;

    private TextView show_title, season_number;
    private ImageView season_poster, backdrop;
    String backdrop_path;
    String title;
    String id;

    private RecyclerView RvEpisodes;
    private EpisodesAdapter episodesAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        season = (SeasonTVShow) intent.getSerializableExtra("season");
        backdrop_path = intent.getStringExtra("backdrop");
        title = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        getSupportActionBar().setTitle(title);

        new FetchEpisodes().execute();

        show_title = findViewById(R.id.season_title);
        season_number = findViewById(R.id.season_number);
        season_poster = findViewById(R.id.poster);
        Log.d("POSTER", String.valueOf(season_poster));
        backdrop = findViewById(R.id.backdrop);

        Picasso.get().load(season.getPosterPath()).placeholder(R.drawable.image_placeholder).into(season_poster);
        Picasso.get().load(backdrop_path).into(backdrop);
        show_title.setText(season.getName());
        Log.d("NUMBER", String.valueOf(season.getSeasonNumber()));
        season_number.setText("Season "+String.valueOf(season.getSeasonNumber()));

    }

    //AsyncTask
    public class FetchEpisodes extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            episodesURL = "https://api.themoviedb.org/3/tv/"+id+"/season/"+season.getSeasonNumber()+"?api_key="+myApiKey;
            episodes = new ArrayList<>();

            try {
                if(NetworkUtils.networkStatus(SeasonDetailActivity.this)){
                    Context context = SeasonDetailActivity.this;
                    episodes = NetworkUtils.fetchDataEpisodes(episodesURL);

                }else{
                    Toast.makeText(SeasonDetailActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);

            RvEpisodes = findViewById(R.id.rv_episodes);
            layoutManager = new LinearLayoutManager(SeasonDetailActivity.this,LinearLayoutManager.VERTICAL, false);
            RvEpisodes.setLayoutManager(layoutManager);
            episodesAdapter = new EpisodesAdapter(SeasonDetailActivity.this,episodes);
            RvEpisodes.setAdapter(episodesAdapter);
        }
    }
}
