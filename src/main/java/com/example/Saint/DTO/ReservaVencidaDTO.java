package com.example.Saint.DTO;

import lombok.Getter;

import java.time.LocalDateTime;

public class ReservaVencidaDTO {

    @Getter
    private LocalDateTime checkOut;

    @Getter
    private Long idUsuario;


}
