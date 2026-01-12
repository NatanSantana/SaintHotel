package com.example.Saint.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public class RedeSaintHotels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHotel;

    @Getter
    @Setter
    private String cidade;

    @Getter
    @Setter

    private String bairro;

    @Getter
    @Setter
    private String cep;

    @Getter
    @Setter
    private LocalDateTime inauguracao;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String telefone;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String email;




}
