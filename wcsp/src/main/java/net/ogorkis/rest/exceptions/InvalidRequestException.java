package net.ogorkis.rest.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class InvalidRequestException extends WebApplicationException {

    public InvalidRequestException(String property, String message) {
        super(Response.status(Response.Status.BAD_REQUEST)
                .entity(new ExceptionJsonResponse(property, message))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build());
    }
}
