package com.littlepay.service;

public class FareMatrix {
    @SuppressWarnings("checkstyle:MagicNumber")
    private final double[][] fare = {//End Stop1, Stop2, Stop3
            /*Start Stop1*/  {0.00, 3.25, 7.30},
            /*Start Stop2*/  {3.25, 0.00, 5.50},
            /*Start Stop3*/  {7.30, 5.50, 0.00}
    };

    /**
     * Determine fare based on given stop
     *
     * @param fromStop Stop where trip started
     * @param toStop   Stop where trip ended
     * @return Total fare for trip
     */
    public double getFare(final int fromStop, final int toStop) {
        return fare[fromStop][toStop];
    }

    /**
     * Possible fare in case of mis off tap. If trip started at Stop2 then passenger can travel to Stop1 or Stop3.
     * possible fare is max out of two trip
     * i.e. stop2 -> stop1 is 3.25 and stop2 -> stop3 is 5.5 then possible fare is 5.5
     *
     * @param fromStop Stop where trip started
     * @return Total fare for trip
     */
    public double getPossibleFare(final int fromStop) {
        double max = 0.0;
        for (int i = 0; i < fare.length; i++) {
            if (fare[fromStop][i] > max) {
                max = fare[fromStop][i];
            }
        }
        return max;
    }
}
