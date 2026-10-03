package com.helpdesk;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {

        var user = userRepository.findByEmailAndPassword(email, password);

        if (user.isPresent()) {

            // Save logged-in user's ID
            session.setAttribute("userId", user.get().getUser_id());

            return "dashboard";
        }

        return "login";
    }
}