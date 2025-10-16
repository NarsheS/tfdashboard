package backend.server.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.server.auth.token.JwtUtil;
import backend.server.model.User;
import backend.server.model.UserView;
import backend.server.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserController(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<UserView> getAllUsers() {
        return userRepo.findAllProjectedBy();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return ResponseEntity.ok(userRepo.save(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> found = userRepo.findByEmail(user.getEmail());

        if (found.isPresent()) {
            String rawPassword = user.getPassword();
            String hashedPassword = found.get().getPassword();

            if (passwordEncoder.matches(rawPassword, hashedPassword)) {
                String token = jwtUtil.generateToken(found.get().getEmail());
                return ResponseEntity.ok(Collections.singletonMap("token", token));
            }
        }

        return ResponseEntity.status(401).body("E-mail ou senha incorretos!");
    }
}
