package com.priyo.apps.jonopriyo.utility;

import java.util.Comparator;

import com.priyo.apps.jonopriyo.model.Poll;

public class PollNumberComparator implements Comparator<Poll> {


    public PollNumberComparator() {
    }

    @Override
    public int compare(Poll pollA, Poll pollB) {
        if (pollA.getNumber() < pollB.getNumber())
            return -1;
        if (pollA.getNumber() > pollB.getNumber())
            return 1;
        return 0;
    }
}
