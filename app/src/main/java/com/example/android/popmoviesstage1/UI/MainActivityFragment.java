package com.example.android.popmoviesstage1.UI;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import com.example.android.popmoviesstage1.Model.Film;
import com.example.android.popmoviesstage1.ImageAdapter;
import com.example.android.popmoviesstage1.Util.NoInternetConnectionDialog;
import com.example.android.popmoviesstage1.R;
import com.example.android.popmoviesstage1.WebDataFetcher.AsyncDownloader;
import com.example.android.popmoviesstage1.WebDataFetcher.MovieDBConnector;

/**
 * The main fragment that contains the GridView screen. Has ImageWorker class w/ImageCache to load children
 * asynchronously, keeping the UI nice and smooth and caching thumbnails for quick retrieval. The
 * cache is retained over configuration changes like orientation change so the images are populated
 * quickly if, for example, the user rotates the device.
 * Modeled after example in android.developer.com
 *
 * http://www.androiddesignpatterns.com/2013/04/retaining-objects-across-config-changes.html
 */
public class MainActivityFragment extends Fragment {

    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    interface TaskCallbacks {
        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute();
    }

    public TaskCallbacks getCallbacks() {
        return mCallbacks;
    }
    private TaskCallbacks mCallbacks;

    private ImageAdapter mAdapter;
    private ArrayList<Film> mFilmsList;
    private String mOptionChosen;
    private String mLastOption;
    // To check if the current chosen option is same as the last one - if yes we need to cancel the running asynctask
    private AsyncDownloader mDownloader;
    private int  mImageSize;
    private int  mImageSpacing;
    //private boolean isTaskRunning = false;

    public MainActivityFragment() {
        //mAdapter = null;
        //mFilmsList = null;
        mLastOption = "";
        //mImageSize = 100;
        //mImageSpacing = 5;

    }

    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        setRetainInstance(true);

