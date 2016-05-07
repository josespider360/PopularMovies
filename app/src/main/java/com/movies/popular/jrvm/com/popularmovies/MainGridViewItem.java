package com.movies.popular.jrvm.com.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;


public class MainGridViewItem implements Parcelable {

    private String dMTitle;
    private String dMPoster;
    private String dMSynopsis;
    private double dMRating;
    private String dMReleaseDate;

    public MainGridViewItem(String dMTitle, String dMPoster, String dMSynopsis, double dMRating, String dMReleaseDate) {
        this.dMTitle = dMTitle;
        this.dMPoster = dMPoster;
        this.dMSynopsis = dMSynopsis;
        this.dMRating = dMRating;
        this.dMReleaseDate = dMReleaseDate;
    }

    protected MainGridViewItem(Parcel in) {
        dMTitle = in.readString();
        dMPoster = in.readString();
        dMSynopsis = in.readString();
        dMRating = in.readDouble();
        dMReleaseDate = in.readString();
    }

    public static final Creator<MainGridViewItem> CREATOR = new Creator<MainGridViewItem>() {
        @Override
        public MainGridViewItem createFromParcel(Parcel in) {
            return new MainGridViewItem(in);
        }

        @Override
        public MainGridViewItem[] newArray(int size) {
            return new MainGridViewItem[size];
        }
    };

    public String getdMTitle() {
        return dMTitle;
    }

    public void setdMTitle(String dMTitle) {
        this.dMTitle = dMTitle;
    }

    public String getdMPoster() {
        return dMPoster;
    }

    public void setdMPoster(String dMPoster) {
        this.dMPoster = dMPoster;
    }

    public String getdMSynopsis() {
        return dMSynopsis;
    }

    public void setdMSynopsis(String dMSynopsis) {
        this.dMSynopsis = dMSynopsis;
    }

    public double getdMRating() {
        return dMRating;
    }

    public void setdMRating(double dMRating) {
        this.dMRating = dMRating;
    }

    public String getdMReleaseDate() {
        return dMReleaseDate;
    }

    public void setdMReleaseDate(String dMReleaseDate) {
        this.dMReleaseDate = dMReleaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dMTitle);
        dest.writeString(dMPoster);
        dest.writeString(dMSynopsis);
        dest.writeDouble(dMRating);
        dest.writeString(dMReleaseDate);
    }
}
