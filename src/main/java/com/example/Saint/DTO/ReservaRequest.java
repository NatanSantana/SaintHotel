package com.example.Saint.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReservaRequest {

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @Getter
    @Setter
    private LocalDateTime dia;

    @Getter
    @Setter
    private String nomeQuarto;

    @Getter
    @Setter
    private int diasNoHotel;

    @Getter
    @Setter
    private String cpf;

}
