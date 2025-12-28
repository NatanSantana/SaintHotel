package com.example.Saint.Controller;

import com.example.Saint.DTO.DeleteReservaDTO;
import com.example.Saint.DTO.ReservaRequest;
import com.example.Saint.Entity.Quartos;
import com.example.Saint.Entity.QuartosOcupados;
import com.example.Saint.Entity.Usuarios;
import com.example.Saint.Repository.QuartosOcupadosRepository;
import com.example.Saint.Repository.QuartosRepository;
import com.example.Saint.Repository.UsuariosRepository;
import com.example.Saint.Service.QuartosOcupadosService;
import com.example.Saint.Service.QuartosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/quartos")
public class QuartosController {

    @Autowired
    private QuartosService quartosService;

    @Autowired
    private QuartosRepository quartosRepository;

    @Autowired
    private QuartosOcupadosService quartosOcupadosService;

    @Autowired
    private QuartosOcupadosRepository quartosOcupadosRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;


    @PostMapping("/registrar")
    public ResponseEntity<?> registrarQuarto(@RequestBody @Valid Quartos quartos) {
        quartosService.registrarQuarto(quartos.getNomeQuarto(), quartos.getNumero());
        return ResponseEntity.ok().body("Quarto Registrado: Nome: " + quartos.getNomeQuarto() +
                " Número: " + quartos.getNumero());
    }

    @PostMapping("/reservar")
    public ResponseEntity<?> reservarQuarto(@RequestBody ReservaRequest request) {


        Optional<Quartos> quartos =
                Optional.ofNullable(quartosRepository.findByNomeQuarto(request.getNomeQuarto()));


        List<QuartosOcupados> quartoOcupado = quartosOcupadosRepository.findAllByIdQuarto(quartos.get().getIdQuarto());

        Optional<Usuarios> usuarios = usuariosRepository.findByCpf(request.getCpf());


        List<LocalDateTime> datasOcupadas = new ArrayList<>();


        for (QuartosOcupados qo : quartoOcupado) {
            for (LocalDateTime data = qo.getDiaReservado();
                 !data.isAfter(qo.getCheckOut());
                 data = data.plusDays(1)) {

                datasOcupadas.add(data);
            }
        }


        if (datasOcupadas.stream().noneMatch(data -> data.isEqual(request.getDia())) &&
                datasOcupadas.stream().noneMatch(data -> data.isEqual(request.getDia().plusDays(request.getDiasNoHotel())))) {

            quartosOcupadosService.reservarQuarto(
                    quartos.get().getIdQuarto(),
                    request.getDia(),
                    request.getDiasNoHotel(),
                    usuarios.get().getIdUsuario());

        } else {
            return ResponseEntity.ok().body("Este Quarto Já foi Reservado");
        }

        return ResponseEntity.ok().body("Quarto " + request.getNomeQuarto() + " Reservado");
    }

    @DeleteMapping("/cancelarReserva")
    public ResponseEntity<?> cancelar(@RequestBody DeleteReservaDTO deleteReservaDTO) {
        quartosOcupadosService.cancelarReserva(deleteReservaDTO);
        return ResponseEntity.ok().body("Reserva Cancelada Com Sucesso");
    }
}
