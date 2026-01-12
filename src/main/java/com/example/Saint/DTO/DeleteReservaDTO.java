package com.example.Saint.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class DeleteReservaDTO {

    @Getter
    @Setter
    private String cpf;

    @Getter
    @Setter
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dia;

    @Getter
    @Setter
    private Long idHotel;

}
