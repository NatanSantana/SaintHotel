package com.example.Saint.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public class QuartosOcupados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuartoOcupado;

    @Getter
    @Setter
    private Long idQuarto;

    @Getter
    @Setter
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime diaReservado;

    @Getter
    @Setter
    private LocalDateTime checkOut;

    @Getter
    @Setter
    private int diasReservados;

    @Getter
    @Setter
    private Long idUsuario;



}
