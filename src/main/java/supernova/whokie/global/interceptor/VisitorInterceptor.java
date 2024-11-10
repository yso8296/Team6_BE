package supernova.whokie.global.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

public class VisitorInterceptor implements HandlerInterceptor {

    private static final String COOKIE_NAME = "visitorUUID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();
        String cookieUuid = null;
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(COOKIE_NAME)) {
                    cookieUuid = cookie.getValue();
                }
            }
        }

        if(cookieUuid == null) {
            cookieUuid = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(COOKIE_NAME, cookieUuid);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        request.setAttribute("visitorUuid", cookieUuid);
        return true;
    }
}
