package com.example.Saint.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CheckOutDTO {

    @Getter
    @Setter
    private String cpf;

    @Getter
    @Setter
    private Long idUsuario;

    @Getter
    @Setter
    private String nomeQuarto;

   @Setter
   private LocalDateTime horaCheckOut;

   @Getter
   @Setter
   @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
   private LocalDateTime diaReservado;

   @Getter
   @Setter
   private Long idHotel;

}
