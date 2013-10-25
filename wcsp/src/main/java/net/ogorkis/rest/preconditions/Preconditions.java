package net.ogorkis.rest.preconditions;


import net.ogorkis.rest.exceptions.InvalidRequestException;

public final class Preconditions {

    private Preconditions() {
    }

    public static void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new InvalidRequestException(message);
        }
    }

    public static void checkPropertyNotNull(Object object, String propertyName) {
        if (object == null) {
            throw new InvalidRequestException("Null property " + propertyName + " passed");
        }
    }

    public static void checkPropertyNotEmpty(String str, String propertyName) {
        checkPropertyNotNull(str, propertyName);
        if ("".equals(str)) {
            throw new InvalidRequestException("Empty property " + propertyName + " passed");
        }
    }

}
