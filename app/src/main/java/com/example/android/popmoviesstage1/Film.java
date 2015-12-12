package com.example.android.popmoviesstage1;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Model class for films of the actor's filmography
 */

/**
 * Created by swatir on 12/3/2015.
 */
public class Film {
    /*
    original title
movie poster image thumbnail
A plot synopsis (called overview in the api)
user rating (called vote_average in the api)
release date

     */



    private String id;
    private String title;
    private String posterPath;
    private String overview;
    private String rating;
    //private String role;

    private Date date;
    private String formattedDate;

    public Film() {
    }


    public String getFormattedDate() {

        if (formattedDate == null || formattedDate.equals("null")){
            return "null";
        }

        return formattedDate;
    }

    public void setDate(Date d) {
        date = d;

    }

    /**
     * This method receives a string representation as "yyyy-MM-dd" for date
     * then it converts it to type Date and sets date with setDate(date) method
     * then it sets formatted date (which is a string).
     *
     * This method is called on FilmsActivity and will be useful
     * once for sorting the list of films by Date.
     *
     * @param stringDate string representation of "yyyy-MM-dd" for date
     */
    public void setFormattedDate(String stringDate) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {

            Date unformatedDate = formatter.parse(stringDate);

            //We use setDate() method so we don't have to repeat code in filmography adapter for every film
            setDate(unformatedDate);

            formattedDate = formatter.format(unformatedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String dateInString) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {

            Date date = formatter.parse(dateInString);

            this.date = date;

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString(){
        return "id:" + this.getId() + " Title:" + this.getTitle() + " Overview"   + this.getOverview() + " Rating:" + this.getRating() + " Release Date:" + this.getFormattedDate();
    }
}

