package com.example.Saint.Service;

import com.example.Saint.DTO.CheckOutDTO;
import com.example.Saint.Entity.CheckOut;
import com.example.Saint.Entity.Quartos;
import com.example.Saint.Entity.QuartosOcupados;
import com.example.Saint.Entity.Usuarios;
import com.example.Saint.Repository.CheckOutRepository;
import com.example.Saint.Repository.QuartosOcupadosRepository;
import com.example.Saint.Repository.QuartosRepository;
import com.example.Saint.Repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CheckOutService {

    @Autowired
    private CheckOutRepository checkOutRepository;

    @Autowired
    private QuartosRepository quartosRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private QuartosOcupadosRepository quartosOcupadosRepository;

    public CheckOut fazerCheckOut(CheckOutDTO checkOutDTO) {

        usuariosRepository.findByCpf(checkOutDTO.getCpf())
                .orElseThrow(() -> new RuntimeException("Não existe usuário com este cpf"));

        Quartos qo = quartosRepository.findByNomeQuarto(checkOutDTO.getNomeQuarto());

        QuartosOcupados qos = quartosOcupadosRepository.findByDiaReservado(checkOutDTO.getDiaReservado())
                .orElseThrow(() -> new RuntimeException("Não existe reserva com esse dia"));

        if(qo == null) {
            throw new RuntimeException("Não existe quarto com este nome");
        }

        CheckOut checkOut = new CheckOut();
        checkOut.setDataHoraCheckOut(LocalDateTime.now());
        checkOut.setIdQuarto(qo.getIdQuarto());
        checkOut.setCpf(checkOutDTO.getCpf());

        if (LocalDateTime.now().isAfter(qos.getDiaReservado()) ) {
            return checkOutRepository.save(checkOut);
        } else {
            throw new RuntimeException("O checkout deve ser feito pelo menos depois do dia de reserva");
        }



    }

}
