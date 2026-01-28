package com.example.Saint.DTO;

import com.example.Saint.Role.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
public class UsuariosDTO {

    @NotBlank(message = "Campo nome não deve estar vázio")
    private String nome;

    @NotBlank(message = "Campo senha não deve estar vázio")
    private String senha;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "Campo dataNascimento não deve estar vázio")
    private LocalDate dataNascimento;

    @NotBlank(message = "Campo cpf não deve estar vázio")
    @CPF
    private String cpf;

    @NotBlank(message = "Campo email não deve estar vázio")
    @Email(message = "Email inválido")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$",
            message = "O email deve estar no formato correto: @gmail.com"
    )
    private String email;

    @NotNull(message = "Campo role não deve estar vázio")
    private UserRole role;
}
