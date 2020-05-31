package com.example.android.testpopularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.android.testpopularmovies.models.Cast;
import com.example.android.testpopularmovies.models.EpisodeTVShow;
import com.example.android.testpopularmovies.models.Movie;
import com.example.android.testpopularmovies.models.SeasonTVShow;
import com.example.android.testpopularmovies.models.TVShow;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();


    public static ArrayList<Movie> fetchDataMovie(String url) throws IOException {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        ArrayList<Movie> movies2 = new ArrayList<Movie>();
        try {

            URL new_url = new URL(url+"&page=1"); //create a url from a String
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection(); //Opening a http connection  to the remote object
            connection.connect();

            InputStream inputStream = connection.getInputStream(); //reading from the object
            String results = IOUtils.toString(inputStream);  //IOUtils to convert inputstream objects into Strings type
            //Log.d("RESULTS", results);
            parseJsonMovie(results,movies);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            URL new_url = new URL(url+"&page=2"); //create a url from a String
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection(); //Opening a http connection  to the remote object
            connection.connect();

            InputStream inputStream = connection.getInputStream(); //reading from the object
            String results = IOUtils.toString(inputStream);  //IOUtils to convert inputstream objects into Strings type
            //Log.d("RESULTS", results);
            parseJsonMovie(results,movies2);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        movies.addAll(movies2);

        return movies;
    }

    public static void parseJsonMovie(String data, ArrayList<Movie> list){

        try {
            JSONObject mainObject = new JSONObject(data);


                JSONArray resArray = mainObject.getJSONArray("results"); //Getting the results object
                for (int i = 0; i < resArray.length(); i++) {
                    JSONObject jsonObject = resArray.getJSONObject(i);
                    Movie movie = new Movie(); //New Movie object
                    movie.setId(jsonObject.getInt("id"));
                    movie.setVoteAverage(jsonObject.getString("vote_average"));
                    movie.setVoteCount(jsonObject.getInt("vote_count"));
                    movie.setOriginalTitle(jsonObject.getString("original_title"));
                    movie.setTitle(jsonObject.getString("title"));
                    movie.setPopularity(jsonObject.getDouble("popularity"));
                    movie.setBackdropPath(jsonObject.getString("backdrop_path"));
                    movie.setOverview(jsonObject.getString("overview"));
                    movie.setReleaseDate(jsonObject.getString("release_date"));
                    movie.setPosterPath(jsonObject.getString("poster_path"));
                    //Adding a new movie object into ArrayList
                    list.add(movie);
                }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error occurred during JSON Parsing", e);
        }

        //Log.d("LIST", String.valueOf(list));

    }



    public static ArrayList<TVShow> fetchDataShow(String url) throws IOException {
        ArrayList<TVShow> tvShows = new ArrayList<TVShow>();
        ArrayList<TVShow> tvShows2 = new ArrayList<>();
        try {

            URL new_url = new URL(url+"&page=1"); //create a url from a String
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection(); //Opening a http connection  to the remote object
            connection.connect();

            InputStream inputStream = connection.getInputStream(); //reading from the object
            String results = IOUtils.toString(inputStream);  //IOUtils to convert inputstream objects into Strings type
            //Log.d("RESULTS", results);
            parseJsonShow(results,tvShows);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            URL new_url = new URL(url+"&page=2"); //create a url from a String
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection(); //Opening a http connection  to the remote object
            connection.connect();

            InputStream inputStream = connection.getInputStream(); //reading from the object
            String results = IOUtils.toString(inputStream);  //IOUtils to convert inputstream objects into Strings type
            //Log.d("RESULTS", results);
            parseJsonShow(results,tvShows2);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        tvShows.addAll(tvShows2);

        return tvShows;
    }

    public static void parseJsonShow(String data, ArrayList<TVShow> list){

        try {
            JSONObject mainObject = new JSONObject(data);

            JSONArray resArray = mainObject.getJSONArray("results"); //Getting the results object
            for (int i = 0; i < resArray.length(); i++) {
                JSONObject jsonObject = resArray.getJSONObject(i);
                TVShow show = new TVShow(); //New Movie object
                show.setId(jsonObject.getInt("id"));
                show.setVoteAverage(jsonObject.getString("vote_average"));
                show.setVoteCount(jsonObject.getInt("vote_count"));
                show.setOriginalName(jsonObject.getString("original_name"));
                show.setName(jsonObject.getString("name"));
                show.setPopularity(jsonObject.getDouble("popularity"));
                show.setBackdropPath(jsonObject.getString("backdrop_path"));
                show.setOverview(jsonObject.getString("overview"));
                show.setFirstAirDate(jsonObject.getString("first_air_date"));
                show.setPosterPath(jsonObject.getString("poster_path"));
                //Adding a new movie object into ArrayList
                list.add(show);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error occurred during JSON Parsing", e);
        }

        //Log.d("LIST", String.valueOf(list));

    }


    public static ArrayList<Cast> fetchDataCast(String url) throws IOException {
        ArrayList<Cast> cast = new ArrayList<Cast>();
        try {

            URL new_url = new URL(url); //create a url from a String
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection(); //Opening a http connection  to the remote object
            connection.connect();

            InputStream inputStream = connection.getInputStream(); //reading from the object
            String results = IOUtils.toString(inputStream);  //IOUtils to convert inputstream objects into Strings type
            //Log.d("RESULTS", results);
            parseJsonCast(results,cast);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return cast;
    }


    public static void parseJsonCast(String data, ArrayList<Cast> list) {
        try {
            JSONObject mainObject = new JSONObject(data);

            JSONArray resArray = mainObject.getJSONArray("cast"); //Getting the results object
            for (int i = 0; i < resArray.length(); i++) {
                JSONObject jsonObject = resArray.getJSONObject(i);
                Cast cast = new Cast(); //New Cast object
                cast.setId(jsonObject.getInt("id"));
                cast.setImg_link(jsonObject.getString("profile_path"));
                cast.setName(jsonObject.getString("name"));
                cast.setRole(jsonObject.getString("character"));

                /*cast.setBiography(jsonObject.getString("biography"));
                cast.setBirthday(jsonObject.getString("birthday"));
                cast.setBirth_place(jsonObject.getString("place_of_birth"));
                cast.setDeathday(jsonObject.getString("deathday"));*/

                //Adding a new cast object into ArrayList
                list.add(cast);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error occurred during JSON Parsing", e);
        }
    }

    public static Cast fetchDataCastDetails(String url) throws IOException {
        Cast cast = new Cast();
        try {

            URL new_url = new URL(url); //create a url from a String
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection(); //Opening a http connection  to the remote object
            connection.connect();

            InputStream inputStream = connection.getInputStream(); //reading from the object
            String results = IOUtils.toString(inputStream);  //IOUtils to convert inputstream objects into Strings type

            cast = parseJsonCastDetails(results);

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return cast;
    }


    public static Cast parseJsonCastDetails(String data) {
        Cast cast = new Cast();
        try {
            JSONObject mainObject = new JSONObject(data);

            cast.setId(mainObject.getInt("id"));
            cast.setImg_link(mainObject.getString("profile_path"));
            cast.setName(mainObject.getString("name"));
            cast.setBiography(mainObject.getString("biography"));
            cast.setBirthday(mainObject.getString("birthday"));
            cast.setBirth_place(mainObject.getString("place_of_birth"));
            cast.setDeathday(mainObject.getString("deathday"));
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return cast;
    }

    public static ArrayList<Object> fetchDataCredits(String url) throws IOException {
        ArrayList<Object> res = new ArrayList<>();
        ArrayList<Object> credits = new ArrayList<Object>();
        ArrayList<String> typeList = new ArrayList<>();
        try {

            URL new_url = new URL(url); //create a url from a String
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection(); //Opening a http connection  to the remote object
            connection.connect();

            InputStream inputStream = connection.getInputStream(); //reading from the object
            String results = IOUtils.toString(inputStream);  //IOUtils to convert inputstream objects into Strings type
            Log.d("RESULTS", results);
            parseJsonCredits(results,credits, typeList);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        res.add(credits);
        res.add(typeList);

        return res;
    }

    public static void parseJsonCredits(String data, ArrayList<Object> list, List<String> types){

        try {
            JSONObject mainObject = new JSONObject(data);
            Log.d("CREDITS2", String.valueOf(mainObject));

            JSONArray resArray = mainObject.getJSONArray("cast"); //Getting the results object
            for (int i = 0; i < resArray.length(); i++) {
                JSONObject jsonObject = resArray.getJSONObject(i);
                if (jsonObject.has("name")) {
                    TVShow show = new TVShow(); //New Movie object
                    show.setId(jsonObject.getInt("id"));
                    show.setVoteAverage(jsonObject.getString("vote_average"));
                    show.setVoteCount(jsonObject.getInt("vote_count"));
                    show.setOriginalName(jsonObject.getString("original_name"));
                    show.setName(jsonObject.getString("name"));
                    show.setPopularity(jsonObject.getDouble("popularity"));
                    show.setBackdropPath(jsonObject.getString("backdrop_path"));
                    show.setOverview(jsonObject.getString("overview"));
                    show.setFirstAirDate(jsonObject.getString("first_air_date"));
                    show.setPosterPath(jsonObject.getString("poster_path"));
                    //Adding a new movie object into ArrayList
                    list.add(show);
                    types.add("show");
                }else if (jsonObject.has("title")) {
                    Movie movie = new Movie(); //New Movie object
                    movie.setId(jsonObject.getInt("id"));
                    movie.setVoteAverage(jsonObject.getString("vote_average"));
                    movie.setVoteCount(jsonObject.getInt("vote_count"));
                    movie.setOriginalTitle(jsonObject.getString("original_title"));
                    movie.setTitle(jsonObject.getString("title"));
                    movie.setPopularity(jsonObject.getDouble("popularity"));
                    movie.setBackdropPath(jsonObject.getString("backdrop_path"));
                    movie.setOverview(jsonObject.getString("overview"));
                    movie.setReleaseDate(jsonObject.getString("release_date"));
                    movie.setPosterPath(jsonObject.getString("poster_path"));
                    //Adding a new movie object into ArrayList
                    list.add(movie);
                    types.add("movie");
                }
            }
            Log.d("LIST", String.valueOf(list));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error occurred during JSON Parsing", e);
        }

        //Log.d("LIST", String.valueOf(list));

    }


    public static ArrayList<Object> fetchDataSearch(String url) throws IOException {
        ArrayList<Object> res = new ArrayList<>();
        ArrayList<Object> credits = new ArrayList<Object>();
        ArrayList<String> typeList = new ArrayList<>();
        try {

            URL new_url = new URL(url); //create a url from a String
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection(); //Opening a http connection  to the remote object
            connection.connect();

            InputStream inputStream = connection.getInputStream(); //reading from the object
            String results = IOUtils.toString(inputStream);  //IOUtils to convert inputstream objects into Strings type
            Log.d("RESULTS", results);
            parseJsonSearch(results,credits, typeList);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        res.add(credits);
        res.add(typeList);

        return res;
    }
    public static void parseJsonSearch(String data, ArrayList<Object> list, List<String> types){

        try {
            JSONObject mainObject = new JSONObject(data);
            //Log.d("CREDITS2", String.valueOf(mainObject));

            JSONArray resArray = mainObject.getJSONArray("results"); //Getting the results object
            for (int i = 0; i < resArray.length(); i++) {
                JSONObject jsonObject = resArray.getJSONObject(i);
                if (jsonObject.has("name")) {
                    TVShow show = new TVShow(); //New Movie object
                    show.setId(jsonObject.getInt("id"));
                    show.setVoteAverage(jsonObject.getString("vote_average"));
                    show.setVoteCount(jsonObject.getInt("vote_count"));
                    show.setOriginalName(jsonObject.getString("original_name"));
                    show.setName(jsonObject.getString("name"));
                    show.setPopularity(jsonObject.getDouble("popularity"));
                    show.setBackdropPath(jsonObject.getString("backdrop_path"));
                    show.setOverview(jsonObject.getString("overview"));
                    show.setFirstAirDate(jsonObject.getString("first_air_date"));
                    show.setPosterPath(jsonObject.getString("poster_path"));
                    //Adding a new movie object into ArrayList
                    list.add(show);
                    types.add("show");
                }else if (jsonObject.has("title")) {
                    Movie movie = new Movie(); //New Movie object
                    movie.setId(jsonObject.getInt("id"));
                    movie.setVoteAverage(jsonObject.getString("vote_average"));
                    movie.setVoteCount(jsonObject.getInt("vote_count"));
                    movie.setOriginalTitle(jsonObject.getString("original_title"));
                    movie.setTitle(jsonObject.getString("title"));
                    movie.setPopularity(jsonObject.getDouble("popularity"));
                    movie.setBackdropPath(jsonObject.getString("backdrop_path"));
                    movie.setOverview(jsonObject.getString("overview"));
                    movie.setReleaseDate(jsonObject.getString("release_date"));
                    movie.setPosterPath(jsonObject.getString("poster_path"));
                    //Adding a new movie object into ArrayList
                    list.add(movie);
                    types.add("movie");
                }
            }
            Log.d("LIST", String.valueOf(list));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error occurred during JSON Parsing", e);
        }

        //Log.d("LIST", String.valueOf(list));

    }


    public static ArrayList<Object> fetchDataShowDetailed(String url) throws IOException {
        ArrayList<Object> res = new ArrayList<>();
        TVShow tvShow = new TVShow();
        ArrayList<SeasonTVShow> seasons = new ArrayList<SeasonTVShow>();

        try {

            URL new_url = new URL(url); //create a url from a String
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection(); //Opening a http connection  to the remote object
            connection.connect();

            InputStream inputStream = connection.getInputStream(); //reading from the object
            String results = IOUtils.toString(inputStream);  //IOUtils to convert inputstream objects into Strings type
            //Log.d("RESULTS", results);
            parseJsonShowDetailed(results,tvShow, seasons);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        res.add(tvShow);
        res.add(seasons);

        return res;
    }

    public static void parseJsonShowDetailed(String data, TVShow show, ArrayList<SeasonTVShow> seasons){

        try {
            JSONObject mainObject = new JSONObject(data);
            show.setNumberSeasons(mainObject.getInt("number_of_seasons"));
            show.setNumberEpisodes(mainObject.getInt("number_of_episodes"));

            JSONArray seasons_detail = mainObject.getJSONArray("seasons");
            for (int i = 0; i < seasons_detail.length(); i++) {
                JSONObject jsonObject = seasons_detail.getJSONObject(i);
                SeasonTVShow season = new SeasonTVShow();
                season.setAirDate(jsonObject.getString("air_date"));
                season.setEpisodeCount(jsonObject.getInt("episode_count"));
                season.setId(jsonObject.getInt("id"));
                season.setName(jsonObject.getString("name"));
                season.setOverview(jsonObject.getString("overview"));
                season.setPosterPath(jsonObject.getString("poster_path"));
                season.setSeasonNumber(jsonObject.getInt("season_number"));

                seasons.add(season);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error occurred during JSON Parsing", e);
        }

        //Log.d("LIST", String.valueOf(list));

    }



    public static ArrayList<EpisodeTVShow> fetchDataEpisodes(String url) throws IOException {
        ArrayList<EpisodeTVShow> episodes = new ArrayList<>();

        try {

            URL new_url = new URL(url); //create a url from a String
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection(); //Opening a http connection  to the remote object
            connection.connect();

            InputStream inputStream = connection.getInputStream(); //reading from the object
            String results = IOUtils.toString(inputStream);  //IOUtils to convert inputstream objects into Strings type
            //Log.d("RESULTS", results);
            parseJsonSeasons(results, episodes);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return episodes;
    }

    public static void parseJsonSeasons(String data, ArrayList<EpisodeTVShow> episodes){

        try {
            JSONObject mainObject = new JSONObject(data);

            JSONArray episode_list = mainObject.getJSONArray("episodes");
            for (int i = 0; i < episode_list.length(); i++) {
                JSONObject jsonObject = episode_list.getJSONObject(i);

                EpisodeTVShow episode = new EpisodeTVShow();
                episode.setAirDate(jsonObject.getString("air_date"));
                episode.setEpisodeNumber(jsonObject.getInt("episode_number"));
                episode.setId(jsonObject.getInt("id"));
                episode.setName(jsonObject.getString("name"));
                episode.setOverview(jsonObject.getString("overview"));
                episode.setStillPath(jsonObject.getString("still_path"));
                episode.setVoteAverage(jsonObject.getString("vote_average"));
                episode.setVoteCount(jsonObject.getInt("vote_count"));

                episodes.add(episode);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error occurred during JSON Parsing", e);
        }

        //Log.d("LIST", String.valueOf(list));

    }

    public static Boolean networkStatus(Context context){
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }
}
