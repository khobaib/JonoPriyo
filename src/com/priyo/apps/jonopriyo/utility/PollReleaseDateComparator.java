package com.priyo.apps.jonopriyo.utility;

import java.util.Comparator;

import com.priyo.apps.jonopriyo.model.Poll;

public class PollReleaseDateComparator implements Comparator<Poll> {
    
    public PollReleaseDateComparator() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public int compare(Poll pollA, Poll pollB) {
        return -(pollA.getReleaseDate().compareTo(pollB.getReleaseDate()));
    }
}
