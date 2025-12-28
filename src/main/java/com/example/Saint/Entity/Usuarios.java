package com.example.Saint.Entity;

import com.example.Saint.Role.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Usuarios {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Getter
    @Setter
    private String nome;

    @Getter
    @Setter
    @NotBlank(message = "A senha não pode ser nula")
    @Size(min = 6, message = ("A senha deve ter no mínimo 6 caracteres"))
    private String senha;

    @Getter
    @Setter
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @Getter
    @Setter
    @NotBlank(message = "Preencha o cpf")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve ter 11 DÍGITOS NUMÉRICOS")
    private String cpf;

    @Getter
    @Setter
    private UserRole role;


}
