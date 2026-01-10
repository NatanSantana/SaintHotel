package com.example.Saint.Service;

import com.example.Saint.DTO.CheckInDTO;
import com.example.Saint.Entity.CheckIn;
import com.example.Saint.Entity.Quartos;
import com.example.Saint.Entity.QuartosOcupados;
import com.example.Saint.Entity.Usuarios;
import com.example.Saint.Repository.CheckInRepository;
import com.example.Saint.Repository.QuartosOcupadosRepository;
import com.example.Saint.Repository.QuartosRepository;
import com.example.Saint.Repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CheckInService {

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private QuartosRepository quartosRepository;

    @Autowired
    private QuartosOcupadosRepository quartosOcupadosRepository;


    public CheckIn fazerCheckIn(CheckInDTO checkInDTO) {

        QuartosOcupados qos = quartosOcupadosRepository.findByDiaReservado(checkInDTO.getDiaReservado())
                .orElseThrow(() -> new RuntimeException("Não existe reserva para o dia selecionado"));





        Usuarios user = usuariosRepository.findByCpf(checkInDTO.getCpf())
                .orElseThrow(() -> new RuntimeException("Não existe usuário com este CPF"));

        Quartos qo = quartosRepository.findByNomeQuarto(checkInDTO.getNomeQuarto());

        if (qo == null) {
            throw new RuntimeException("Não existe um quarto com esse nome");
        }

        CheckIn checkIn = new CheckIn();
        checkIn.setCpf(checkInDTO.getCpf());
        checkIn.setDataHoraCheckIn(LocalDateTime.now());
        checkIn.setIdQuarto(qo.getIdQuarto());
        checkIn.setDiaReservado(checkInDTO.getDiaReservado());

        if (qos.getDiaReservado().isBefore(LocalDateTime.now()) &&
                LocalDateTime.now().isBefore(qos.getCheckOut())) {

            return checkInRepository.save(checkIn);

        } else {
            throw new RuntimeException("A reserva deve ser feita depois do primeiro dia reservado e antes do último");
        }


    }




}
