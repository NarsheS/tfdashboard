package backend.server.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Definindo entidade + criação de tabela
@Entity 
@Table(name = "transactions")
public class Transaction {

    @Id // Definindo ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Geração automática + auto-increment
    private Long id;

    @Size(max = 500, message = "O limite de caracteres é de 500")
    private String description;

    @NotNull(message = "Informe o valor")
    private BigDecimal amount;

    @NotNull(message = "Informe a Data")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @com.fasterxml.jackson.annotation.JsonBackReference // Corta o loop entre tabelas (tem um em User)
    private User user;

    // Getters e Setters
    public Long getId(){
        return id;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public BigDecimal getAmount(){
        return amount;
    }

    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }

    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }
}
