package com.codycaughlan.yoelevation.event;

import com.codycaughlan.yoelevation.model.ElevationResult;
import com.codycaughlan.yoelevation.model.ElevationResults;


public class ElevateEvent {
    public ElevationResults result;
    public String requestId;

    public ElevateEvent(String requestId, ElevationResults result){
        this.requestId = requestId;
        this.result = result;
    }

    public ElevationResult getPrimaryEntry() {
        if(result != null && result.results.size() == 1) {
            return result.results.get(0);
        } else {
            return null;
        }
    }
}
