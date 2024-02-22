package login.jwtlogin.controller;

import login.jwtlogin.annotation.UserInfo;
import login.jwtlogin.model.Users;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/api/admin")
    public String admin(@UserInfo Users userInfo) {

        return userInfo.getName();
    }

}
