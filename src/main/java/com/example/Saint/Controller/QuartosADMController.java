package com.example.Saint.Controller;

import com.example.Saint.DTO.*;
import com.example.Saint.Entity.*;
import com.example.Saint.Service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/quartosADM")
@AllArgsConstructor
public class QuartosADMController {
    private final QuartosService quartosService;

    @PatchMapping("/atualizarNome")
    public ResponseEntity<AtualizarNomeQuartoResponse> atualizarNomeQuarto(@RequestBody AtualizarNomeQuartoDTO dto) {
        return ResponseEntity.ok().body(quartosService.atualizarNomeQuarto(dto));
    }

    @PatchMapping("/atualizarPreco")
    public ResponseEntity<AtualizarPrecoQuartoResponse> atualizarPreco(@RequestBody AtualizarPrecoQuartoDTO dto) {
        return ResponseEntity.ok().body(quartosService.atualizarPreco(dto));
    }


    @PostMapping("/registrar") // verificação e tratamento feito
    public ResponseEntity<?> registrarQuarto(@RequestBody @Valid QuartosDTO request) {
        quartosService.registrarQuarto(request.nomeQuarto(), request.numero(), request.valorDoQuarto(), request.idHotel());
        return ResponseEntity.ok().body("Quarto Registrado: Nome: " + request.nomeQuarto() +
                " Número: " + request.numero());
    }

    @DeleteMapping("/excluirRegistroQuarto/{nomeQuarto}")
    public ResponseEntity<?> excluirRegistroQuarto(@PathVariable String nomeQuarto) {
        quartosService.deletarQuartoRegistrado(nomeQuarto);
        return ResponseEntity.ok().body("Registro do quarto: "+nomeQuarto+"\n Excluído com sucesso");
    }


}
