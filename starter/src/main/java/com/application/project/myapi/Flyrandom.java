/**
 * A class that returns a random number using Flymsg API.
 */
package com.application.project.myapi;

import com.flyspring.autoroute.FlyRequest;

public class Flyrandom {
    /**
     * Returns a random number between 0 and 1.
     *
     * @param request The FlyRequest object that contains the request information.
     * @return A double value that represents a random number.
     */
    public double flyget(FlyRequest request) {
        return Math.random();
    }
}
