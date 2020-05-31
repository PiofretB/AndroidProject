package com.example.android.testpopularmovies.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.testpopularmovies.BuildConfig;
import com.example.android.testpopularmovies.R;
import com.example.android.testpopularmovies.adapters.EpisodesAdapter;
import com.example.android.testpopularmovies.models.EpisodeTVShow;
import com.example.android.testpopularmovies.models.SeasonTVShow;
import com.example.android.testpopularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class SeasonsDetailFragment extends Fragment {

    View rootView;
    Toolbar toolbar;

    Context context;

    String episodesURL;
    ArrayList<EpisodeTVShow> episodes;
    String myApiKey = BuildConfig.API_KEY;

    SeasonTVShow season;
    String backdrop_path, title, id;
    private TextView show_title, season_number;
    private ImageView season_poster, backdrop;

    private RecyclerView RvEpisodes;
    private EpisodesAdapter episodesAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_season,container,false);

        backdrop = rootView.findViewById(R.id.backdrop);
        backdrop.getLayoutParams().height = 800;

        toolbar = rootView.findViewById(R.id.toolbar_detail);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        Bundle bundle = this.getArguments();
        season = (SeasonTVShow) bundle.getSerializable("season");
        backdrop_path = bundle.getString("backdrop");
        title = bundle.getString("title");
        id = bundle.getString("id");
        toolbar.setTitle(title);

        new FetchEpisodes().execute();

        show_title = rootView.findViewById(R.id.season_title);
        season_number = rootView.findViewById(R.id.season_number);
        season_poster = rootView.findViewById(R.id.poster);


        Picasso.get().load(season.getPosterPath()).placeholder(R.drawable.image_placeholder).into(season_poster);
        Picasso.get().load(backdrop_path).into(backdrop);
        show_title.setText(season.getName());
        season_number.setText("Season "+String.valueOf(season.getSeasonNumber()));

        return rootView;
    }

    //AsyncTask
    public class FetchEpisodes extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            episodesURL = "https://api.themoviedb.org/3/tv/"+id+"/season/"+season.getSeasonNumber()+"?api_key="+myApiKey;
            episodes = new ArrayList<>();

            try {
                episodes = NetworkUtils.fetchDataEpisodes(episodesURL);

            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);

            RvEpisodes = rootView.findViewById(R.id.rv_episodes);
            layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false);
            RvEpisodes.setLayoutManager(layoutManager);
            episodesAdapter = new EpisodesAdapter(context,episodes);
            RvEpisodes.setAdapter(episodesAdapter);
        }
    }
}