        String InstanceTagLocal =  this.getActivity().getResources().getString(R.string.InstanceTag1);
        if(savedInstanceState == null || !savedInstanceState.containsKey(InstanceTagLocal)) {
            //mFilmsList = new ArrayList<Film>(Arrays.asList(Film));
            if (mOptionChosen == null || mOptionChosen.trim().isEmpty()  ) {
                mOptionChosen = this.getActivity().getResources().getString(R.string.OptionMenu1);
            }
            Log.d(getResources().getString(R.string.logcat_tag), getResources().getString(R.string.Message2) + "instance: " + this.toString());
            createMovieList();
        }
        else {
            mFilmsList = savedInstanceState.getParcelableArrayList(InstanceTagLocal);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String InstanceTagLocal =  this.getActivity().getResources().getString(R.string.InstanceTag1);
        outState.putParcelableArrayList(InstanceTagLocal, mFilmsList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setImageParams();
        GridView gridview = (GridView) rootView.findViewById(R.id.pomoviepostergridview);
        mAdapter = new ImageAdapter( (Context)this.getActivity(), mFilmsList);
        gridview.setAdapter(mAdapter);
        mAdapter.setImageSizeDetails(mImageSize, mImageSpacing);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //displayToast(v.getContext(), "" + position);
                displayMovieDetails(parent, position);

            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                // Sort by Order
                /* http://api.themoviedb.org/3/discover/movie?sort_by=ratings.asc&api_key=deea9711e0770caae3fc592b028bb17e */
                mOptionChosen = getResources().getString(R.string.OptMenu1Value);

            case R.id.item2:
                // Sort by popularity
                mOptionChosen = getResources().getString(R.string.OptMenu2Value);
             case R.id.item3:
                mOptionChosen =getResources().getString(R.string.OptMenu3Value);
            case R.id.item4:
                mOptionChosen = getResources().getString(R.string.OptMenu4Value);
            default:
                break;
        }

        switch (item.getItemId()) {
            case R.id.item1:
            case R.id.item2:
            case R.id.item3:
            case R.id.item4:
                if ( !mLastOption.equals(mOptionChosen)){
                    mLastOption = mOptionChosen;
                    cancelAsynctaskIfExists();
                    createMovieList();
                }
                else
                {
                    // same option chosen again - no action
                }

            default:
                break;
        }

        return false;
    }
    public void updateFilmList(List<Film> FilmListParam)
    {
        if ( mFilmsList != null && !mFilmsList.isEmpty())
        {
            mFilmsList.clear();
            mFilmsList = null;
        }

        int dataSize = FilmListParam.size();

        if (dataSize != 0) {
            mFilmsList = new ArrayList<Film>(dataSize);

            for (int i = 0; i < dataSize; i++) {
                mFilmsList.add( FilmListParam.get(i));
            }
        }
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

        FragmentManager dlgFragment = getFragmentManager();
        if (dlgFragment != null){
            NoInternetConnectionDialog dialog = new NoInternetConnectionDialog();
            displayToast( (Context)this.getActivity(), this.getActivity().getResources().getString(R.string.no_internet_message) );
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)( (Context)this.getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    public void updateAdapter(List<Film> FilmsParam) {
        mAdapter.setFilms(FilmsParam);
        mAdapter.notifyDataSetChanged();
    }

    private void displayMovieDetails(View v, int position ){
        String InstanceTagLocal =  this.getActivity().getResources().getString(R.string.InstanceTag2);
        Intent intent = new Intent(v.getContext(), DisplayMovieDetailsActivity.class);

        Film mySelectedFilm = mAdapter.getFilms().get(position);

        Bundle selectedFilmData = new Bundle();
        selectedFilmData.putParcelable(InstanceTagLocal, mySelectedFilm);

        intent.putExtra(InstanceTagLocal, mySelectedFilm);
        startActivity(intent);
    }

    private void initiateDownloadFromWeb() {
        Log.d(getResources().getString(R.string.logcat_tag), getResources().getString(R.string.Message4));
        MovieDBConnector url = MovieDBConnector.getInstance();
        if (mOptionChosen == null || mOptionChosen.trim().isEmpty()  ) {
            mOptionChosen = getResources().getString(R.string.OptMenu1Value);
        }

        url.setmAPIKey(getResources().getString(R.string.themovieDB_api_key));
        String getMoviesHttpMethod = url.getMoviesQuery(mOptionChosen);

        mDownloader = new AsyncDownloader((Context)this.getActivity(), this );
        mDownloader.execute(getMoviesHttpMethod);
    }

    private void createMovieList()
    {

        if ( !isNetworkAvailable() ){
            Log.d(getResources().getString(R.string.logcat_tag), getResources().getString(R.string.Message3));
            // noInternetAlertMessage();
            //displayToast( this.getContext(), "No Network Available");
        }
        else
        {
            if ( mFilmsList != null && !mFilmsList.isEmpty())
            {
                mFilmsList.clear();
                mFilmsList = null;
            }

            initiateDownloadFromWeb();
        }
    }
    private void setImageParams()
    {
        // Fetch screen height and width, to use as our max size when loading images as this
        // activity runs full screen
        mImageSize= 100;
        mImageSpacing = (int) (mImageSize*0.05);
        try{
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            final int height = displayMetrics.heightPixels;
            final int width = displayMetrics.widthPixels;
            mImageSize = (height > width ? height : width) / 2;
            mImageSpacing = (int) (mImageSize*0.05);

        }catch(Exception e)
        {
            Log.d(getResources().getString(R.string.logcat_tag), getResources().getString(R.string.Message1));
            e.printStackTrace();
        }

    }

    private void cancelAsynctaskIfExists(){
        if (mDownloader != null ) {
            if( mDownloader.getStatus().equals(AsyncDownloader.Status.RUNNING) || mDownloader.getStatus().equals(AsyncDownloader.Status.PENDING) ){

                if(  !mDownloader.isCancelled()) {
                    mDownloader.cancel(true);
                }

            } else if ( mDownloader.getStatus().equals(AsyncDownloader.Status.FINISHED)  ){

            }


        } else {
            Log.d(getResources().getString(R.string.logcat_tag), getResources().getString(R.string.Message10));
        }

    }
}
