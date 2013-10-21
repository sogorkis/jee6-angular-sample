package net.ogorkis.data;

import com.google.common.collect.ImmutableSet;
import net.ogorkis.model.User;
import net.ogorkis.model.UserRole;
import net.ogorkis.test.IntegrationTestBase;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static junit.framework.Assert.*;

@RunWith(Arquillian.class)
public class UserRepoTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return IntegrationTestBase.createTestArchiveWithPersistence()
                .addClasses(User.class, UserRole.class, UserRepo.class);
    }

    @Inject
    private UserRepo userRepo;

    @Inject
    private EntityManager em;

    private DateTime expirationDateTime = DateTime.now(DateTimeZone.UTC);

    @Test
    public void testSave() throws Exception {
        String email = "test@example.com";
        User user = createUser(email, null);

        user = userRepo.save(user);

        assertNotNull(user.getId());
    }

    @Test
    public void testFind() throws Exception {
        String email = "test2@example.com";
        User user = createUser(email, null);

        user = userRepo.save(user);

        User foundUser = userRepo.find(user.getId());

        assertNotNull(foundUser);
        assertEquals(user, foundUser);
        assertNotSame(user, foundUser);
    }

    @Test
    public void testNativePrincipalAndRolesQueries() {
        String email = "test3@example.com";
        String rememberMeToken = "rememberMeToken";
        User user = createUser(email, rememberMeToken);
        user.setRoles(ImmutableSet.of(new UserRole("admin"), new UserRole("user")));

        userRepo.save(user);

        String principalsQuery = "SELECT password_hash, password_salt, remember_me_expiration FROM users WHERE email = ?";
        List<Object[]> principalResults = em.createNativeQuery(principalsQuery)
                .setParameter(1, email)
                .getResultList();

        assertNotNull(principalResults);
        assertEquals(1, principalResults.size());

        Object[] row = principalResults.get(0);

        assertEquals("passwordHash", row[0]);
        assertEquals("passwordSalt", row[1]);

        String principalsRememberMeQuery = "SELECT password_hash, password_salt, remember_me_expiration FROM users WHERE remember_me_token = ?";
        List<Object[]> principalRememberMeResults = em.createNativeQuery(principalsRememberMeQuery)
                .setParameter(1, rememberMeToken)
                .getResultList();

        assertNotNull(principalRememberMeResults);
        assertEquals(1, principalRememberMeResults.size());

        row = principalRememberMeResults.get(0);

        assertEquals("passwordHash", row[0]);
        assertEquals("passwordSalt", row[1]);

        String rolesQuery = "SELECT ur.name, 'Roles' FROM user_roles ur INNER JOIN users u ON ur.user_id = u.id WHERE u.email =?";
        List<Object[]> rolesResults = em.createNativeQuery(rolesQuery)
                .setParameter(1, email)
                .getResultList();

        assertNotNull(rolesResults);
        assertEquals(2, rolesResults.size());

        int adminRoleIndex = "admin".equals(rolesResults.get(0)[0]) ? 0 : 1;
        Object[] adminRoleRow = rolesResults.get(adminRoleIndex);
        Object[] userRoleRow = rolesResults.get(adminRoleIndex + 1 % 2);

        assertEquals("admin", adminRoleRow[0]);
        assertEquals("Roles", adminRoleRow[1]);
        assertEquals("user", userRoleRow[0]);
        assertEquals("Roles", userRoleRow[1]);
    }

    private User createUser(String email, String rememberMeToken) {
        User user = new User();
        user.setEmail(email);
        user.setName("sampleName");
        user.setPasswordHash("passwordHash");
        user.setPasswordSalt("passwordSalt");
        user.setRememberMeToken(rememberMeToken);
        user.setRememberMeExpiration(expirationDateTime);
        return user;
    }

}
