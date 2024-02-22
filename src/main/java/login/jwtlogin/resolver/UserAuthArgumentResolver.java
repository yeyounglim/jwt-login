package login.jwtlogin.resolver;

import io.jsonwebtoken.Claims;
import login.jwtlogin.annotation.UserInfo;
import login.jwtlogin.constants.TokenConstants;
import login.jwtlogin.model.Users;
import login.jwtlogin.repository.UserRepository;
import login.jwtlogin.util.JWTTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class UserAuthArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserRepository userRepository;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean users = parameter.getParameterType().equals(Users.class);
        boolean userInfo = parameter.hasParameterAnnotation(UserInfo.class);
        return userInfo && users;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String jwt = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwt == null) return null;

        JWTTokenUtil jwtTokenUtil = new JWTTokenUtil();

        SecretKey key = jwtTokenUtil.getSecretKey(TokenConstants.JWT_KEY);
        Claims claims = jwtTokenUtil.parseToken(key, jwt);
        String email = jwtTokenUtil.getTokenEmail(claims);

        Users searchUser = userRepository.findByEmail(email)
                .stream()
                .findFirst()
                .orElse(null);
        return searchUser;
    }
}
