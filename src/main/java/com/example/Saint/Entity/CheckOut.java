package com.example.Saint.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public class CheckOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCheckOut;

    @Getter
    @Setter
    private Long idUsuario;

    @Getter
    @Setter
    private String cpf;

    @Setter
    private LocalDateTime dataHoraCheckOut;

    @Getter
    @Setter
    private Long idQuarto;

    @Getter
    @Setter
    private Long idHotel;



}
