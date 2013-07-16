package com.priyo.apps.jonopriyo.utility;

import java.util.Comparator;

import com.priyo.apps.jonopriyo.model.Poll;

public class PollPrizeValueComparator implements Comparator<Poll> {
    
    public PollPrizeValueComparator() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public int compare(Poll pollA, Poll pollB) {
        if (pollA.getPollPrize().getValue() < pollB.getPollPrize().getValue())
            return -1;
        if (pollA.getPollPrize().getValue() > pollB.getPollPrize().getValue())
            return 1;
        return 0;
    }

}
