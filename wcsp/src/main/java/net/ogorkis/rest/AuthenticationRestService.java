package net.ogorkis.rest;

import com.google.common.base.Strings;
import net.ogorkis.data.UserRepo;
import net.ogorkis.exceptions.NotAuthorizedException;
import net.ogorkis.model.User;
import net.ogorkis.util.HttpServletUtils;
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
                      @Context HttpServletRequest httpRequest,
                      @Context HttpServletResponse httpResponse) throws IOException {

        logger.info("logging username: {}, password: ***", username);

        try {
            // force creation of JSESSIONID cookie
            HttpSession session = httpRequest.getSession();

            httpRequest.login(username, password);

            logger.info("logging username: {} - successful", username);

            session.setAttribute("username", username);

            return userRepo.findByEmail(username);
        } catch (ServletException e) {
            logger.info("logging username: {} - failed", username);

            throw new NotAuthorizedException(e.getMessage());
        }
    }

    @POST
    @Path("/getAppUser")
    public User getAppUser(@Context HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            String username = (String) session.getAttribute("username");

            return userRepo.findByEmail(username);
        }

        if (HttpServletUtils.isCookieSet(httpRequest, "REMEMBERME")) {
            // TODO
        }

        return  null;
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
