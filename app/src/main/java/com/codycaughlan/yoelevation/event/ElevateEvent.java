package com.codycaughlan.yoelevation.event;

import com.codycaughlan.yoelevation.model.ElevationResults;


public class ElevateEvent {
    public ElevationResults result;

    public ElevateEvent(ElevationResults result){
        this.result = result;
    }
}
