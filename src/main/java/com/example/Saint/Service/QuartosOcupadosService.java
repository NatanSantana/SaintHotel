package com.example.Saint.Service;

import com.example.Saint.DTO.DeleteReservaDTO;
import com.example.Saint.Entity.QuartosOcupados;
import com.example.Saint.Entity.Usuarios;
import com.example.Saint.Repository.QuartosOcupadosRepository;
import com.example.Saint.Repository.UsuariosRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuartosOcupadosService {

    @Autowired
    private QuartosOcupadosRepository quartosOcupadosRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;


    public void reservarQuarto(Long idQuarto, LocalDateTime dia, int diasNoHotel, Long idUsuario) {

        QuartosOcupados quartosOcupados = new QuartosOcupados();
        quartosOcupados.setIdQuarto(idQuarto);
        quartosOcupados.setDiaReservado(dia);
        quartosOcupados.setCheckOut(dia.plusDays(diasNoHotel));
        quartosOcupados.setDiasReservados(diasNoHotel);
        quartosOcupados.setIdUsuario(idUsuario);


        quartosOcupadosRepository.save(quartosOcupados);
    }

    @Transactional
    public void cancelarReserva(DeleteReservaDTO deleteReservaDTO) {

        Usuarios usuarios = usuariosRepository.findByCpf(deleteReservaDTO.getCpf()).orElseThrow
                (() -> new RuntimeException("Não existe registro de reserva com esse CPF"));

        int qo = quartosOcupadosRepository.deleteReserva(deleteReservaDTO.getDia(), usuarios.getIdUsuario());
        if (qo == 0) {
            throw new RuntimeException("Não existe Reserva Para Deletar");
        }

    }

}
