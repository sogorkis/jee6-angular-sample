package net.ogorkis.jee6utils.jndi;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public final class JNDIUtils {

    private JNDIUtils() {
    }

    public static <T> T lookup(String name) {
        try {
            InitialContext ctx = new InitialContext();
            return (T) ctx.lookup(name);
        } catch (NamingException e) {
            throw new IllegalArgumentException("Could not lookup jndi resource " + name, e);
        }
    }
}
