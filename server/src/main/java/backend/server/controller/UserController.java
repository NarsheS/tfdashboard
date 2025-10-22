package backend.server.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/users") // users endpoint
public class UserController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserController(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping // GET - retorna todos os usuários e informações - remover depois!!!
    public List<UserView> getAllUsers() {
        return userRepo.findAllProjectedBy();
    }

    @PostMapping("/register") // POST - registrar usuário
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // Criptografa e define senha do usuário
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // Tenta salvar o usuário com senha criptografada
            return ResponseEntity.ok(userRepo.save(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }


    @PostMapping("/login") // POST - logar usuário
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> found = userRepo.findByEmail(user.getEmail());

        // Se achar o email de usuário fornecido
        if (found.isPresent()) {
            String rawPassword = user.getPassword(); // senha fornecida no login
            String hashedPassword = found.get().getPassword(); // senha criptografada do DB

            // Se forem compativéis gera um token
            if (passwordEncoder.matches(rawPassword, hashedPassword)) {
                String token = jwtUtil.generateToken(found.get().getEmail());
                return ResponseEntity.ok(Collections.singletonMap("token", token));
            }
        }
        // Se não o usuário não existir retorna 401
        return ResponseEntity.status(401).body("E-mail ou senha incorretos!");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccount(Authentication auth){
        String email = auth.getName(); // Nosso nome é o email

        Optional<User> userOpt = userRepo.findByEmail(email);
        if(userOpt.isEmpty()){
            return ResponseEntity.status(404).body("Usuário não encontrado!");
        }

        userRepo.delete(userOpt.get());
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
