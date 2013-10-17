package net.ogorkis.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import net.ogorkis.data.AppUserRepo;
import net.ogorkis.model.AppUser;

import org.slf4j.Logger;

@Path("/auth")
public class AuthenticationRestService {

	@Inject
	private Logger logger;

	@Inject
	private AppUserRepo appUserRepo;

	@POST
	@Path("/login")
	@Produces(APPLICATION_JSON)
	public AppUser login(@QueryParam("username") String username,
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

			return appUserRepo.findAppUser(username);
		} catch (ServletException e) {
			e.printStackTrace();
			logger.info("logging username: {} - failed", username);

			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
		}

		return null;
	}

	@POST
	@Path("/getAppUser")
	public AppUser getAppUser(@Context HttpServletRequest httpRequest) {
		HttpSession session = httpRequest.getSession(false);
		if (session == null) {
			return null;
		}

		String username = (String) session.getAttribute("username");

		return appUserRepo.findAppUser(username);
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

		httpResponse.sendRedirect(httpRequest.getContextPath() + "/");
	}

}
