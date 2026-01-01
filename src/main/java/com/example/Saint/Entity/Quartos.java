package com.example.Saint.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
public class Quartos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long idQuarto;

    @Getter
    @Setter
    @NotBlank(message = "Insira um nome para o quarto")
    private String nomeQuarto;

    @Getter
    @Setter
    @NotNull(message = "Número é obrigatório")
    @Positive(message = "Número deve ser positivo")
    private Integer numero;

    @Getter
    @Setter
    private BigDecimal valorDoQuarto;

}
