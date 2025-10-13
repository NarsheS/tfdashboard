package backend.server.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 500, message = "O limite de caracteres é de 500")
    private String description;

    @NotBlank(message = "Informe o valor")
    private Double value;

    @NotBlank(message = "Informe a Data")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
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

    public Double getValue(){
        return value;
    }

    public void setValue(Double value){
        this.value = value;
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
