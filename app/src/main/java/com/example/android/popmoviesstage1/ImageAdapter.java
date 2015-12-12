package com.example.android.popmoviesstage1;

//import android.view.Display;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.GridView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
//import android.widget.ProgressBar;

//import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by swatir on 12/1/2015.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<Film> mFilms;
    //private ProgressBar progress;
    private static String mBaseURLToFetchImage = "http://image.tmdb.org/t/p/";
    private static String mImageSize = "original";   /* \"w92\", \"w154\", \"w185\", \"w342\", \"w500\", \"w780\", or \"original\" */

    public ImageAdapter(Context c, List<Film> filmsArray) {
        mContext = c;
        mFilms = filmsArray;
    }

    public ImageAdapter(Context c) {
        mContext = c;
        mFilms = null;
    }

    public List<Film> getFilms() {
        return mFilms;
    }

    public void setFilms(List<Film> filmsArray) {

        if ( mFilms != null  )
        {
            mFilms.clear();
            mFilms = null;
        }


        int dataSize = filmsArray.size();

        if (dataSize != 0) {
            mFilms = new ArrayList<Film>(dataSize);

            for (int i = 0; i < dataSize; i++) {
                mFilms.add((Film) filmsArray.get(i));
            }
        }
    }



    public int getCount() {
        if (mFilms == null) {
            return 0;
        }
        return mFilms.size();
    }

    public Object getItem(int position) {
        return mFilms.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }


    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //ProgressBar progress = null;
        /* this piece of code needs a better place to live
        if (parent != null) {
            progress = (ProgressBar) parent.findViewById(R.id.Movie_progressBar);
            progress.setVisibility(parent.VISIBLE);
        }
         this piece of code needs a better place to live */
        ///////////////////////////////////////////////////
        ViewHolder viewHolder;
        //ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            // inflate the GridView item layout
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.gridview_item, parent, false);
            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.movieThumbnail = (ImageView) convertView.findViewById(R.id.ivIcon);


            RelativeLayout.LayoutParams vp;
           // vp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
           // viewHolder.movieThumbnail.setLayoutParams(vp);
            viewHolder.movieThumbnail.setLayoutParams(new RelativeLayout.LayoutParams(85, 85));
            viewHolder.movieThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewHolder.movieThumbnail.setPadding(8, 8, 8, 8);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Film film = mFilms.get(position);
        //imageView.setImageResource(mThumbIds[position]);
        Picasso.with(mContext)
                .load( mBaseURLToFetchImage + mImageSize + film.getPosterPath())
                        .placeholder(R.drawable.place_holder)
                .error(R.drawable.big_problem)
                .into(viewHolder.movieThumbnail);
        //Picasso.with(mContext).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        return convertView;
    }


    /*
     *
     * The view holder design pattern prevents using findViewById()     * repeatedly in the getView() method of the adapter.
     *
     * @see http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder
     */
    private static class ViewHolder {
        ImageView movieThumbnail;

    }


}
