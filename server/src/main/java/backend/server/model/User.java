package backend.server.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Cria o DB
@Entity
@Table(name = "users")
public class User {
    // Classe com ID, nome, email e senha.

    @Id // Definindo identificador
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gerar numero automaticamente
    private Long id;

    @NotNull(message = "Informar nome")
    @Size(max = 100, message = "Seu nome não pode ser maior que 100 caracteres")
    private String name;

    @Column(unique = true)
    @Email
    @NotNull(message = "Cadastre o Email")
    private String email;

    @NotNull(message = "Cadastre a Senha")
    @Size(min = 8, max = 50, message = "A senha deve ter no mínimo 8 caracteres")
    private String password;

    // this final List can give headache later
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Transaction> transactions = new ArrayList<>();

    // Getters e Setters
    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
        transaction.setUser(this);
    }

    public void removeTransaction(Transaction transaction){
        transactions.remove(transaction);
        transaction.setUser(null);
    }

}
