package com.example.Saint.Service;

import com.example.Saint.DTO.UsuariosDTO;
import com.example.Saint.Entity.Usuarios;
import com.example.Saint.Exception.GlobalExceptionHandler;
import com.example.Saint.Repository.UsuariosRepository;
import com.example.Saint.mapper.MapperUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UsuariosService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    public void registrarUsuario(UsuariosDTO dto) {

        try {
            if (Period.between(dto.getDataNascimento(), LocalDate.now()).getYears() >= 18) {
                usuariosRepository.save(new MapperUser().userDtoToUsuarios(dto));

            } else {
                throw new RuntimeException("A idade deve ser maior que 18 para criar uma conta");
            }
        } catch (DataIntegrityViolationException e) {
             throw new RuntimeException("Os campos email e cpf não devem ter valores duplicadcos");

        }
    }

    public void deletarConta(String nome){
        int delete = usuariosRepository.deleteByNome(nome);
        if (delete == 0) {
            throw new RuntimeException("Usuário não encontrado");
        }
    }


}
