package supernova.whokie.global.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import supernova.whokie.global.annotation.AdminAuthenticate;
import supernova.whokie.global.exception.AuthenticationException;
import supernova.whokie.global.exception.ForbiddenException;
import supernova.whokie.user.Role;

public class LoginAdminArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AdminAuthenticate.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String userId = (String) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        if (userId == null) {
            throw new AuthenticationException("로그인 후 이용해 주세요.");
        }
        if (!role.equals(Role.ADMIN.toString())) {
            throw new ForbiddenException("관리자만 이용할 수 있습니다.");
        }
        return Long.parseLong(userId);
    }
}
