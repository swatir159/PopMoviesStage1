package com.example.android.popmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * Created by swatir on 12/13/2015.
 */
public class selectedFilm implements Parcelable {
    private String mID;
    private String mTitle;
    private String mPosterPath;
    private String mOverview;
    private float mRating;
    private String mReleaseDate;

    public selectedFilm() {

    }

    public selectedFilm( String Id, String Title, String Posterpath, String Overview, float Rating, String Releasedate) {
        setID(Id);
        setTitle(Title);
        setPosterPath(Posterpath);
        setOverview(Overview);
        setRating(Rating);
        setReleaseDate(Releasedate);
    }

    public static final Parcelable.Creator<selectedFilm> CREATOR = new Creator<selectedFilm>() {
        public selectedFilm createFromParcel(Parcel source) {
            selectedFilm myFilm = new selectedFilm();
            myFilm.mID = source.readString();
            myFilm.mTitle = source.readString();
            myFilm.mPosterPath = source.readString();
            myFilm.mOverview = source.readString();
            myFilm.mRating = source.readFloat();
            myFilm.mReleaseDate = source.readString();
            return myFilm;
        }
        public selectedFilm[] newArray(int size) {
            return new selectedFilm[size];
        }
    };

    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mID);
        parcel.writeString(mTitle);
        parcel.writeString(mPosterPath);
        parcel.writeString(mOverview);
        parcel.writeFloat(mRating);
        parcel.writeString(mReleaseDate);
    }




    public String getID() {
        return mID;
    }

    public void setID(String mIDd) {
        this.mID = mIDd;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String mPposterPath) {
        this.mPosterPath = mPposterPath;
    }

    public String getOverview( ) {
        return mOverview;
    }

    public void setOverview(String mOoverview) {
        this.mOverview = mOoverview;
    }

    public float getRating() {
        return mRating;
    }

    public void setRating(float Rating) {
        this.mRating = Rating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

}
