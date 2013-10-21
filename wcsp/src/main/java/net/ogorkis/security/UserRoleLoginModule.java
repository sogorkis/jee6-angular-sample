package net.ogorkis.security;

import net.ogorkis.util.HttpServletUtils;
import net.ogorkis.util.JNDIUtils;
import org.jboss.security.ErrorCodes;
import org.jboss.security.auth.spi.DatabaseServerLoginModule;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class UserRoleLoginModule extends DatabaseServerLoginModule {

    private String passwordHash;
    private String passwordSalt;
    private String httpRememberMeCookieValue;

    private String hashAlgorithm;
    private String rememberMeCookieName;
    private String principalsRememberMeQuery;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        if (options.containsKey("hashStorePassword")) {
            throw new RuntimeException("hashStorePassword option usupported");
        }

        principalsRememberMeQuery = (String) options.get("principalsRememberMeQuery");
        rememberMeCookieName = (String) options.get("rememberMeCookieName");

        if ((principalsRememberMeQuery != null) ^ (rememberMeCookieName != null)) {
            throw new RuntimeException("Both principalsRememberMeQuery and rememberMeCookieName must be set or unset");
        }

        hashAlgorithm = (String) options.get("hashAlgorithm");

        super.initialize(subject, callbackHandler, sharedState, options);
    }

    @Override
    protected String getUsersPassword() throws LoginException {
        return passwordHash;
    }

    @Override
    protected String createPasswordHash(String username, String password, String digestOption) throws LoginException {
        trace = log.isTraceEnabled();

        boolean useRememberMeCookie = principalsRememberMeQuery != null && password == null;

        if (useRememberMeCookie) {
            loadHttpRememberMeCookieValue();
        }

        loadPrincipalData(useRememberMeCookie);

        if (!useRememberMeCookie) {
            try {
                // salt password
                password = password + passwordSalt;

                return super.createPasswordHash(username, password, digestOption);
            } catch (Exception e) {
                throw translateToLoginException("Failed to create password hash", e);
            }
        }

        return passwordHash;
    }

    private void loadHttpRememberMeCookieValue() throws LoginException {
        try {
            HttpServletRequest request = (HttpServletRequest) PolicyContext.getContext("javax.servlet.http.HttpServletRequest");

            Cookie rememberMeCookie = HttpServletUtils.getCookie(request, rememberMeCookieName);

            if (rememberMeCookie != null) {
                httpRememberMeCookieValue = rememberMeCookie.getValue();
            }
        } catch (PolicyContextException e) {
            throw translateToLoginException("Could not find HttpServletRequest in context", e);
        }
    }

    private void loadPrincipalData(boolean useRememberMeCookie) throws LoginException {
        Transaction tx = suspendTransaction();
        DataSource ds = lookupDataSource();

        String query = useRememberMeCookie ? principalsRememberMeQuery : principalsQuery;
        String queryParam = useRememberMeCookie ? httpRememberMeCookieValue : getUsername();

        try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            if (log.isTraceEnabled()) {
                log.trace("Executing query: " + query + ", with username: " + queryParam);
            }

            ps.setString(1, queryParam);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    if (trace) {
                        log.trace("Query returned no matches from db");
                    }
                    throw new FailedLoginException(ErrorCodes.PROCESSING_FAILED + "No matching username found in Principals");
                }

                Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

                passwordHash = rs.getString(1);
                passwordSalt = rs.getString(2);
                Date rememberMeExpiration = rs.getDate(3, utcCalendar);

                if (useRememberMeCookie && utcCalendar.getTime().after(rememberMeExpiration)) {
                    throw new LoginException("rememberMeCookie expired");
                }

                if (trace) {
                    log.trace("Obtained user password");
                }
            }
        } catch (SQLException ex) {
            throw translateToLoginException("Query failed", ex);
        } finally {
            resumeTransaction(tx);
        }
    }

    private DataSource lookupDataSource() throws LoginException {
        try {
            return JNDIUtils.lookup(dsJndiName);
        } catch (IllegalArgumentException e) {
            throw translateToLoginException("Error looking up DataSource from: " + dsJndiName, e);
        }
    }

    private LoginException translateToLoginException(String message, Exception ex) {
        LoginException le = new LoginException(ErrorCodes.PROCESSING_FAILED + message);
        le.initCause(ex);
        return le;
    }

    private Transaction suspendTransaction() {
        Transaction tx = null;
        if (suspendResume) {
            try {
                if (tm == null)
                    throw new IllegalStateException(ErrorCodes.NULL_VALUE + "Transaction Manager is null");
                tx = tm.suspend();
            } catch (SystemException e) {
                throw new RuntimeException(e);
            }
            if (trace)
                log.trace("suspendAnyTransaction");
        }
        return tx;
    }

    private void resumeTransaction(Transaction tx) {
        if (suspendResume) {
            try {
                tm.resume(tx);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (trace)
                log.trace("resumeAnyTransaction");
        }
    }

}
