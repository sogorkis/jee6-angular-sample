package net.ogorkis.rest;

import com.google.common.collect.ImmutableSet;
import com.google.common.hash.Hashing;
import net.ogorkis.data.UserRepo;
import net.ogorkis.model.User;
import net.ogorkis.model.UserRole;
import net.ogorkis.rest.exceptions.ExistingEmailException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.UUID;

import static net.ogorkis.rest.preconditions.Preconditions.checkPropertyNotEmpty;

@Path("/register")
public class RegistrationRestService {

    private static final Charset PASSWORD_ENCODING_HASH_CODE = Charset.forName("UTF-8");

    @Inject
    private Logger logger;

    @Inject
    private UserRepo userRepo;

//    @Resource(lookup = "java:jboss/mail/Default")
//    private Session mailSession;

    @POST
    public void register(@QueryParam("email") String email,
                         @QueryParam("username") String username,
                         @QueryParam("password") String password) {
        checkPropertyNotEmpty(email, "email");
        checkPropertyNotEmpty(username, "username");
        checkPropertyNotEmpty(password, "password");

        logger.info("Registering new user {}", email);

        User user = new User();
        user.setEmail(email);
        user.setName(username);

        String passwordSalt = generatePasswordSalt();
        String passwordHash = generatePasswordHash(password, passwordSalt);

        user.setPasswordSalt(passwordSalt);
        user.setPasswordHash(passwordHash);

        logger.info("password: {}, passwordSalt: {}, passwordHash: {}", password, passwordSalt, passwordHash);

        user.setRoles(getUserRoleSet());

        try {
            userRepo.save(user);
        } catch (Exception e) {
            // TODO: hibernate validator + exception handling
            logger.error("Error while persisting user {}", email);

            throw new ExistingEmailException("Provided email is already registered");
        }

        sendActivationEmail(user);
    }

    private String generatePasswordSalt() {
        return UUID.randomUUID().toString();
    }

    private String generatePasswordHash(String password, String salt) {
        password = password + salt;

        return Hashing.md5().hashString(password, PASSWORD_ENCODING_HASH_CODE).toString();
    }

    private Set<UserRole> getUserRoleSet() {
        UserRole userRole = new UserRole("user");
        return ImmutableSet.of(userRole);
    }

    private void sendActivationEmail(User user) {
//        TODO:
//        try {
//            MimeMessage message = new MimeMessage(mailSession);
//
//            message.setFrom(new InternetAddress(""));
//
//        } catch (MessagingException e) {
//            logger.error("MessagingException {}", e.getMessage(), e);
//        }
    }
}
