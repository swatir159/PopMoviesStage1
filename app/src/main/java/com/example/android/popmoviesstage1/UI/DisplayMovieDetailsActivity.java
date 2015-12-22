package com.example.android.popmoviesstage1.UI;

/**
 * Created by swatir on 12/2/2015.
 */

        import android.app.Activity;
        //import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        //import android.view.View;
        import android.util.DisplayMetrics;
        import android.widget.ImageView;
        import android.widget.RatingBar;
        import android.widget.TextView;

        import com.bumptech.glide.Glide;
        import com.example.android.popmoviesstage1.Model.Film;
        import com.example.android.popmoviesstage1.R;


//import com.squareup.picasso.Picasso;

public class DisplayMovieDetailsActivity extends Activity {

    private static String baseImgURL = "https://image.tmdb.org/t/p/w185/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_view);


        // Get intent data
        Intent i = getIntent();

        // Selected image id
       // int position = i.getExtras().getInt("id");
        String InstanceTagLocal =  getApplicationContext().getResources().getString(R.string.InstanceTag2);
        Film recdFilm = i.getParcelableExtra(InstanceTagLocal);

        /* decide the heights of the controls on the basis of the displayMetrics
        */

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int HeightLocal = displayMetrics.heightPixels;
        //final int WidthLocal = displayMetrics.widthPixels;

        ImageView imageView = (ImageView) findViewById(R.id.MovieDetailView);
        imageView.setMaxHeight((int)Math.round(HeightLocal * 0.30)) ;
       // Film film = imageAdapter.getInstance().films.get(position);
        //imageView.setImageResource(film.getPosterPath());
        Glide.with(this)
                .load(baseImgURL + recdFilm.getPosterPath())
                .error(R.drawable.big_problem)
                .into(imageView);

        TextView title = (TextView) findViewById(R.id.MovieTitle);
        title.setHeight((int)Math.round(HeightLocal*0.20));
        title.setText(recdFilm.getTitle());

        RatingBar rating = (RatingBar) findViewById(R.id.UserRating);
        //float f = Float.parseFloat( film.getRating().trim() );
        rating.setRating(recdFilm.getRating());

        TextView releaseDate = (TextView) findViewById(R.id.ReleaseDate);

        releaseDate.setText( getApplicationContext().getResources().getString(R.string.ReleaseDateLabel) + recdFilm.getFormattedDate());

        TextView overView = (TextView) findViewById(R.id.OverView);
        overView.setHeight((int)Math.round(HeightLocal*0.30));
        overView.setText(recdFilm.getOverview());

    }
}

