package com.example.Saint.mapper;

import com.example.Saint.DTO.UsuariosDTO;
import com.example.Saint.Entity.Usuarios;


public class MapperUser {

    public Usuarios userDtoToUsuarios(UsuariosDTO dto) {
        Usuarios usuarios = new Usuarios();
        usuarios.setSenha(dto.getSenha());
        usuarios.setCpf(dto.getCpf());
        usuarios.setDataNascimento(dto.getDataNascimento());
        usuarios.setNome(dto.getNome());
        usuarios.setRole(dto.getRole());
        usuarios.setEmail(dto.getEmail());

        return usuarios;
    }



}
