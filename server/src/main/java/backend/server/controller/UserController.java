package backend.server.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.server.model.User;
import backend.server.model.UserView;
import backend.server.repository.UserRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    
    @GetMapping
    public List<UserView> getAllUsers(){
        return userRepo.findAllProjectedBy();
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        return userRepo.save(user);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody User user) {
        Optional<User> found = userRepo.findByEmail(user.getEmail());

        if (found.isPresent() && found.get().getPassword().equals(user.getPassword())) {
            return "Login bem-sucedido!";
        } else {
            return "Email ou senha incorretos.";
        }
    }
}
