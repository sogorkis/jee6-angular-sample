package net.ogorkis.util;

import org.hibernate.exception.ConstraintViolationException;

public final class ExceptionUtils {

    private ExceptionUtils() {
    }

    public static boolean isConstraintViolationCause(Exception e) {
        ConstraintViolationException cve = getConstraintViolationExceptionDownStackTrace(e);

        return cve == null ? false : true;
    }

    private static ConstraintViolationException getConstraintViolationExceptionDownStackTrace(Throwable e) {
        if (e.getClass() == ConstraintViolationException.class) {
            return (ConstraintViolationException) e;
        }

        if (e.getCause() != null) {
            return getConstraintViolationExceptionDownStackTrace(e.getCause());
        }

        return null;
    }

}
