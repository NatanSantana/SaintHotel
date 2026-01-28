package com.example.Saint.DTO;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AtualizarPrecoQuartoDTO(@Positive(message = "O valor deve ser positivo") BigDecimal valor, Long idQuarto) {
}
