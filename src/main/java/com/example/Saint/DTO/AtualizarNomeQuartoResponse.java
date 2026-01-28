package com.example.Saint.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AtualizarNomeQuartoResponse {

    private String nomeQuartoAntigo;

    private Long idQuarto;

    private String nomeNovo;

}
