package net.ogorkis.rest.preconditions;


import net.ogorkis.rest.exceptions.InvalidRequestException;

public final class Preconditions {

    private Preconditions() {
    }

    public static void checkPropertyNotNull(Object object, String propertyName) {
        if (object == null) {
            throw new InvalidRequestException(propertyName, "null");
        }
    }

    public static void checkPropertyNotEmpty(String str, String propertyName) {
        checkPropertyNotNull(str, propertyName);
        if ("".equals(str)) {
            throw new InvalidRequestException(propertyName, "empty");
        }
    }

}
