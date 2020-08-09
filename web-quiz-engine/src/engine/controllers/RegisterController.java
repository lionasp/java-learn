package engine.controllers;

import engine.models.User;
import engine.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RegisterController() {
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public class IncorrectUserData extends RuntimeException {

    }

    @PostMapping(value = "/api/register", consumes = "application/json")
    public void register(@RequestBody User user) {
        boolean emptyEmail = "".equals(user.getEmail()) || user.getEmail() == null;
        boolean wrongEmail = user.getEmail() == null || !user.getEmail().contains("@") || !user.getEmail().contains(".");
        boolean shortPassword = user.getPassword() == null || user.getPassword().length() < 5;
        boolean userAlreadyExists = userRepository.findByEmail(user.getEmail()).isPresent();
        if (emptyEmail || wrongEmail || shortPassword || userAlreadyExists) {
            throw new IncorrectUserData();
        }

        user.setUsername(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
