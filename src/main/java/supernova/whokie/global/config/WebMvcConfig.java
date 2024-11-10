package supernova.whokie.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import supernova.whokie.global.interceptor.VisitorInterceptor;
import supernova.whokie.global.interceptor.JwtInterceptor;
import supernova.whokie.global.auth.JwtProvider;
import supernova.whokie.global.resolver.VisitorArgumentResolver;
import supernova.whokie.global.resolver.LoginAdminArgumentResolver;
import supernova.whokie.global.resolver.LoginUserArgumentResolver;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;

    @Bean
    @Order(1)
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor(jwtProvider);
    }

    @Bean
    @Order(2)
    public VisitorInterceptor ipInterceptor() {
        return new VisitorInterceptor();
    }

    @Bean
    public LoginUserArgumentResolver loginUserArgumentResolver() {
        return new LoginUserArgumentResolver();
    }

    @Bean
    public LoginAdminArgumentResolver loginAdminArgumentResolver() {
        return new LoginAdminArgumentResolver();
    }

    @Bean
    public VisitorArgumentResolver ipArgumentResolver() {
        return new VisitorArgumentResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/api/**");
        registry.addInterceptor(ipInterceptor())
                .addPathPatterns("/api/profile/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver());
        resolvers.add(ipArgumentResolver());
        resolvers.add(loginAdminArgumentResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .exposedHeaders("Authorization")
                .maxAge(3600);
    }
}
