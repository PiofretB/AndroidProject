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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.testpopularmovies.adapters.CreditsAdapter;
import com.example.android.testpopularmovies.models.Cast;
import com.example.android.testpopularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class CastDetailActivity extends AppCompatActivity implements CreditsAdapter.OnItemClickListener {


    Cast cast_intent;
    String knownForURL;
    String castDetailsURL;
    Cast castDetails;
    ArrayList<Object> mKnownFor;
    ArrayList<String> typesList;

    private TextView name, birthday, birthPlace, biography, deathDay, deathDayTitle;
    private ImageView profile;

    private RecyclerView RvCredits;
    private CreditsAdapter creditsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_detail);

        Intent intent = getIntent();
        cast_intent = (Cast) intent.getSerializableExtra("cast_detail");

        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(cast_intent.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        name = findViewById(R.id.name);
        profile = findViewById(R.id.profile_picture);
        birthday = findViewById(R.id.birthday);
        birthPlace = findViewById(R.id.birth_place);
        biography = findViewById(R.id.biography);
        deathDay = findViewById(R.id.deathday);
        deathDay.setVisibility(View.INVISIBLE);
        deathDayTitle= findViewById(R.id.deathday_title);


        name.setText(cast_intent.getName());
        Picasso.get().load(cast_intent.getImg_link()).placeholder(R.drawable.default_cast_foreground).into(profile);

        new FetchDetails().execute();
    }

    @Override
    public void onItemClick(int position, ArrayList<Object> movieList) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("detail", (Serializable) movieList.get(position));
        if (typesList.get(position).equals("movie")) {
            intent.putExtra("type", "movie");
        } else if (typesList.get(position).equals("show")) {
            intent.putExtra("type", "show");
        }

        startActivity(intent);
    }


    //AsyncTask
    public class FetchDetails extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            knownForURL = "https://api.themoviedb.org/3/person/"+cast_intent.getId()+"/combined_credits?api_key="+BuildConfig.API_KEY;
            castDetailsURL = "https://api.themoviedb.org/3/person/"+cast_intent.getId()+"?api_key="+BuildConfig.API_KEY;

            castDetails = new Cast();
            mKnownFor = new ArrayList<Object>();

            try {
                if(NetworkUtils.networkStatus(CastDetailActivity.this)){
                    Context context = CastDetailActivity.this;
                    mKnownFor = (ArrayList<Object>) NetworkUtils.fetchDataCredits(knownForURL).get(0); //Get known for
                    typesList = (ArrayList<String>) NetworkUtils.fetchDataCredits(knownForURL).get(1);

                    castDetails = NetworkUtils.fetchDataCastDetails(castDetailsURL);


                }else{
                    Toast.makeText(CastDetailActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            Log.d("CREDITS", String.valueOf(mKnownFor));
            Log.d("CREDITS", String.valueOf(castDetails));
            return null;
        }

        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);
            //mProgressBar.setVisibility(View.INVISIBLE);

            if (castDetails.getBirthday() == "null") {
                birthday.setVisibility(View.INVISIBLE);
                findViewById(R.id.birthday_title).setVisibility(View.INVISIBLE);
            }
            birthday.setText(castDetails.getBirthday());
            if (castDetails.getBirth_place() == "null") {
                birthPlace.setVisibility(View.INVISIBLE);
                findViewById(R.id.birthplace_title).setVisibility(View.INVISIBLE);
            }
            birthPlace.setText(castDetails.getBirth_place());
            if (castDetails.getBiography() == "null") {
                biography.setVisibility((View.INVISIBLE));
                findViewById(R.id.biography_title).setVisibility(View.INVISIBLE);
            }
            Log.d("BIO", castDetails.getBiography());
            biography.setText(castDetails.getBiography());
            Log.d("DEATH", castDetails.getDeathday());
            if (castDetails.getDeathday()!="null") {
                deathDay.setText(" / "+castDetails.getDeathday());
                deathDayTitle.setText(" / Deathday");
                deathDay.setVisibility(View.VISIBLE);
                deathDayTitle.setVisibility(View.VISIBLE);
            } else {
                deathDay.setVisibility(View.INVISIBLE);
                deathDayTitle.setVisibility(View.INVISIBLE);
            }

            RvCredits = findViewById(R.id.rv_movie_credits);
            layoutManager = new LinearLayoutManager(CastDetailActivity.this,LinearLayoutManager.HORIZONTAL, false);
            RvCredits.setLayoutManager(layoutManager);
            creditsAdapter = new CreditsAdapter(CastDetailActivity.this,mKnownFor);
            RvCredits.setAdapter(creditsAdapter);
            CreditsAdapter.setOnItemClickListener(CastDetailActivity.this);
        }
    }
}
