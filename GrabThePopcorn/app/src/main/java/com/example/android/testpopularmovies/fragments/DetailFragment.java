package com.example.android.testpopularmovies.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.testpopularmovies.BuildConfig;
import com.example.android.testpopularmovies.R;
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

public class DetailFragment extends Fragment implements CastAdapter.OnItemClickListener, SeasonsAdapter.OnItemClickListener{

    View rootView;
    Context context;

    String myApiKey = BuildConfig.API_KEY;
    String castURL;
    ArrayList<Cast> mCast;

    private TextView title, release_date, user_rating, synopsis, seasons_episodes;
    private ImageView poster, backdrop;

    private RecyclerView RvCast;
    private CastAdapter castAdapter;
    private RecyclerView.LayoutManager layoutManager;

    RecyclerView RvSeasons;
    private SeasonsAdapter seasonsAdapter;
    private RecyclerView.LayoutManager layoutManager2;

    Toolbar toolbar;

    Movie movie;
    TVShow show;

    String page_key;

    String showURL;
    TVShow show_episodes;
    ArrayList<SeasonTVShow> seasons;
    TextView seasons_header;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_detail,container,false);

        toolbar = rootView.findViewById(R.id.toolbar_detail);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        //setHasOptionsMenu(false);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        title = rootView.findViewById(R.id.season_title);
        release_date = rootView.findViewById(R.id.release_date);
        user_rating = rootView.findViewById(R.id.rating);
        synopsis = rootView.findViewById(R.id.synopsis);
        poster = rootView.findViewById(R.id.poster);
        backdrop = rootView.findViewById(R.id.backdrop);
        seasons_episodes = rootView.findViewById(R.id.seasons_episodes);

        seasons_header = rootView.findViewById(R.id.seasons);
        RvSeasons = rootView.findViewById(R.id.rv_seasons);

        Bundle bundle = this.getArguments();
        page_key = bundle.getString("page_key");
        //String name = bundle.getString("name");
        //Log.d("NAME2", name);
        if (page_key.equals("movies")) {
            seasons_header.setVisibility(View.INVISIBLE);
            RvSeasons.setVisibility(View.INVISIBLE);
            movie = (Movie) bundle.getSerializable("selected");
            //Log.d("MOVIE2", String.valueOf(movie));
            title.setText(movie.getTitle());
            release_date.setText(movie.getReleaseDate());
            user_rating.setText(movie.getVoteAverage()+"/10");
            synopsis.setText(movie.getOverview());
            Picasso.get().load(movie.getPosterPath()).placeholder(R.drawable.image_placeholder).into(poster);
            Picasso.get().load(movie.getBackdropPath()).into(backdrop);

            toolbar.setTitle(movie.getTitle());

            castURL = "https://api.themoviedb.org/3/movie/"+movie.getId()+"/credits?api_key="+myApiKey;
        } else if (page_key.equals("shows")) {
            show = (TVShow) bundle.getSerializable("selected");
            //Log.d("MOVIE2", String.valueOf(movie));
            title.setText(show.getName());
            release_date.setText(show.getFirstAirDate());
            user_rating.setText(show.getVoteAverage()+"/10");
            synopsis.setText(show.getOverview());
            Picasso.get().load(show.getPosterPath()).placeholder(R.drawable.image_placeholder).into(poster);
            Picasso.get().load(show.getBackdropPath()).into(backdrop);

            toolbar.setTitle(show.getName());

            castURL = "https://api.themoviedb.org/3/tv/"+show.getId()+"/credits?api_key="+myApiKey;
            showURL = "https://api.themoviedb.org/3/tv/"+show.getId()+"?api_key="+myApiKey;
        }

        new FetchCast().execute();

        return rootView;
    }


    @Override
    public void onItemClick(int position, ArrayList<Cast> castList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected", castList.get(position));

        CastDetailFragment castDetailFragment = new CastDetailFragment();
        castDetailFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer2, castDetailFragment).addToBackStack(null).commit();
    }

    @Override
    public void onSeasonClick(int position, ArrayList<SeasonTVShow> seasons) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("season", (Serializable) seasons.get(position));
        bundle.putString("backdrop", show.getBackdropPath());
        bundle.putString("title", show.getName());
        bundle.putString("id", String.valueOf(show.getId()));

        SeasonsDetailFragment seasonsDetailFragment = new SeasonsDetailFragment();
        seasonsDetailFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer2, seasonsDetailFragment).addToBackStack(null).commit();

    }

    //AsyncTask
    public class FetchCast extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... voids) {


            mCast = new ArrayList<>();
            ArrayList<Object> res = new ArrayList<>();

            try {
                mCast = NetworkUtils.fetchDataCast(castURL); //Get cast
                res = NetworkUtils.fetchDataShowDetailed(showURL);
                show_episodes = (TVShow) res.get(0);
                seasons = (ArrayList<SeasonTVShow>) res.get(1);
            } catch (IOException e){
                e.printStackTrace();
            }
            //Log.d("CAST", String.valueOf(mCast));
            return null;
        }

        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);
            RvCast = rootView.findViewById(R.id.rv_cast);
            layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
            RvCast.setLayoutManager(layoutManager);
            castAdapter = new CastAdapter(context,mCast);
            RvCast.setAdapter(castAdapter);
            CastAdapter.setOnItemClickListener(DetailFragment.this);

            if (page_key.equals("shows")) {
                seasons_episodes.setText(show_episodes.getNumberSeasons()+" season(s), "+show_episodes.getNumberEpisodes()+" episode(s)");
                RvSeasons.setVisibility(View.VISIBLE);
                seasons_header.setVisibility(View.VISIBLE);
                layoutManager2 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                RvSeasons.setLayoutManager(layoutManager2);
                seasonsAdapter = new SeasonsAdapter(context, seasons);
                RvSeasons.setAdapter(seasonsAdapter);
                SeasonsAdapter.setOnItemClickListener(DetailFragment.this);
            }
        }
    }
}