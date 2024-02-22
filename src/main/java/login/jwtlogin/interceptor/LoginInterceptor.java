package login.jwtlogin.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import login.jwtlogin.constants.TokenConstants;
import login.jwtlogin.model.Users;
import login.jwtlogin.repository.UserRepository;
import login.jwtlogin.util.JWTTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (null != jwt) {
            JWTTokenUtil jwtTokenUtil = new JWTTokenUtil();

            try {
                SecretKey key = jwtTokenUtil.getSecretKey(TokenConstants.JWT_KEY);
                Claims claims = jwtTokenUtil.parseToken(key, jwt);

            } catch (Exception e) {
                String refreshJwtToken = request.getHeader(TokenConstants.REFRESH_JWT_HEADER);

                if (null == refreshJwtToken) return false;

                Date accTokenDate = jwtTokenUtil.getAccessTokenDate();
                SecretKey key = null;
                String email = null;

                try {
                    key = jwtTokenUtil.getSecretKey(TokenConstants.REFRESH_JWT_KEY);
                    Claims claims = jwtTokenUtil.parseToken(key, refreshJwtToken);
                    email = jwtTokenUtil.getTokenEmail(claims);

                    Users users = userRepository.findByEmail(email).get(0);

                    if (users != null) {
                        String getRefresh = users.getRefreshToken();

                        if (getRefresh.equals(refreshJwtToken)) {

                            key = jwtTokenUtil.getSecretKey(TokenConstants.JWT_KEY);
                            String refreshAccessJwtToken = jwtTokenUtil.generateToken(email, key, accTokenDate);
                            response.setHeader(TokenConstants.JWT_HEADER, refreshAccessJwtToken);
                        }
                    }
                } catch (Exception ex) {
                    throw new RuntimeException("invalid refresh token", ex);
                }
            }
        }
        return true;
    }
}
