package com.example.Saint.Controller;

import com.example.Saint.DTO.DeleteReservaDTO;
import com.example.Saint.DTO.ReservaRequest;
import com.example.Saint.Entity.Quartos;
import com.example.Saint.Entity.QuartosOcupados;
import com.example.Saint.Entity.Usuarios;
import com.example.Saint.Repository.QuartosOcupadosRepository;
import com.example.Saint.Repository.QuartosRepository;
import com.example.Saint.Repository.UsuariosRepository;
import com.example.Saint.Service.MercadoPagoService;
import com.example.Saint.Service.QuartosOcupadosService;
import com.example.Saint.Service.QuartosService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("/registrar") // verificação e tratamento feito
    public ResponseEntity<?> registrarQuarto(@RequestBody @Valid Quartos quartos) {
        quartosService.registrarQuarto(quartos.getNomeQuarto(), quartos.getNumero());
        return ResponseEntity.ok().body("Quarto Registrado: Nome: " + quartos.getNomeQuarto() +
                " Número: " + quartos.getNumero());
    }

    @PostMapping("/reservar") // verificação e tratamento feito
    public ResponseEntity<?> reservarQuarto(@RequestBody ReservaRequest request) throws MPException, MPApiException {

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

            Preference preference = mercadoPagoService.criarCheckoutPro(request);


            return ResponseEntity.ok().body(
                    Map.of(
                            "init_point", preference.getInitPoint(),
                            "sandbox_init_point", preference.getSandboxInitPoint()
                    )
            );

        } else {
            return ResponseEntity.ok().body("Este Quarto Já foi Reservado");
        }

    }

    @DeleteMapping("/cancelarReserva") // verificação e tratamento feito
    public ResponseEntity<?> cancelar(@RequestBody DeleteReservaDTO deleteReservaDTO) {
        quartosOcupadosService.cancelarReserva(deleteReservaDTO);
        return ResponseEntity.ok().body("Reserva Cancelada Com Sucesso");
    }

    @PostMapping("/checkout") // verificação e tratamento feito
    public ResponseEntity<?> checkout(@RequestBody DeleteReservaDTO deleteReservaDTO) {

        Usuarios usuarios = usuariosRepository.findByCpf(deleteReservaDTO.getCpf())
                .orElseThrow(() -> new RuntimeException("Não existe usuario com o CPF informado"));

        int qo = quartosOcupadosRepository.deleteReserva(deleteReservaDTO.getDia(), usuarios.getIdUsuario());
        if (qo == 0) {
            return ResponseEntity.ok().body("A reserva informada não existe para realizar a operação de CheckOut");
        }


        return ResponseEntity.ok().body("CheckOut Feito Com Sucesso");
    }


}
