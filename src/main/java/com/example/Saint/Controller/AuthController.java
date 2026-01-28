package com.example.Saint.Controller;

import com.example.Saint.DTO.LoginRequestDTO;
import com.example.Saint.DTO.UsuariosDTO;
import com.example.Saint.InfraSecurity.JwtUtil;
import com.example.Saint.Service.UsuariosService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UsuariosService usuarioService;

    private final PasswordEncoder passwordEncoder;


    @PostMapping("/register") // verificação e tratamento feito
    public ResponseEntity<?> register(@RequestBody @Valid UsuariosDTO dto) {
        dto.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuarioService.registrarUsuario(dto);

        return ResponseEntity.ok().body
                ("Usuario Registrado:\nNome: "+dto.getNome()+
                        "\nData de Nascimento: "+dto.getDataNascimento()+
                        "\nCpf: "+dto.getCpf());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.nome(), loginRequestDTO.senha()));

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok().body(token);

    }




}
