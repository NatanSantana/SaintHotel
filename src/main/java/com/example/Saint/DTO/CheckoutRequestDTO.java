package com.example.Saint.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Data
public class CheckoutRequestDTO {

    @Getter
    @Setter
    private String referenceId;

    @Getter
    @Setter
    private BigDecimal valor;

    @Getter
    @Setter
    private String nomeCliente;

    @Getter
    @Setter
    private String emailCliente;
    }

