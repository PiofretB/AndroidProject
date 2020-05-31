package com.example.android.testpopularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.testpopularmovies.BuildConfig;
import com.example.android.testpopularmovies.R;
import com.example.android.testpopularmovies.SearchActivity;
import com.example.android.testpopularmovies.adapters.MovieAdapter;
import com.example.android.testpopularmovies.adapters.TVShowAdapter;
import com.example.android.testpopularmovies.models.Movie;
import com.example.android.testpopularmovies.models.TVShow;
import com.example.android.testpopularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

public class MovieDisplayFragment extends Fragment implements MovieAdapter.OnItemClickListener, TVShowAdapter.OnShowClickListener {

    View rootView;

    Context context;

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

    //FragmentActionListener fragmentActionListener;

    String page_key = "";

    String type = "";

    Toolbar toolbar;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        page_key = bundle.getString("page_key");
        type = bundle.getString("type");

        new FetchMovies().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_dipslay,container,false);

        toolbar = rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        toolbar.setNavigationIcon(R.drawable.ic_home_white_24dp);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (page_key.equals("movies")) {
            if (id == R.id.popular) {
                refreshListMovies("popular");
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Popular Movies");
            }
            if (id == R.id.top_rated) {
                refreshListMovies("top_rated");
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Top Rated Movies");
            }
        } else {
            if (id == R.id.popular) {
                refreshListShows("popular");
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Popular Shows");
            }
            if (id == R.id.top_rated) {
                refreshListShows("top_rated");
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Top Rated Shows");
            }
        }

        if (id == R.id.search) {
            Intent intent = new Intent(context, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshListMovies(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("page_key", page_key);
        MovieDisplayFragment movieDisplayFragment = new MovieDisplayFragment();
        movieDisplayFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, movieDisplayFragment).commit();
    }

    private void refreshListShows(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("page_key", page_key);
        MovieDisplayFragment movieDisplayFragment = new MovieDisplayFragment();
        movieDisplayFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, movieDisplayFragment).commit();
    }

    @Override
    public void onItemClick(int position, ArrayList<Movie> movieList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected", movieList.get(position));
        bundle.putString("page_key", page_key);
        //bundle.putString("name", movieList.get(position).getTitle());
        //Log.d("MOVIE3", String.valueOf(movieList.get(position)));
        //Log.d("NAME", movieList.get(position).getTitle());

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer2, detailFragment).addToBackStack(null).commit();
    }

    /*public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }*/

    @Override
    public void onShowClick(int position, ArrayList<TVShow> showList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected", showList.get(position));
        bundle.putString("page_key", page_key);
        //bundle.putString("name", movieList.get(position).getTitle());
        //Log.d("MOVIE3", String.valueOf(movieList.get(position)));
        //Log.d("NAME", movieList.get(position).getTitle());

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer2, detailFragment).addToBackStack(null).commit();
    }

    //AsyncTask
    public class FetchMovies extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            popularMoviesURL = "https://api.themoviedb.org/3/movie/popular?api_key="+myApiKey;
            topRatedMoviesURL = "https://api.themoviedb.org/3/movie/top_rated?api_key="+myApiKey;
            popularShowsURL = "https://api.themoviedb.org/3/tv/popular?api_key="+myApiKey;
            topRatedShowsURL = "https://api.themoviedb.org/3/tv/top_rated?api_key="+myApiKey;

            mPopularList = new ArrayList<>();
            mTopRatedList = new ArrayList<>();

            try {
                    //Context context = MovieDisplayActivity.this;
                    mPopularList = NetworkUtils.fetchDataMovie(popularMoviesURL); //Get popular movies
                    mTopRatedList = NetworkUtils.fetchDataMovie(topRatedMoviesURL); //Get top rated movies
                    mPopularShows = NetworkUtils.fetchDataShow(popularShowsURL); // Get popular shows
                    mTopRatedShows = NetworkUtils.fetchDataShow(topRatedShowsURL); //Get top rated shows

            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);
            recyclerView = rootView.findViewById(R.id.movies_grid);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            recyclerView.setHasFixedSize(true);
            //recyclerView.setLayoutManager(layoutManager);


            // With buttons
            if (page_key.equals("movies")) {
                if (type.equals("popular")) {
                    adapter = new MovieAdapter(context, mPopularList);
                    recyclerView.setAdapter(adapter);
                    MovieAdapter.setOnItemClickListener(MovieDisplayFragment.this);
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Popular Movies");
                    //getSupportActionBar().setTitle("Popular Movies");
                } else if (type.equals("top_rated")) {
                    adapter = new MovieAdapter(context, mTopRatedList);
                    recyclerView.setAdapter(adapter);
                    MovieAdapter.setOnItemClickListener(MovieDisplayFragment.this);
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Top Rated Movies");
                    //getSupportActionBar().setTitle("Popular Movies");
                }
            } else if (page_key.equals("shows")){
                if (type.equals("popular")) {
                    adapter = new TVShowAdapter(context, mPopularShows);
                    recyclerView.setAdapter(adapter);
                    TVShowAdapter.setOnShowClickListener(MovieDisplayFragment.this);
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Popular Shows");
                    //getSupportActionBar().setTitle("Popular Shows");
                } else if (type.equals("top_rated")) {
                    adapter = new TVShowAdapter(context, mTopRatedShows);
                    recyclerView.setAdapter(adapter);
                    TVShowAdapter.setOnShowClickListener(MovieDisplayFragment.this);
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Top Rated Shows");
                    //getSupportActionBar().setTitle("Popular Shows");
                }
            }


        }
    }
}