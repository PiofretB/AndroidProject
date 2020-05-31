package com.example.android.testpopularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.testpopularmovies.adapters.CastAdapter;
import com.example.android.testpopularmovies.adapters.SeasonsAdapter;
import com.example.android.testpopularmovies.models.Cast;
import com.example.android.testpopularmovies.models.Movie;
import com.example.android.testpopularmovies.models.SeasonTVShow;
import com.example.android.testpopularmovies.models.TVShow;
import com.example.android.testpopularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements CastAdapter.OnItemClickListener, SeasonsAdapter.OnItemClickListener {

    String castURL;
    ArrayList<Cast> mCast;
    String myApiKey = BuildConfig.API_KEY;
    Movie movie_intent;
    TVShow show_intent;
    String page_type;

    String showURL;
    TVShow show_episodes;
    ArrayList<SeasonTVShow> seasons;


    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.seasons)
    TextView seasons_header;

    private TextView title, release_date, user_rating, synopsis, seasons_episodes;
    private ImageView poster, backdrop;

    private RecyclerView RvCast;
    private CastAdapter castAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.rv_seasons)
    RecyclerView RvSeasons;
    private SeasonsAdapter seasonsAdapter;
    private RecyclerView.LayoutManager layoutManager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE); //Hide Progressbar by Default
        seasons_header.setVisibility(View.INVISIBLE);
        RvSeasons.setVisibility(View.INVISIBLE);


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
        page_type = intent.getStringExtra("type");
        if (page_type.equals("movie")) {
            movie_intent = (Movie) intent.getSerializableExtra("detail");
            getSupportActionBar().setTitle(movie_intent.getTitle());
        } else {
            show_intent = (TVShow) intent.getSerializableExtra("detail");
            getSupportActionBar().setTitle(show_intent.getName());
        }

        new FetchData().execute();

        title = findViewById(R.id.season_title);
        release_date = findViewById(R.id.release_date);
        user_rating = findViewById(R.id.rating);
        poster = findViewById(R.id.poster);
        backdrop = findViewById(R.id.backdrop);
        synopsis = findViewById(R.id.synopsis);

        if (page_type.equals("movie")) {
            Picasso.get().load(movie_intent.getBackdropPath()).into(backdrop);
            Picasso.get().load((movie_intent.getPosterPath())).placeholder(R.drawable.image_placeholder).into(poster);
            title.setText(movie_intent.getTitle());
            user_rating.setText(movie_intent.getVoteAverage()+"/10");
            release_date.setText(movie_intent.getReleaseDate());
            synopsis.setText(movie_intent.getOverview());
        } else {
            Picasso.get().load(show_intent.getBackdropPath()).into(backdrop);
            Picasso.get().load((show_intent.getPosterPath())).placeholder(R.drawable.image_placeholder).into(poster);
            title.setText(show_intent.getName());
            user_rating.setText(show_intent.getVoteAverage()+"/10");
            release_date.setText(show_intent.getFirstAirDate());
            synopsis.setText(show_intent.getOverview());
        }

    }


    @Override
    public void onItemClick(int position, ArrayList<Cast> castList) {
        Intent intent = new Intent(this, CastDetailActivity.class);
        intent.putExtra("cast_detail", castList.get(position));
        startActivity(intent);
    }

    @Override
    public void onSeasonClick(int position, ArrayList<SeasonTVShow> seasons) {
        Intent intent = new Intent(this, SeasonDetailActivity.class);
        intent.putExtra("season", (Serializable) seasons.get(position));
        intent.putExtra("backdrop", show_intent.getBackdropPath());
        intent.putExtra("title", show_intent.getName());
        intent.putExtra("id", String.valueOf(show_intent.getId()));
        startActivity(intent);
    }


    //AsyncTask
    public class FetchData extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (page_type.equals("movie")) {
                castURL = "https://api.themoviedb.org/3/movie/"+movie_intent.getId()+"/credits?api_key="+myApiKey;
            } else {
                castURL = "https://api.themoviedb.org/3/tv/"+show_intent.getId()+"/credits?api_key="+myApiKey;
                showURL = "https://api.themoviedb.org/3/tv/"+show_intent.getId()+"?api_key="+myApiKey;
            }


            mCast = new ArrayList<>();
            show_episodes = new TVShow();

            ArrayList<Object> res = new ArrayList<>();


            try {
                if(NetworkUtils.networkStatus(DetailActivity.this)){
                    Context context = DetailActivity.this;
                    mCast = NetworkUtils.fetchDataCast(castURL); //Get cast
                    res = NetworkUtils.fetchDataShowDetailed(showURL);
                    show_episodes = (TVShow) res.get(0);
                    seasons = (ArrayList<SeasonTVShow>) res.get(1);
                }else{
                    Toast.makeText(DetailActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            Log.d("CAST", String.valueOf(mCast));
            return null;
        }

        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);
            seasons_episodes = findViewById(R.id.seasons_episodes);
            mProgressBar.setVisibility(View.INVISIBLE);
            RvCast = findViewById(R.id.rv_cast);
            layoutManager = new LinearLayoutManager(DetailActivity.this,LinearLayoutManager.HORIZONTAL, false);
            RvCast.setLayoutManager(layoutManager);
            castAdapter = new CastAdapter(DetailActivity.this,mCast);
            RvCast.setAdapter(castAdapter);
            CastAdapter.setOnItemClickListener(DetailActivity.this);


            if (page_type.equals("show")) {
                seasons_episodes.setText(show_episodes.getNumberSeasons()+" season(s), "+show_episodes.getNumberEpisodes()+" episode(s)");
                RvSeasons.setVisibility(View.VISIBLE);
                seasons_header.setVisibility(View.VISIBLE);
                layoutManager2 = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false);
                RvSeasons.setLayoutManager(layoutManager2);
                seasonsAdapter = new SeasonsAdapter(DetailActivity.this, seasons);
                RvSeasons.setAdapter(seasonsAdapter);
                SeasonsAdapter.setOnItemClickListener(DetailActivity.this);
            }
        }
    }
}