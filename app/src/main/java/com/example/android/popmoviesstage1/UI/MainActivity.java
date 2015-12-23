package com.example.android.popmoviesstage1.UI;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popmoviesstage1.R;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.TaskCallbacks {

    private static final String TAG_TASK_FRAGMENT = "task_fragment";
    private MainActivityFragment mTaskFragment;
    public int mOptionSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.app.FragmentManager fm = (FragmentManager) this.getFragmentManager();
        mTaskFragment = (MainActivityFragment)  fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null) {
            Log.d(getResources().getString(R.string.logcat_tag), "No Task fragmnet exists - Creating new Task fragment");
            mTaskFragment = new MainActivityFragment();
             fm.beginTransaction().add( mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getResources().getString(R.string.SnackBarMsg), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String menuStringLocal = getResources().getString(id);

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.item1:
            case R.id.item2:
            case R.id.item3:
            case R.id.item4:
                Toast.makeText(this, menuStringLocal, Toast.LENGTH_SHORT).show();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onPreExecute() { displayToast( this.getApplicationContext(), "MainActivity ->onPreExecute"); }

    @Override
    public void onProgressUpdate(int percent) { displayToast( this.getApplicationContext(), "MainActivity ->onPreExecute"); }

    @Override
    public void onCancelled() { displayToast( this.getApplicationContext(), "MainActivity ->onPreExecute"); }

    @Override
    public void onPostExecute() { displayToast( this.getApplicationContext(), "MainActivity ->onPreExecute"); }

    private void displayToast(Context context, String text) {

        int duration = Toast.LENGTH_SHORT;
        CharSequence styledText = Html.fromHtml(text);
        Toast toast = Toast.makeText(context, styledText, duration);
        toast.show();
    }

}
