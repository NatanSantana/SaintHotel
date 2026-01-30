package com.example.Saint.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReservaRequest {

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dia;

    @NotNull(message = "campo nomeQuarto não pode ser null")
    private String nomeQuarto;

    @NotNull(message = "campo nomeQuarto não pode ser null")
    private String nomeCliente;

    @NotNull(message = "campo nomeQuarto não pode ser null")
    private int diasNoHotel;

    @NotNull(message = "campo nomeQuarto não pode ser null")
    @CPF
    private String cpf;

    @NotNull(message = "campo nomeQuarto não pode ser null")
    @Email(message = "email inválido")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$",
            message = "O email deve estar no formato correto: @gmail.com"
    )
    private String email;

    @NotNull(message = "campo nomeQuarto não pode ser null")
    private Long idHotel;

}
