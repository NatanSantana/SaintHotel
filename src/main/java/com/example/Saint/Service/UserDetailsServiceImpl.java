package com.example.Saint.Service;

import com.example.Saint.Entity.Usuarios;
import com.example.Saint.Repository.UsuariosRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private UsuariosRepository usuariosRepository;

    @Override
    public UserDetails loadUserByUsername(String nome) throws UsernameNotFoundException {

        Usuarios usuarios = usuariosRepository.findByNome(nome).orElseThrow(
                () -> new UsernameNotFoundException("Usuário não encontrado"));

        return new User(usuarios.getNome(), usuarios.getSenha(), List.of(new SimpleGrantedAuthority("ROLE_" + usuarios.getRole())));
    }
}
