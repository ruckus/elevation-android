package com.codycaughlan.yoelevation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PlaceResult implements Parcelable {
    public String formatted_address;
    public Geometry geometry;
    public String icon;
    public String id;
    public String name;
    public double elevation;

    public double getLat() {
        return geometry.location.lat;
    }

    public double getLng() {
        return geometry.location.lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(formatted_address);
        out.writeParcelable(geometry, flags);
        out.writeString(icon);
        out.writeString(id);
        out.writeString(name);
        out.writeDouble(elevation);
    }

    public static final Parcelable.Creator<PlaceResult> CREATOR = new Parcelable.Creator<PlaceResult>() {
        public PlaceResult createFromParcel(Parcel in) {
            return new PlaceResult(in);
        }
        public PlaceResult[] newArray(int size) {
            return new PlaceResult[size];
        }
    };

    private PlaceResult(Parcel in) {
        this.formatted_address = in.readString();
        this.geometry = in.readParcelable(PlaceResult.class.getClassLoader());
        this.icon = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.elevation = in.readDouble();
    }
}
