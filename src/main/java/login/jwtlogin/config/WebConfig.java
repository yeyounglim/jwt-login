package login.jwtlogin.config;

import login.jwtlogin.interceptor.LoginInterceptor;
import login.jwtlogin.resolver.UserAuthArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    private final UserAuthArgumentResolver userAuthArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/account/**")
                .addPathPatterns("/api/admin/**")
                .addPathPatterns("/api/user")
        ;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userAuthArgumentResolver);
    }

    @Bean
    public PasswordEncoder pwdEncoder() {
        return new BCryptPasswordEncoder();
    }

}
