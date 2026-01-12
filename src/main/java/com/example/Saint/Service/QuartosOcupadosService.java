package com.example.Saint.Service;

import com.example.Saint.DTO.DeleteReservaDTO;
import com.example.Saint.DTO.ReservaRequest;
import com.example.Saint.Entity.Quartos;
import com.example.Saint.Entity.QuartosOcupados;
import com.example.Saint.Entity.RedeSaintHotels;
import com.example.Saint.Entity.Usuarios;
import com.example.Saint.Repository.QuartosOcupadosRepository;
import com.example.Saint.Repository.QuartosRepository;
import com.example.Saint.Repository.RedeSaintHotelsRepository;
import com.example.Saint.Repository.UsuariosRepository;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QuartosOcupadosService {

    @Autowired
    private QuartosOcupadosRepository quartosOcupadosRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private RedeSaintHotelsRepository redeSaintHotelsRepository;

    @Autowired
    private QuartosRepository quartosRepository;

    @Autowired
    private MercadoPagoService mercadoPagoService;



    public ResponseEntity<?> pedirReserva(ReservaRequest request) throws MPException, MPApiException {
        Optional<Quartos> quartos =
                Optional.ofNullable(quartosRepository.findByNomeQuarto(request.getNomeQuarto(), request.getIdHotel()));
        if (quartos.isEmpty()) {
            throw new RuntimeException("Não foi possível encontrar o quarto solicitado");
        }

        List<QuartosOcupados> quartoOcupado = quartosOcupadosRepository.findAllByIdQuarto(quartos.get().getIdQuarto());

        Usuarios usuarios = usuariosRepository.findByCpf(request.getCpf()).orElseThrow(() ->
                new RuntimeException("Usuario não encontrado com esse cpf"));


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
            return ResponseEntity.badRequest().body("Este Quarto Já foi Reservado");
        }
    }

    public void confirmarReservarQuarto(Long idQuarto, LocalDateTime dia, int diasNoHotel, Long idUsuario, Long idHotel) {

        QuartosOcupados quartosOcupados = new QuartosOcupados();
        quartosOcupados.setIdQuarto(idQuarto);
        quartosOcupados.setDiaReservado(dia);
        quartosOcupados.setCheckOut(dia.plusDays(diasNoHotel));
        quartosOcupados.setDiasReservados(diasNoHotel);
        quartosOcupados.setIdUsuario(idUsuario);
        quartosOcupados.setExpirou(false);
        quartosOcupados.setIdHotel(idHotel);


        quartosOcupadosRepository.save(quartosOcupados);
    }

    @Transactional
    public void cancelarReserva(DeleteReservaDTO deleteReservaDTO) {

        Usuarios usuarios = usuariosRepository.findByCpf(deleteReservaDTO.getCpf()).orElseThrow
                (() -> new RuntimeException("Não existe registro de reserva com esse CPF"));

        RedeSaintHotels redeSaintHotels = redeSaintHotelsRepository.findById(deleteReservaDTO.getIdHotel()).orElseThrow(() ->
                new RuntimeException("Não foi encontrado nenhuma reserva com esse idHotel"));

        int qo = quartosOcupadosRepository.deleteReserva(deleteReservaDTO.getDia(), usuarios.getIdUsuario(), deleteReservaDTO.getIdHotel());
        if (qo == 0) {
            throw new RuntimeException("Não existe Reserva Para Deletar");
        }

    }

}
