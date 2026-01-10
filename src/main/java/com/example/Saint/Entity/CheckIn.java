package com.example.Saint.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCheckIn;

    @Getter
    @Setter
    private String cpf;

    @Getter
    @Setter
    private Long idQuarto;

    @Getter
    @Setter
    private LocalDateTime dataHoraCheckIn;

    @Getter
    @Setter
    private LocalDateTime diaReservado;



}
