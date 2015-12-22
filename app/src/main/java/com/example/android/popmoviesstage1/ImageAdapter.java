package com.example.android.popmoviesstage1;

//import android.view.Display;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
//import android.widget.ProgressBar;

//import com.squareup.picasso.Callback;
import com.bumptech.glide.Glide;
import com.example.android.popmoviesstage1.Model.Film;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by swatir on 12/1/2015.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<Film> mFilmsList;




    //private ProgressBar progress;
    private static String mBaseURLToFetchImage = "http://image.tmdb.org/t/p/";
    private static String mImageSizeToDownload = "original";   /* \"w92\", \"w154\", \"w185\", \"w342\", \"w500\", \"w780\", or \"original\" */
    private int mImageSizeSpacing;
    private int mImageSize;

    public ImageAdapter(Context c, List<Film> FilmListParam) {
        mContext = c;
        mFilmsList = FilmListParam;
        mImageSizeSpacing = mImageSize =0;
    }

    public ImageAdapter(Context c) {
        mContext = c;
        mFilmsList = null;
        mImageSizeSpacing = mImageSize =0;
    }

    public List<Film> getFilms() {
        return mFilmsList;
    }

    public void setFilms(List<Film> FilmListParam) {

        if (mFilmsList != null) {
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



    public int getCount() {
        if (mFilmsList == null) {
            return 0;
        }
        return mFilmsList.size();
    }

    public Object getItem(int position) {
        return mFilmsList.get(position);
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


            //RelativeLayout.LayoutParams vp;
           // vp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
           // viewHolder.movieThumbnail.setLayoutParams(vp);

            viewHolder.movieThumbnail.setLayoutParams(new RelativeLayout.LayoutParams(mImageSize, mImageSize));
            viewHolder.movieThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewHolder.movieThumbnail.setPadding(mImageSizeSpacing, mImageSizeSpacing, mImageSizeSpacing, mImageSizeSpacing);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Film film = mFilmsList.get(position);
        //imageView.setImageResource(mThumbIds[position]);
        Glide.with(mContext)
                .load( mBaseURLToFetchImage + mImageSizeToDownload + film.getPosterPath())
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

    public int getmImageSize() {
        return mImageSize;
    }

    public void setImageSize(int ImageSize) {
        this.mImageSize = ImageSize;
    }

    public void setImageSizeSpacing(int ImageSize) {
        this.mImageSizeSpacing = ImageSize;}

    public void setImageSizeDetails(int ImageSize, int imageSizeSpacing) {
        this.mImageSize = ImageSize;
        this.mImageSizeSpacing = imageSizeSpacing;
    }
}
