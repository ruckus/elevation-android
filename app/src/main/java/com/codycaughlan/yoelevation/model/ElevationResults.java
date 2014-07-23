package com.codycaughlan.yoelevation.model;

import java.util.ArrayList;

public class ElevationResults {
    public String status;
    public ArrayList<ElevationResult> results;

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("status=").append(status);
        for(ElevationResult e : results) {
            buf.append("\n").append("elevation=").append(e.elevation);
        }

        return buf.toString();
    }
}
