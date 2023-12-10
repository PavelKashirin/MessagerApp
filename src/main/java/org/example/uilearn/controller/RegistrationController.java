package org.example.uilearn.controller;

import lombok.RequiredArgsConstructor;
import org.example.uilearn.dao.UserRepository;
import org.example.uilearn.entity.Role;
import org.example.uilearn.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.example.uilearn.entity.Role.USER;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserRepository userRepository;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    @Transactional
    public String addUser(User user, Map<String, Object> model) {
        Optional<User> optUserFromDb = userRepository.findByUsername(user.getUsername());

        if (optUserFromDb.isPresent()) {
            model.put("message", "User exists!");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(USER));
        userRepository.save(user);

        return "redirect:/login";
    }
}
