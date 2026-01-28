package com.example.Saint.mapper;

import com.example.Saint.DTO.RedeSaintHotelsDTO;
import com.example.Saint.Entity.RedeSaintHotels;
import com.example.Saint.Repository.RedeSaintHotelsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class MapperRedeSaintHotels {

    @Autowired
    private RedeSaintHotelsRepository redeSaintHotelsRepository;

    public RedeSaintHotels dtoToEntity(RedeSaintHotelsDTO dto) {
        RedeSaintHotels redeSaintHotels = new RedeSaintHotels();
        redeSaintHotels.setBairro(dto.bairro());
        redeSaintHotels.setCep(dto.cep());
        redeSaintHotels.setCidade(dto.cidade());
        redeSaintHotels.setEmail(dto.email());
        redeSaintHotels.setInauguracao(LocalDateTime.now());
        redeSaintHotels.setTelefone(dto.telefone());

        if (redeSaintHotelsRepository.existsByEmail(dto.email())){
            throw new RuntimeException("Já existe um Hotel com este email: "+dto.email());
        }

        if (redeSaintHotelsRepository.existsByTelefone(dto.telefone())) {
            throw new RuntimeException("Já existe um Hotel com este telefone");
        }

        return redeSaintHotels;

    }

}
