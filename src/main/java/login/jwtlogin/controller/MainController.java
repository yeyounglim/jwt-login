package login.jwtlogin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/api/main")
    public String mainPage() {
        return "main page";
    }
}
