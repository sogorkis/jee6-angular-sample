package net.ogorkis.rest.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ExistingEmailException extends WebApplicationException {

    public ExistingEmailException() {
        super(Response.status(Response.Status.BAD_REQUEST)
                .entity(new ExceptionJsonResponse("email", "nonUniqueEmail"))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build());
    }

}
