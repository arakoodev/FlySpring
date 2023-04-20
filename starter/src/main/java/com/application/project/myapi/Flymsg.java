/**
 * A class that handles requests using Flymsg API.
 */
package com.application.project.myapi;

import com.flyspring.autoroute.FlyRequest;
import com.flyspring.autoroute.annotations.PathVariableAnnotation;

public class Flymsg {
    /**
     * Returns the value of the path variable "msg" in the request.
     *
     * @param request The FlyRequest object that contains the request information.
     * @return A string that represents the value of the path variable "msg".
     */
    @PathVariableAnnotation(name = {"{msg}"})
    public String flyget(FlyRequest request) {
        return request.getPathVariable("msg");
    }

}
