package com.example.android.testpopularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.testpopularmovies.adapters.DisplayAdapter;
import com.example.android.testpopularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements DisplayAdapter.OnItemClickListener {

    Toolbar toolbar;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<Object> searchResults;
    ArrayList<String> typeList;
    String searchURL;
    String searchQuery;

    Button search_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setTitle("Search");
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchQuery = makeAPIQuery();

        search_button = findViewById(R.id.button_search);
        search_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchQuery = makeAPIQuery();
                TextView query = findViewById(R.id.search_query);
                query.setText("\""+searchQuery.replace("%20", " ")+"\"");
                new FetchData().execute();
            }
        });


    }


    public String makeAPIQuery() {
        String apiQuery;
        EditText et_search = findViewById(R.id.search_bar);
        String query = et_search.getText().toString();
        apiQuery = query.replace(" ","%20");
        return apiQuery;
    }

    /*@Override
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
    }*/

    @Override
    public void onItemClick(int position, ArrayList<Object> list) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("detail", (Serializable) list.get(position));
        if (typeList.get(position).equals("movie")) {
            intent.putExtra("type", "movie");
        } else if (typeList.get(position).equals("show")) {
            intent.putExtra("type", "show");
        }

        startActivity(intent);
    }


    //AsyncTask
    public class FetchData extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            searchURL = "https://api.themoviedb.org/3/search/multi?api_key="+BuildConfig.API_KEY+"&query="+searchQuery;

            searchResults = new ArrayList<>();

            try {
                if(NetworkUtils.networkStatus(SearchActivity.this)){
                    Context context = SearchActivity.this;
                    searchResults = (ArrayList<Object>) NetworkUtils.fetchDataSearch(searchURL).get(0);
                    typeList = (ArrayList<String>) NetworkUtils.fetchDataSearch(searchURL).get(1);
                }else{
                    Toast.makeText(SearchActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void  s) {
            super.onPostExecute(s);

            recyclerView = findViewById(R.id.rv_search);
            if (findViewById(R.id.tablet_search)!=null) {
                layoutManager = new GridLayoutManager(SearchActivity.this, 4);
            } else {
                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    layoutManager = new GridLayoutManager(SearchActivity.this, 4);
                } else {
                    layoutManager = new GridLayoutManager(SearchActivity.this, 2);
                }
            }
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);

            adapter = new DisplayAdapter(SearchActivity.this, searchResults);
            recyclerView.setAdapter(adapter);
            DisplayAdapter.setOnItemClickListener(SearchActivity.this);

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
}
