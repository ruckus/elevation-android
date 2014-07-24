package com.codycaughlan.yoelevation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Geometry implements Parcelable{
    public Location location;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(location, flags);
    }

    public static final Parcelable.Creator<Geometry> CREATOR
            = new Parcelable.Creator<Geometry>() {
        public Geometry createFromParcel(Parcel in) {
            return new Geometry(in);
        }

        public Geometry[] newArray(int size) {
            return new Geometry[size];
        }
    };

    private Geometry(Parcel in) {
        this.location = in.readParcelable(Geometry.class.getClassLoader());
    }
}
