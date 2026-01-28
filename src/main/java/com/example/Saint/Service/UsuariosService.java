package com.example.Saint.Service;

import com.example.Saint.DTO.UsuariosDTO;
import com.example.Saint.Repository.UsuariosRepository;
import com.example.Saint.mapper.MapperUser;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@AllArgsConstructor
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;

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
