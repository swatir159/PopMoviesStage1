package com.example.android.popmoviesstage1;

/**
 * Created by swatir on 12/2/2015.
 */

        import android.app.Activity;
        //import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        //import android.view.View;
        import android.widget.ImageView;
        import android.widget.RatingBar;
        import android.widget.TextView;

        import com.squareup.picasso.Picasso;

//import com.squareup.picasso.Picasso;

public class DisplayMovieDetailsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_view);


        // Get intent data
        Intent i = getIntent();

        // Selected image id
        int position = i.getExtras().getInt("id");
        Film film;
        film = new Film();

        ImageView imageView = (ImageView) findViewById(R.id.MovieDetailView);
       // Film film = imageAdapter.getInstance().films.get(position);
        //imageView.setImageResource(film.getPosterPath());
        Picasso.with(this)
                .load("https://image.tmdb.org/t/p/w185/" + film.getPosterPath())
                .error(R.drawable.big_problem)
                .into(imageView);

        TextView title = (TextView) findViewById(R.id.MovieTitle);
        title.setText(film.getTitle());

        RatingBar rating = (RatingBar) findViewById(R.id.UserRating);
        //float f = Float.parseFloat( film.getRating().trim() );
        rating.setRating((float)4.6);

        TextView releaseDate = (TextView) findViewById(R.id.ReleaseDate);

        releaseDate.setText(film.getFormattedDate());

        TextView overView = (TextView) findViewById(R.id.OverView);
        overView.setText(film.getOverview());




    }
}

/*
        if (position!= -1){

            Picasso.with(DisplayMovieDetailsActivity.this)
                    .load(imageAdapter.mThumbsIds[position])
                    .placeholder(R.drawable.place_holder)
                    .noFade()
                    .error(R.drawable.big_problem)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            if (progress != null) {
                                progress .setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });

        }
        else {
            Picasso.with(DisplayMovieDetailsActivity.this)
                    .load(R.drawable.big_problem)
                    .noFade()
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            if (progress != null) {
                                progress .setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });

        } */