package com.tomaytotomato.model;

import java.util.List;

public class ScoredLocation implements Comparable<ScoredLocation> {
    List<Location> locations;
    int score;

    public ScoredLocation(List<Location> locations, int score) {
        this.locations = locations;
        this.score = score;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(ScoredLocation other) {
        return Integer.compare(this.score, other.score);
    }
}