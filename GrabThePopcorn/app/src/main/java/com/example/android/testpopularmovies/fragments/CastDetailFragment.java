package com.example.android.testpopularmovies.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.example.android.testpopularmovies.adapters.CreditsAdapter;
import com.example.android.testpopularmovies.models.Cast;
import com.example.android.testpopularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class CastDetailFragment extends Fragment implements CreditsAdapter.OnItemClickListener{

    View rootView;
    Context context;

    Cast cast_bundle;

    ArrayList<Object> mKnownFor;
    String knownForURL;
    ArrayList<String> typesList;
    Cast castDetails;
    String castDetailsURL;

    private TextView name, birthday, birthPlace, biography, deathDay, deathDayTitle;
    private ImageView profile;

    private RecyclerView RvCredits;
    private CreditsAdapter creditsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    String myApiKey = BuildConfig.API_KEY;

    Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_actor_detail,container,false);

        toolbar = rootView.findViewById(R.id.toolbar_detail);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        name = rootView.findViewById(R.id.name);
        profile = rootView.findViewById(R.id.profile_picture);
        birthday = rootView.findViewById(R.id.birthday);
        birthPlace = rootView.findViewById(R.id.birth_place);
        biography = rootView.findViewById(R.id.biography);
        deathDay = rootView.findViewById(R.id.deathday);
        deathDay.setVisibility(View.INVISIBLE);
        deathDayTitle= rootView.findViewById(R.id.deathday_title);

        Bundle bundle = this.getArguments();
        cast_bundle = (Cast) bundle.getSerializable("selected");
        knownForURL = "https://api.themoviedb.org/3/person/"+cast_bundle.getId()+"/combined_credits?api_key="+BuildConfig.API_KEY;
        castDetailsURL = "https://api.themoviedb.org/3/person/"+cast_bundle.getId()+"?api_key="+BuildConfig.API_KEY;

        name.setText(cast_bundle.getName());
        Picasso.get().load(cast_bundle.getImg_link()).placeholder(R.drawable.default_cast_foreground).into(profile);

        toolbar.setTitle(cast_bundle.getName());

        new FetchDetails().execute();

        return rootView;
    }

    @Override
    public void onItemClick(int position, ArrayList<Object> list) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected", (Serializable) list.get(position));
        bundle.putString("page_key", typesList.get(position)+"s");

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer2, detailFragment).addToBackStack(null).commit();
    }


    //AsyncTask
    public class FetchDetails extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... voids) {



            castDetails = new Cast();
            mKnownFor = new ArrayList<Object>();

            try {
                castDetails = NetworkUtils.fetchDataCastDetails(castDetailsURL); //Get cast
                mKnownFor = (ArrayList<Object>) NetworkUtils.fetchDataCredits(knownForURL).get(0); //Get known for
                typesList = (ArrayList<String>) NetworkUtils.fetchDataCredits(knownForURL).get(1);
            } catch (IOException e){
                e.printStackTrace();
            }
            //Log.d("CAST", String.valueOf(mCast));
            return null;
        }

        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);
            if (castDetails.getBirthday().equals("null")) {
                birthday.setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.birthday_title).setVisibility(View.INVISIBLE);
            }
            birthday.setText(castDetails.getBirthday());
            if (castDetails.getBirth_place().equals("null")) {
                birthPlace.setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.birthplace_title).setVisibility(View.INVISIBLE);
            }
            birthPlace.setText(castDetails.getBirth_place());
            if (castDetails.getBiography().equals("null")) {
                biography.setVisibility((View.INVISIBLE));
                rootView.findViewById(R.id.biography_title).setVisibility(View.INVISIBLE);
            }
            Log.d("BIO", castDetails.getBiography());
            biography.setText(castDetails.getBiography());
            Log.d("DEATH", castDetails.getDeathday());
            if (!castDetails.getDeathday().equals("null")) {
                deathDay.setText(" / " + castDetails.getDeathday());
                deathDayTitle.setText(" / Deathday");
                deathDay.setVisibility(View.VISIBLE);
                deathDayTitle.setVisibility(View.VISIBLE);
            } else {
                deathDay.setVisibility(View.INVISIBLE);
                deathDayTitle.setVisibility(View.INVISIBLE);
            }

            RvCredits = rootView.findViewById(R.id.rv_movie_credits);
            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            RvCredits.setLayoutManager(layoutManager);
            creditsAdapter = new CreditsAdapter(context, mKnownFor);
            RvCredits.setAdapter(creditsAdapter);
            CreditsAdapter.setOnItemClickListener(CastDetailFragment.this);
        }
    }
}
