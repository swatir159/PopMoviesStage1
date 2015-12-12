package com.example.android.popmoviesstage1;

/**
 * Created by swatir on 12/3/2015.
 */
/**
 * Singleton class to store the MovieDbUrl for theMovieDB API
 */

public class MovieDBConnector {
    //Volatile keyword ensures that multiple threads handle the unique/instance correctly
    private volatile static MovieDBConnector uniqueInstance;
/* the URL http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=a06a137818569958a0f7a1299a22809e */
    private final String url = "http://api.themoviedb.org/3/";
    private final String API_KEY = "";

    private MovieDBConnector() {
    }

    //Check for an instance and if there isn't one enter the synchronized method
    public static MovieDBConnector getInstance() {
        if (uniqueInstance == null) {
            synchronized (MovieDBConnector.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new MovieDBConnector(); //Once in the block, check again and if still null, create the instance
                }
            }
        }
        return uniqueInstance;
    }

    public String getMoviesQuery(String settingsValue) {
        return url + "discover/movie?sort_by=" + settingsValue + "&api_key=" + API_KEY;

    }

}

