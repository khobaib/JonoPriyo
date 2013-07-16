package com.priyo.apps.jonopriyo.utility;

import java.util.Comparator;

import com.priyo.apps.jonopriyo.model.Poll;

public class PollCategoryComparator implements Comparator<Poll> {
    
    public PollCategoryComparator() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public int compare(Poll pollA, Poll pollB) {
        return (pollA.getCategory().compareTo(pollB.getCategory()));
    }

}
