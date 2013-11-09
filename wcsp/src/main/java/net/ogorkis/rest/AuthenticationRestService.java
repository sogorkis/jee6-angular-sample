package net.ogorkis.rest;

import net.ogorkis.data.UserRepo;
import net.ogorkis.jee6utils.servlet.HttpServletUtils;
import net.ogorkis.model.User;
import net.ogorkis.rest.exceptions.NotAuthorizedException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.IOException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static net.ogorkis.rest.preconditions.Preconditions.checkPropertyNotEmpty;

@Path("/auth")
public class AuthenticationRestService {

    @Inject
    private Logger logger;

    @Inject
    private UserRepo userRepo;

    @POST
    @Path("/login")
    @Produces(APPLICATION_JSON)
    public User login(@QueryParam("username") String username,
                      @QueryParam("password") String password,
                      @Context HttpServletRequest httpRequest) throws IOException {
        checkPropertyNotEmpty(username, "username");
        checkPropertyNotEmpty(password, "password");

        logger.info("logging username: {}, password: ***", username);

        try {
            boolean userAuthenticated = httpRequest.getRemoteUser() != null;
            if (userAuthenticated) {
                return userRepo.findByEmail(username);
            }

            // force creation of JSESSIONID cookie
            httpRequest.getSession();

            httpRequest.login(username, password);

            logger.info("logging username: {} - successful", username);

            return userRepo.findByEmail(username);
        } catch (ServletException e) {
            logger.info("logging username: {} - failed", username);

            throw new NotAuthorizedException(e.getMessage());
        }
    }

    @GET
    @Path("/getAppUser")
    public User getAppUser(@Context HttpServletRequest httpRequest) {
        boolean userAuthenticated = httpRequest.getRemoteUser() != null;
        if (userAuthenticated) {

            logger.info("Returning username: {} by JSESSIONID cookie", httpRequest.getRemoteUser());

            return userRepo.findByEmail(httpRequest.getRemoteUser());
        }

        if (HttpServletUtils.isCookieSet(httpRequest, "REMEMBERME")) {
            // TODO
        }

        return null;
    }

    @POST
    @Path("/logout")
    public void logout(@Context HttpServletRequest httpRequest,
                       @Context HttpServletResponse httpResponse)
            throws IOException {
        httpResponse.setHeader("Cache-Control", "no-cache, no-store");
        httpResponse.setHeader("Pragma", "no-cache");

        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            String username = (String) session.getAttribute("username");

            logger.info("logging out username: {}, password: ***", username);

            session.invalidate();
        }
    }

}
