package com.example.Saint.Service;

import com.example.Saint.DTO.CheckInDTO;
import com.example.Saint.Entity.CheckIn;
import com.example.Saint.Entity.Quartos;
import com.example.Saint.Entity.QuartosOcupados;
import com.example.Saint.Entity.Usuarios;
import com.example.Saint.Repository.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CheckInService {

    private final CheckInRepository checkInRepository;

    private final UsuariosRepository usuariosRepository;

    private final QuartosRepository quartosRepository;

    private final QuartosOcupadosRepository quartosOcupadosRepository;

    private final RedeSaintHotelsRepository redeSaintHotelsRepository;


    public CheckIn fazerCheckIn(CheckInDTO checkInDTO) {

        QuartosOcupados qos = quartosOcupadosRepository.findByDiaReservado(checkInDTO.getDiaReservado())
                .orElseThrow(() -> new RuntimeException("Não existe reserva para o dia selecionado"));

        if (!redeSaintHotelsRepository.existsById(checkInDTO.getIdHotel())) {
            throw new RuntimeException("Não existe Hotel com esse ID");
        }

        Usuarios user = usuariosRepository.findByCpf(checkInDTO.getCpf())
                .orElseThrow(() -> new RuntimeException("Não existe usuário com este CPF"));

        Quartos qo = quartosRepository.findByNomeQuarto(checkInDTO.getNomeQuarto(), checkInDTO.getIdHotel());

        if (qo == null) {
            throw new RuntimeException("Não existe um quarto com esse nome");
        }

        CheckIn checkIn = new CheckIn();
        checkIn.setCpf(checkInDTO.getCpf());
        checkIn.setDataHoraCheckIn(LocalDateTime.now());
        checkIn.setIdQuarto(qo.getIdQuarto());
        checkIn.setDiaReservado(checkInDTO.getDiaReservado());
        checkIn.setIdHotel(checkInDTO.getIdHotel());
        checkIn.setIdUsuario(user.getIdUsuario());

        if (qos.getDiaReservado().isBefore(LocalDateTime.now()) &&
                LocalDateTime.now().isBefore(qos.getCheckOut())) {

            return checkInRepository.save(checkIn);

        } else {
            throw new RuntimeException("A reserva deve ser feita depois do primeiro dia reservado e antes do último");
        }


    }




}
