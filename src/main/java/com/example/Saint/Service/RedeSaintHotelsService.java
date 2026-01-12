package com.example.Saint.Service;

import com.example.Saint.DTO.RedeSaintHotelsDTO;
import com.example.Saint.Entity.RedeSaintHotels;
import com.example.Saint.Repository.RedeSaintHotelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RedeSaintHotelsService {

    @Autowired
    private RedeSaintHotelsRepository redeSaintHotelsRepository;


    public void registrarHotel(RedeSaintHotelsDTO dto) {
        if (dto == null) {
            throw new RuntimeException("O DTO está vázio");
        }
        if (redeSaintHotelsRepository.existsByEmail(dto.email())){
            throw new RuntimeException("Já existe um Hotel com este email: "+dto.email());
        }

        if (redeSaintHotelsRepository.existsByTelefone(dto.telefone())) {
            throw new RuntimeException("Já existe um Hotel com este telefone");
        }

        RedeSaintHotels redeSaintHotels = new RedeSaintHotels();
        redeSaintHotels.setBairro(dto.bairro());
        redeSaintHotels.setCep(dto.cep());
        redeSaintHotels.setCidade(dto.cidade());
        redeSaintHotels.setEmail(dto.email());
        redeSaintHotels.setInauguracao(LocalDateTime.now());
        redeSaintHotels.setTelefone(dto.telefone());


        redeSaintHotelsRepository.save(redeSaintHotels);
    }

    public void excluirHotel(Long idHotel) {
        if (idHotel == null) {
            throw new RuntimeException("O ID está vázio");
        }

        int rede = redeSaintHotelsRepository.deleteByIdHotel(idHotel);
        if (rede == 0) {
            throw new RuntimeException("Hotel não encontrado");
        }


    }



}
