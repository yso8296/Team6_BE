package supernova.whokie.global.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import supernova.whokie.global.annotation.Authenticate;
import supernova.whokie.global.exception.AuthenticationException;
import supernova.whokie.global.exception.RequireAdditionalDataException;
import supernova.whokie.user.Role;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authenticate.class);
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
        if (role.equals(Role.TEMP.toString())) {
            throw new RequireAdditionalDataException("회원가입 절차를 마무리해주세요.");
        }

        return Long.parseLong(userId);
    }
}
