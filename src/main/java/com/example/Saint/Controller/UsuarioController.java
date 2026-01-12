package com.example.Saint.Controller;

import com.example.Saint.DTO.LoginRequestDTO;
import com.example.Saint.Repository.RedeSaintHotelsRepository;
import com.example.Saint.Repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UsuarioController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuariosRepository usuariosRepository;


    @DeleteMapping("/deletaruser")
    public ResponseEntity<?> deletarUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.nome(), loginRequestDTO.senha()));

        if (authentication.isAuthenticated()) {
            usuariosRepository.deleteByNome(loginRequestDTO.nome());
            return ResponseEntity.ok().body("Usuário deletado com sucesso");
        } else {
            return ResponseEntity.badRequest().body("Senha ou usuário incorreto");
        }


    }



}
