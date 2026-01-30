package com.example.Saint.Controller;

import com.example.Saint.DTO.*;
import com.example.Saint.Entity.*;
import com.example.Saint.Repository.QuartosOcupadosRepository;
import com.example.Saint.Repository.RedeSaintHotelsRepository;
import com.example.Saint.Repository.UsuariosRepository;
import com.example.Saint.Service.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/quartos")
@AllArgsConstructor
public class QuartosController {
    private final QuartosService quartosService;
    private final QuartosOcupadosService quartosOcupadosService;
    private final QuartosOcupadosRepository quartosOcupadosRepository;
    private final UsuariosRepository usuariosRepository;
    private final CheckInService checkInService;
    private final CheckOutService checkOutService;
    private final RedeSaintHotelsRepository redeSaintHotelsRepository;


    @GetMapping("/all")
    public ResponseEntity<List<Quartos>> buscarQuartos(@RequestParam(required = false) Long idHotel) {
        if (idHotel != null) {
            List<Quartos> quartosEHotel = quartosService.listarQuartos(idHotel);
            return ResponseEntity.ok().body(quartosEHotel);
        } else {
            List<Quartos> quartos = quartosService.listarQuartos();
            return ResponseEntity.ok().body(quartos);
        }
    }

    @PostMapping("/reservar") // verificação e tratamento feito
    public ResponseEntity<?> reservarQuarto(@RequestBody @Valid ReservaRequest request) throws MPException, MPApiException {
        return quartosOcupadosService.pedirReserva(request);
    }

    @PostMapping("/checkin")
    public ResponseEntity<?> checkin(@RequestBody CheckInDTO checkInDTO) {
        checkInService.fazerCheckIn(checkInDTO);
        return ResponseEntity.ok().body("Check In Registrado");
    }


    // o que difere do checkout, é que o cancelamento da reserva é feito antes mesmo do check in
    @DeleteMapping("/cancelarReserva") // verificação e tratamento feito
    public ResponseEntity<?> cancelar(@RequestBody DeleteReservaDTO deleteReservaDTO) {
        quartosOcupadosService.cancelarReserva(deleteReservaDTO);
        return ResponseEntity.ok().body("Reserva Cancelada Com Sucesso");
    }

    @PostMapping("/checkout") // verificação e tratamento feito
    public ResponseEntity<?> checkout(@RequestBody CheckOutDTO checkOutDTO) {

        Usuarios usuarios = usuariosRepository.findByCpf(checkOutDTO.getCpf())
                .orElseThrow(() -> new RuntimeException("Não existe usuario com o CPF informado"));

        RedeSaintHotels redeSaintHotels = redeSaintHotelsRepository.findById(checkOutDTO.getIdHotel())
                .orElseThrow(() -> new RuntimeException("Nenhum hotel com encontrado com esse ID"));


        checkOutDTO.setCpf(checkOutDTO.getCpf());
        checkOutDTO.setNomeQuarto(checkOutDTO.getNomeQuarto());

        checkOutService.fazerCheckOut(checkOutDTO);

        int qo = quartosOcupadosRepository.deleteReserva(checkOutDTO.getDiaReservado(), usuarios.getIdUsuario(), checkOutDTO.getIdHotel());
        if (qo == 0) {
            return ResponseEntity.ok().body("A reserva informada não existe para realizar a operação de CheckOut");
        }

        return ResponseEntity.ok().body("CheckOut Feito Com Sucesso");
    }


}
