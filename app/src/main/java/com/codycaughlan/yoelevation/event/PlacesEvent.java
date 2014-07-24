package com.codycaughlan.yoelevation.event;

import com.codycaughlan.yoelevation.model.PlacesResults;

public class PlacesEvent {
    public PlacesResults result;

    public PlacesEvent(PlacesResults result){
        this.result = result;
    }
}
