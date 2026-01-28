package com.example.Saint.DTO;

import com.mercadopago.resources.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RedeSaintHotelsDTO(@NotBlank(message = "Campo Obrigatório não preenchido: cidade") String cidade,
                                 @NotBlank(message = "Campo Obrigatório não preenchido: bairro") String bairro,
                                 @NotBlank(message = "Campo Obrigatório não preenchido: cep") String cep,
                                 LocalDateTime inauguracao,
                                 @NotBlank(message = "Campo Obrigatório não preenchido: telefone") String telefone,
                                 @NotBlank(message = "Campo Obrigatório não preenchido: email") String email) {
}
