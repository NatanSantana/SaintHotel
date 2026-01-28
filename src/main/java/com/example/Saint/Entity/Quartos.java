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
    private int numero;

    @Getter
    @Setter
    @NotNull(message = "O Quarto deve ter um valor")
    private BigDecimal valorDoQuarto;

    @Getter
    @Setter
    @NotNull(message = "Insira o ID do Hotel para registrar um quarto")
    private Long idHotel;

    @Override
    public String toString() {
        return "idQuarto: "+idQuarto+" nomeQuarto: "+nomeQuarto+" numero: "+numero+" valorDoQuarto: "+valorDoQuarto+" idHotel: "+idHotel;
    }

}
