package backend.server.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping // Retorna todas as transações do usuário
    public ResponseEntity<List<Transaction>> listAll(Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        List<Transaction> transactions = transactionRepo.findByUser(user);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping // Adiciona uma transação
    public ResponseEntity<Transaction> add(@Valid @RequestBody Transaction transaction, Authentication auth) {
        String email = auth.getName();
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        transaction.setUser(user);
        Transaction saved = transactionRepo.save(transaction);
        return ResponseEntity.status(201).body(saved);
    }

    @DeleteMapping("/{id}") // Delete uma transação (/transactions/{id})
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id, Authentication auth){
        String email = auth.getName();
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        
        Transaction transaction = transactionRepo.findById(id)
            .orElse(null);
        
        if(transaction == null || !transaction.getUser().getId().equals(user.getId())){
            return ResponseEntity.status(404).body("Transação não encontrada ou não pertence ao usuário");
        }

        transactionRepo.delete(transaction);
        return ResponseEntity.noContent().build(); // HTTP 204
    }

    @PutMapping("/{id}") // Atualiza uma transação
    public ResponseEntity<?> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody Transaction updatedTransaction,
            Authentication auth) {

        // 1. Pega o e-mail do usuário logado a partir do token
        String email = auth.getName();

        // 2. Busca o usuário
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // 3. Busca a transação pelo ID
        Transaction transaction = transactionRepo.findById(id).orElse(null);

        // 4. Verifica se existe
        if (transaction == null) {
            return ResponseEntity.status(404).body("Transação não encontrada");
        }

        // 5. Verifica se pertence ao usuário autenticado
        if (!transaction.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Você não tem permissão para alterar esta transação");
        }

        // 6. Atualiza os campos permitidos
        transaction.setDescription(updatedTransaction.getDescription());
        transaction.setAmount(updatedTransaction.getAmount());
        transaction.setDate(updatedTransaction.getDate());

        // 7. Salva no banco
        Transaction saved = transactionRepo.save(transaction);

        // 8. Retorna o objeto atualizado
        return ResponseEntity.ok(saved);
    }

}
