package backend.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.server.model.Transaction;
import backend.server.model.User;
import backend.server.repository.TransactionRepository;
import backend.server.repository.UserRepository;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepo;
    private final UserRepository userRepo;

    public TransactionController(TransactionRepository transactionRepo, UserRepository userRepo) {
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("/{userId}")
    public Transaction add(@PathVariable Long userId, @RequestBody Transaction transaction) {
        User user = userRepo.findById(userId).orElseThrow();
        user.addTransaction(transaction);
        userRepo.save(user); // cascade saves transaction
        return transaction;
    }


    @GetMapping("/{userId}")
    public List<Transaction> listAll(@PathVariable Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        return transactionRepo.findByUser(user);
    }
}
