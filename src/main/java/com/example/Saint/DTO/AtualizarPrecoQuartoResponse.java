package com.example.Saint.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AtualizarPrecoQuartoResponse {

    private BigDecimal valorNovo;

    private BigDecimal valorAntigo;

    private String nomeQuarto;



}
