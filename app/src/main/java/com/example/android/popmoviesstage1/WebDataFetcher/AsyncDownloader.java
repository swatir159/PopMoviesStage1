package com.example.android.popmoviesstage1.WebDataFetcher;

/**
 * Created by swatir on 12/3/2015.
 */

        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.os.AsyncTask;
        import android.util.Log;
        import android.view.Gravity;
        import android.widget.Toast;

        import com.example.android.popmoviesstage1.Model.Film;
        import com.example.android.popmoviesstage1.R;
        import com.example.android.popmoviesstage1.UI.MainActivity;
        import com.example.android.popmoviesstage1.UI.MainActivityFragment;
        import com.squareup.okhttp.Call;
        import com.squareup.okhttp.OkHttpClient;
        import com.squareup.okhttp.Request;
        import com.squareup.okhttp.Response;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.IOException;
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

    //public static final String TAG = AsyncDownloader.class.getSimpleName();

    private Context mContext;

    private MainActivityFragment mMovieGridFragment;
    private List<Film> mFilmsList;

    private ProgressDialog mDialog;

    private String mURL;

    public AsyncDownloader(Context ctx, MainActivityFragment movieFrg ) {
        mContext = ctx;
        //classToLoad = c;
        this.mMovieGridFragment = movieFrg;
        this.mDialog = null;
        this.mURL = null;

    }

    /**
     * onPreExecute runs on the UI thread before doInBackground.
     * This will start showing a small dialog that says Loading with a spinner
     * to let the user know download is in progress
     */
    @Override
    protected void onPreExecute() {
        if (mMovieGridFragment.getCallbacks() != null) {
            super.onPreExecute();

            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage(mContext.getString(R.string.Message8));
            mDialog.setProgressStyle(mDialog.STYLE_SPINNER);
            mDialog.setCancelable(true);
            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mDialog.dismiss();
                }
            });
            mDialog.show();
        }
    }


    public String getmURL() {
        return mURL;
    }

    public void setmURL(String url) {
        this.mURL = url;
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

        //String url = params[0];
        setmURL(params[0]);
        Log.d(mContext.getString(R.string.logcat_tag), mContext.getString(R.string.Message5));
        OkHttpClient client = new OkHttpClient();
        //client.setConnectTimeout(150, "ms");
        Request request = new Request.Builder()
                .url(mURL)
                .build();
        Call call = client.newCall(request);

        Response response = null;

        String jsonData = null;

        try {
            if (!isCancelled()) {
                response = call.execute();
                if (response.isSuccessful()) {
                    jsonData = response.body().string();
                    Log.d(mContext.getString(R.string.logcat_tag), mContext.getString(R.string.Message6));

                    response.body().close();
                }
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonData; //This is returned to onPostExecute()
    }

    @Override
    protected void onCancelled() {
        if (mMovieGridFragment.getCallbacks() != null) {
            super.onCancelled();
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                this.mDialog = null;
            }

            Log.d(mContext.getString(R.string.logcat_tag), mContext.getString(R.string.Message9));
            Toast toast = Toast.makeText(mMovieGridFragment.getActivity(),
                    mContext.getString(R.string.Message9), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
            //mFilmsList = null;
            //mMovieGridFragment.updateFilmList(mFilmsList);
            //mMovieGridFragment.updateAdapter(mFilmsList);
            //userOnBoardingTask=null;
        }
    }
    private void getFilmsFrom(String rawJSON) {
        JSONObject results = null;
        if (rawJSON != null && rawJSON.trim().length()>0 ) {
            try
            {
                results = new JSONObject(rawJSON);
                JSONArray data = results.getJSONArray("results");
                if (data != null )
                {
                    int dataSize = data.length();
                    if (dataSize == 0) {
                        //showNotFoundNotification();
                        //super.onBackPressed();
                    }
                    mFilmsList = new ArrayList<Film>(dataSize);
                    for (int i = 0; i < dataSize; i++) {
                        //Log.d("logcat_tag", Integer.toString(i) + "th data in Response ");
                        JSONObject jsonFilm;
                        jsonFilm = data.getJSONObject(i);
                        String DateLocal = jsonFilm.getString("release_date");
                        if (DateLocal != null && !DateLocal.equals("null"))
                        {
                            String IdLocal = jsonFilm.getString("id");
                            String TitleLocal= jsonFilm.getString("title");
                            String PosterPathLocal = jsonFilm.getString("poster_path");
                            String OverviewLocal = jsonFilm.getString("overview");
                            float RatingLocal = Float.parseFloat(jsonFilm.getString("vote_average"));
                            Film FilmLocal = new Film(IdLocal, TitleLocal, PosterPathLocal, OverviewLocal, RatingLocal,DateLocal);
                            //Log.d("logcat_tag", "The Film [" + Integer.toString(i) + "] Details :=" + FilmLocal.toString());
                            mFilmsList.add(FilmLocal);
                        }
                    }
                }
                // Collections.sort(films, new FilmComparator());
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        if (mMovieGridFragment.getCallbacks()!= null ) {
            super.onPostExecute(jsonData);

            if (jsonData != null && jsonData.length() > 0) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                    this.mDialog = null;
                }

                getFilmsFrom(jsonData);
                if (mFilmsList != null) {
                    mMovieGridFragment.updateFilmList(mFilmsList);
                    mMovieGridFragment.updateAdapter(mFilmsList);
                }
            }
        }
    }

   /* private void hideProgress() {
        if(mDialog != null) {
            if(mDialog.isShowing()) { //check if dialog is showing.

                //get the Context object that was used to great the dialog
                Context context = ((ContextWrapper)mDialog.getContext()).getBaseContext();

                //if the Context used here was an activity AND it hasn't been finished or destroyed
                //then dismiss it
                if(context instanceof Activity) {
                    if(!((Activity)context).isFinishing()) // && !((Activity)context).isDestroyed()
                        mDialog.dismiss();
                } else //if the Context used wasnt an Activity, then dismiss it too
                    mDialog.dismiss();
            }
            mDialog = null;
        }
    }
    */
}
