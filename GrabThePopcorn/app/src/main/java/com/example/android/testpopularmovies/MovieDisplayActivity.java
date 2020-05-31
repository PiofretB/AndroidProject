package com.example.android.testpopularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.testpopularmovies.adapters.MovieAdapter;
import com.example.android.testpopularmovies.adapters.TVShowAdapter;
import com.example.android.testpopularmovies.fragments.MovieDisplayFragment;
import com.example.android.testpopularmovies.models.Movie;
import com.example.android.testpopularmovies.models.TVShow;
import com.example.android.testpopularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDisplayActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener, TVShowAdapter.OnShowClickListener {

    String myApiKey = BuildConfig.API_KEY;
    String popularMoviesURL;
    String topRatedMoviesURL;
    String popularShowsURL;
    String topRatedShowsURL;
    ArrayList<Movie> mPopularList;
    ArrayList<Movie> mTopRatedList;
    ArrayList<TVShow> mPopularShows;
    ArrayList<TVShow> mTopRatedShows;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    View rootView;


    private Toolbar toolbar;

    String page_key = "";

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @BindView(R.id.indeterminateBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);
        mProgressBar.setVisibility(View.INVISIBLE); //Hide Progressbar by Default

        Intent intent = getIntent();
        page_key = intent.getStringExtra("type");

        fragmentManager = getSupportFragmentManager();

        if(findViewById(R.id.activity_movies_phone)!= null){
            toolbar = findViewById(R.id.toolbar_detail);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            //getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            //getSupportActionBar().setDisplayShowHomeEnabled(true);

            new FetchMovies().execute();
        }else if (findViewById(R.id.activity_movies_tablet)!=null){
            addMovieDisplayFragment();
        }
    }

    private void addMovieDisplayFragment(){
        fragmentTransaction=fragmentManager.beginTransaction();

        MovieDisplayFragment movieListFragment=new MovieDisplayFragment();
        //movieListFragment.setFragmentActionListener(this);

        Bundle bundle=new Bundle();
        bundle.putString("page_key",page_key);
        bundle.putString("type", "popular");
        movieListFragment.setArguments(bundle);

        fragmentTransaction.add(R.id.fragmentContainer,movieListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(int position, ArrayList<Movie> movieList) {
        if(!movieList.get(position).isFooter()) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("detail", movieList.get(position));
            intent.putExtra("type", "movie");

            startActivity(intent);
        }
    }

    @Override
    public void onShowClick(int position, ArrayList<TVShow> showList) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("detail", showList.get(position));
        intent.putExtra("type", "show");

        startActivity(intent);
    }

    //AsyncTask
    public class FetchMovies extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            popularMoviesURL = "https://api.themoviedb.org/3/movie/popular?api_key="+myApiKey;
            topRatedMoviesURL = "https://api.themoviedb.org/3/movie/top_rated?api_key="+myApiKey;
            popularShowsURL = "https://api.themoviedb.org/3/tv/popular?api_key="+myApiKey;
            topRatedShowsURL = "https://api.themoviedb.org/3/tv/top_rated?api_key="+myApiKey;

            mPopularList = new ArrayList<>();
            mTopRatedList = new ArrayList<>();

            try {
                if(NetworkUtils.networkStatus(MovieDisplayActivity.this)){
                    Context context = MovieDisplayActivity.this;
                    mPopularList = NetworkUtils.fetchDataMovie(popularMoviesURL); //Get popular movies
                    mTopRatedList = NetworkUtils.fetchDataMovie(topRatedMoviesURL); //Get top rated movies
                    mPopularShows = NetworkUtils.fetchDataShow(popularShowsURL); // Get popular shows
                    mTopRatedShows = NetworkUtils.fetchDataShow(topRatedShowsURL); //Get top rated shows
                }else{
                    Toast.makeText(MovieDisplayActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            Log.d("POPULAR", String.valueOf(mPopularList));
            return null;
        }

        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);
            mProgressBar.setVisibility(View.INVISIBLE);
            recyclerView = findViewById(R.id.movies_grid);

            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                layoutManager = new GridLayoutManager(MovieDisplayActivity.this, 4);
            } else {
                layoutManager = new GridLayoutManager(MovieDisplayActivity.this, 2);
            }
            //layoutManager = new GridLayoutManager(MovieDisplayActivity.this,2);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);

            // With buttons
            if (page_key.equals("movies")) {
                adapter = new MovieAdapter(MovieDisplayActivity.this, mPopularList);
                recyclerView.setAdapter(adapter);
                MovieAdapter.setOnItemClickListener(MovieDisplayActivity.this);
                getSupportActionBar().setTitle("Popular Movies");

            } else if (page_key.equals("shows")){
                adapter = new TVShowAdapter(MovieDisplayActivity.this, mPopularShows);
                recyclerView.setAdapter(adapter);
                TVShowAdapter.setOnShowClickListener(MovieDisplayActivity.this);
                getSupportActionBar().setTitle("Popular Shows");
            }




        }
    }




    public Activity getActivity(){
        Context context = this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    // With menu choice
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (findViewById(R.id.fragmentContainer2)==null) {
            getMenuInflater().inflate(R.menu.menu_options, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (findViewById(R.id.fragmentContainer2)==null) {
            if (page_key.equals("movies")) {
                if (id == R.id.popular) {
                    refreshListMovies(mPopularList);
                    getSupportActionBar().setTitle("Popular Movies");
                }
                if (id == R.id.top_rated) {
                    refreshListMovies(mTopRatedList);
                    getSupportActionBar().setTitle("Top Rated Movies");
                }
            } else {
                if (id == R.id.popular) {
                    refreshListShows(mPopularShows);
                    getSupportActionBar().setTitle("Popular Shows");
                }
                if (id == R.id.top_rated) {
                    refreshListShows(mTopRatedShows);
                    getSupportActionBar().setTitle("Top Rated Shows");
                }
            }
        }

        if (id == R.id.search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshListMovies(ArrayList<Movie> list) {
        MovieAdapter adapter = new MovieAdapter(MovieDisplayActivity.this, list);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void refreshListShows(ArrayList<TVShow> list) {
        TVShowAdapter adapter = new TVShowAdapter(MovieDisplayActivity.this, list);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
