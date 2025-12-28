package com.example.Saint.Service;

import com.example.Saint.Entity.Usuarios;
import com.example.Saint.Repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UsuariosService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    public void registrarUsuario(Usuarios user) {

        if (Period.between(user.getDataNascimento(), LocalDate.now()).getYears() >= 18 ) {
             usuariosRepository.save(user);

        } else {
            throw new RuntimeException("A idade deve ser maior que 18 para criar uma conta");
        }
    }

    public void deletarConta(String cpfDigitado){
        usuariosRepository.deleteByCpf(cpfDigitado);
    }


}
