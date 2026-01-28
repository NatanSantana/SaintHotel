package com.example.Saint.Service;

import com.example.Saint.DTO.CheckOutDTO;
import com.example.Saint.Entity.*;
import com.example.Saint.Repository.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CheckOutService {

    private final CheckOutRepository checkOutRepository;

    private final QuartosRepository quartosRepository;

    private final UsuariosRepository usuariosRepository;

    private final QuartosOcupadosRepository quartosOcupadosRepository;

    private final RedeSaintHotelsRepository redeSaintHotelsRepository;

    public CheckOut fazerCheckOut(CheckOutDTO checkOutDTO) {

        usuariosRepository.findByCpf(checkOutDTO.getCpf())
                .orElseThrow(() -> new RuntimeException("Não existe usuário com este cpf"));

        Quartos qo = quartosRepository.findByNomeQuarto(checkOutDTO.getNomeQuarto(), checkOutDTO.getIdHotel());

        QuartosOcupados qos = quartosOcupadosRepository.findByDiaReservado(checkOutDTO.getDiaReservado())
                .orElseThrow(() -> new RuntimeException("Não existe reserva com esse dia"));

        Usuarios user = usuariosRepository.findByCpf(checkOutDTO.getCpf())
                .orElseThrow(() -> new RuntimeException("Não existe usuário com este CPF"));

        RedeSaintHotels redeSaintHotels = redeSaintHotelsRepository.findById(checkOutDTO.getIdHotel())
                .orElseThrow(() -> new RuntimeException("Não foi encontrado Hotel com esse ID"));

        if(qo == null) {
            throw new RuntimeException("Não existe quarto com este nome");
        }

        CheckOut checkOut = new CheckOut();
        checkOut.setDataHoraCheckOut(LocalDateTime.now());
        checkOut.setIdQuarto(qo.getIdQuarto());
        checkOut.setCpf(checkOutDTO.getCpf());
        checkOut.setIdHotel(checkOut.getIdHotel());
        checkOut.setIdUsuario(user.getIdUsuario());

        if (LocalDateTime.now().isAfter(qos.getDiaReservado()) ) {
            return checkOutRepository.save(checkOut);
        } else {
            throw new RuntimeException("O checkout deve ser feito pelo menos depois do dia de reserva");
        }



    }

}
