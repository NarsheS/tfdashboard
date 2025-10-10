package backend.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.server.model.Transaction;
import backend.server.model.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
}
