package com.example.android.popmoviesstage1;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;
import android.util.Log;

import static com.example.android.popmoviesstage1.R.id.pomoviepostergridview;
/**
 * The main fragment that contains the GridView screen. Has ImageWorker class w/ImageCache to load children
 * asynchronously, keeping the UI nice and smooth and caching thumbnails for quick retrieval. The
 * cache is retained over configuration changes like orientation change so the images are populated
 * quickly if, for example, the user rotates the device.
 * Modeled after example in android.developer.com
 */
public class MainActivityFragment extends Fragment {

    private ImageAdapter mAdapter;
    public List<Film> films;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if ( !isNetworkAvailable() ){
            Log.d("logcat_tag", "No internet Connection available, quitting");
           // noInternetAlertMessage();
            //displayToast( this.getContext(), "No Network Available");
        }
        else
        {
            Log.d("logcat_tag", "Internet Connection available, getting the movie names");
            MovieDBConnector url = MovieDBConnector.getInstance();
            String getMoviesHttpMethod = url.getMoviesQuery("popularity.desc");

            AsyncDownloader downloader = new AsyncDownloader(this.getContext(), MainActivityFragment.this);
            downloader.execute(getMoviesHttpMethod);
        }

        GridView gridview = (GridView) rootView.findViewById(R.id.pomoviepostergridview);

        mAdapter = new ImageAdapter(this.getContext());
        gridview.setAdapter(mAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //displayToast(v.getContext(), "" + position);
                displayMovieDetails(parent, position);

            }
        });
        return rootView;
    }

    private void displayToast(Context context, String text) {
        //Context context = myView.getContext();
        int duration = Toast.LENGTH_SHORT;

        //Resources res = getResources();
        //String text = res.getString(textid);
        CharSequence styledText = Html.fromHtml(text);

        Toast toast = Toast.makeText(context, styledText, duration);
        toast.show();
    }

    private void noInternetAlertMessage() {

        android.support.v4.app.FragmentManager dlgFragment = getFragmentManager();
        if (dlgFragment != null){
            NoInternetConnectionDialog dialog = new NoInternetConnectionDialog();
            //dial_og.show(getFragmentManager(), "no_internet_error_dialog");
            dialog.show( dlgFragment , "no_internet_error_dialog");
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    public void updateAdapter(List<Film> films) {
        mAdapter.setFilms(films);
        mAdapter.notifyDataSetChanged();
    }

    private void displayMovieDetails(View v, int position ){
        // Send intent to SingleViewActivity
        Intent i = new Intent(v.getContext(), DisplayMovieDetailsActivity.class);

        // Pass image index
        i.putExtra("id", position);
        startActivity(i);
    }
}
