package com.example.Saint.Service;

import com.example.Saint.DTO.RedeSaintHotelsDTO;
import com.example.Saint.Entity.RedeSaintHotels;
import com.example.Saint.Repository.RedeSaintHotelsRepository;
import com.example.Saint.mapper.MapperRedeSaintHotels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RedeSaintHotelsService {

    @Autowired
    private RedeSaintHotelsRepository redeSaintHotelsRepository;


    public void registrarHotel(RedeSaintHotelsDTO dto) {
        if (dto != null) {
            redeSaintHotelsRepository.save(new MapperRedeSaintHotels().dtoToEntity(dto));
        } else {
            throw new RuntimeException("O DTO está vázio");
        }

    }

    public void excluirHotel(Long idHotel) {
        if (idHotel != null) {
            int rede = redeSaintHotelsRepository.deleteByIdHotel(idHotel);
            if (rede == 0) {
                throw new RuntimeException("Hotel não encontrado");
            }
        } else {
            throw new RuntimeException("O ID está vázio");
        }





    }



}
