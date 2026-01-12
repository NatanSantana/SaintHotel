package com.example.Saint.Controller;

import com.example.Saint.DTO.CheckInDTO;
import com.example.Saint.DTO.CheckOutDTO;
import com.example.Saint.DTO.DeleteReservaDTO;
import com.example.Saint.DTO.ReservaRequest;
import com.example.Saint.Entity.*;
import com.example.Saint.Repository.QuartosOcupadosRepository;
import com.example.Saint.Repository.QuartosRepository;
import com.example.Saint.Repository.RedeSaintHotelsRepository;
import com.example.Saint.Repository.UsuariosRepository;
import com.example.Saint.Service.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private CheckOutService checkOutService;

    @Autowired
    private RedeSaintHotelsRepository redeSaintHotelsRepository;


    @PostMapping("/registrar") // verificação e tratamento feito
    public ResponseEntity<?> registrarQuarto(@RequestBody @Valid Quartos quartos) {
        quartosService.registrarQuarto(quartos.getNomeQuarto(), quartos.getNumero(), quartos.getValorDoQuarto(), quartos.getIdHotel());
        return ResponseEntity.ok().body("Quarto Registrado: Nome: " + quartos.getNomeQuarto() +
                " Número: " + quartos.getNumero());
    }

    @DeleteMapping("/excluirRegistroQuarto/{nomeQuarto}")
    public ResponseEntity<?> excluirRegistroQuarto(@PathVariable String nomeQuarto) {
        quartosService.deletarQuartoRegistrado(nomeQuarto);
        return ResponseEntity.ok().body("Registro do quarto: "+nomeQuarto+"\n Excluído com sucesso");
    }

    @PostMapping("/reservar") // verificação e tratamento feito
    public void reservarQuarto(@RequestBody ReservaRequest request) throws MPException, MPApiException {
        quartosOcupadosService.pedirReserva(request);
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
