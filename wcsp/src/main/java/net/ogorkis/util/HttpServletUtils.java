package net.ogorkis.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public final class HttpServletUtils {

    private HttpServletUtils() {
    }

    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie;
            }
        }

        return null;
    }

    public static boolean isCookieSet(HttpServletRequest request, String cookieName) {
        return getCookie(request, cookieName) != null;
    }
}
