package backend.server.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.server.model.Transaction;
import backend.server.model.User;
import backend.server.repository.TransactionRepository;
import backend.server.repository.UserRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepo;
    private final UserRepository userRepo;

    public TransactionController(TransactionRepository transactionRepo, UserRepository userRepo) {
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> listAll(Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        List<Transaction> transactions = transactionRepo.findByUser(user);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<Transaction> add(@Valid @RequestBody Transaction transaction, Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        transaction.setUser(user);
        Transaction saved = transactionRepo.save(transaction);
        return ResponseEntity.status(201).body(saved);
    }
}
