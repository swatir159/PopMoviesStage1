package com.example.android.popmoviesstage1;

/**
 * Created by swatir on 12/3/2015.
 */

        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.ContextWrapper;
        import android.os.AsyncTask;
        import android.util.Log;

        import com.squareup.okhttp.Call;
        import com.squareup.okhttp.OkHttpClient;
        import com.squareup.okhttp.Request;
        import com.squareup.okhttp.Response;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.IOException;
        import java.net.URI;
        import java.net.URISyntaxException;
        import java.util.ArrayList;
        import java.util.List;

/**
 * Public class to be re used for downloading JSON response using library
 * OkHttp.
 *
 * We extend AsyncTask because so we can make an Asynchronous call to retrieve the
 * response from theMovieDB.org API without affecting the GUI.
 *
 * Also this class is reused in the application to reduce boiler plate code. If needed we can subclass
 * this class and override method onPostExecute() which can be used to do specific things in the
 * main thread (GUI).
 */
public class AsyncDownloader extends AsyncTask<String, Integer, String> {

    public static final String TAG = AsyncDownloader.class.getSimpleName();

    private Context mContext;
    //private Class classToLoad;
    private MainActivityFragment mMovieGridFragment;
    private List<Film> mFilms;

    private ProgressDialog mDialog;

    private String mUrl;

    public AsyncDownloader(Context ctx,MainActivityFragment movieFrg ) {
        mContext = ctx;
        //classToLoad = c;
        this.mMovieGridFragment = movieFrg;
        this.mDialog = null;
    }

    /**
     * onPreExecute runs on the UI thread before doInBackground.
     * This will start showing a small dialog that says Loading with a spinner
     * to let the user know download is in progress
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Loading...");
        mDialog.setProgressStyle(mDialog.STYLE_SPINNER);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    /**
     * doInBackground() runs in the background on a worker thread. This is where code that can block the GUI should go.
     *  Since we are using asynctask this is already in background threas we use okHttp method
     *  call.execute() which executes in current thread (which is the background threas of this Async class)
     *  Once we finish retrieving jsonData it is passed to method onPostExecute()
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(String... params) {

        String url = params[0];
        Log.d("logcat_tag", "Will go to URL: " + url + " to fetch data");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);

        Response response = null;

        String jsonData = null;

        try {
            response = call.execute();

            if (response.isSuccessful()) {
                jsonData = response.body().string();
                Log.d("logcat_tag", "Film Data found");
                response.body().close();

            } else {
                jsonData = null;
                Log.d("logcat_tag", "Film Data NOT found");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonData; //This is returned to onPostExecute()
    }

    private void getFilmsFrom(String rawJSON) {

        JSONObject results = null;
        try {


            results = new JSONObject(rawJSON);
            JSONArray data = results.getJSONArray("results");

            int dataSize = data.length();

            if (dataSize == 0) {
                //showNotFoundNotification();
                //super.onBackPressed();
            }

            mFilms = new ArrayList<>(dataSize);

            for (int i = 0; i < dataSize; i++) {
                Log.d("logcat_tag", Integer.toString(i) + "th data in Response " );
                JSONObject jsonFilm;
                jsonFilm = data.getJSONObject(i);

                Film film = new Film();

                String dateString = jsonFilm.getString("release_date");

                if (dateString != null && !dateString.equals("null")) {
                    film.setId(jsonFilm.getString("id"));
                    Log.d("logcat_tag", "id :=" + film.getId());
                    film.setTitle(jsonFilm.getString("title"));
                    Log.d("logcat_tag", "title :=" + film.getTitle());
                    film.setPosterPath(jsonFilm.getString("poster_path"));
                    Log.d("logcat_tag", "poster path :=" + film.getPosterPath());
                    film.setFormattedDate(dateString);
                    film.setRating(jsonFilm.getString("vote_average"));
                    Log.d("logcat_tag", "Popularity Rating :=" + film.getRating());
                    film.setOverview(jsonFilm.getString("overview"));
                    Log.d("logcat_tag", "overview :=" + film.getOverview());
                    Log.d("logcat_tag", " **************************************************");

                    mFilms.add(film);
                }
            }

            // Collections.sort(films, new FilmComparator());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     * onPostExecute runs on the  main (GUI) thread and receives
     * the result of doInBackground.
     *
     * Here we pass a string representation of jsonData to the child/receiver
     * activity.
     *
     * @param jsonData
     */
    @Override
    protected void onPostExecute(String jsonData) {
        super.onPostExecute(jsonData);
        if ( mDialog.isShowing() ) {
            mDialog.dismiss();
        }

        getFilmsFrom(jsonData);
        if (mFilms != null)
        {
            mMovieGridFragment.updateAdapter(mFilms);
        }
       // Intent i = new Intent(context, classToLoad);
       // i.putExtra("jsonData", jsonData);
       // context.startActivity(i);
    }

    private void hideProgress() {
        if(mDialog != null) {
            if(mDialog.isShowing()) { //check if dialog is showing.

                //get the Context object that was used to great the dialog
                Context context = ((ContextWrapper)mDialog.getContext()).getBaseContext();

                //if the Context used here was an activity AND it hasn't been finished or destroyed
                //then dismiss it
                if(context instanceof Activity) {
                    if(!((Activity)context).isFinishing() /*&& !((Activity)context).isDestroyed()  */ )
                        mDialog.dismiss();
                } else //if the Context used wasnt an Activity, then dismiss it too
                    mDialog.dismiss();
            }
            mDialog = null;
        }
    }
}
