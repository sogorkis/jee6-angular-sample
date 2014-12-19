package net.ogorkis.rest.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NotAuthorizedException extends WebApplicationException {

    public NotAuthorizedException(String message) {
        super(Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ExceptionJsonResponse("authorization", message))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build());
    }
}
