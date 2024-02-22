package login.jwtlogin.controller;

import login.jwtlogin.annotation.UserInfo;
import login.jwtlogin.constants.TokenConstants;
import login.jwtlogin.model.Users;
import login.jwtlogin.repository.UserRepository;
import login.jwtlogin.util.JWTTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.HttpResource;

import javax.crypto.SecretKey;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;

@RestController
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder pwdEncoder;

    @PostMapping("/api/register")
    public ResponseEntity<String> userRegister(@RequestBody Users users) {
        ResponseEntity response = null;

        try {
            String encodePwd = pwdEncoder.encode(users.getPwd());
            users.setPwd(encodePwd);
            users.setCreateDt(new Date());

            Users savedUsers = userRepository.save(users);

            if (savedUsers.getId() > 0) {
                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("registerd");
            }
        } catch (Exception e) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("register fail " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody Users users) {

        List<Users> searchUser = userRepository.findByEmail(users.getEmail());

        HttpHeaders headers = new HttpHeaders();

        if (searchUser.size() > 0) {
            Users setUsers = searchUser.get(0);
            String email = setUsers.getEmail();
            String pwd = setUsers.getPwd();

            String rawPwd = users.getPwd();

            if (pwdEncoder.matches(rawPwd, pwd)) {

                JWTTokenUtil jwtTokenUtil = new JWTTokenUtil();

                Date accTokenDate = jwtTokenUtil.getAccessTokenDate();
                Date refreshTokenDate = jwtTokenUtil.getRefreshTokenDate();

                SecretKey key = jwtTokenUtil.getSecretKey(TokenConstants.JWT_KEY);
                String jwt = jwtTokenUtil.generateToken(email, key, accTokenDate);

                key = jwtTokenUtil.getSecretKey(TokenConstants.REFRESH_JWT_KEY);
                String refreshJwt = jwtTokenUtil.generateToken(email, key, refreshTokenDate);

                headers.add(TokenConstants.JWT_HEADER, jwt);
                headers.add(TokenConstants.REFRESH_JWT_HEADER, refreshJwt);

                setUsers.setRefreshToken(refreshJwt);
                userRepository.save(setUsers);
            } else {
                throw new IllegalStateException("invalid password");
            }
        } else {
            throw new IllegalStateException("user not found");
        }
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/api/user")
    public Users getUserInfo(@UserInfo Users users) {
        return users;
    }
}
