package com.example.Saint.Service;

import com.example.Saint.Entity.Quartos;
import com.example.Saint.Repository.QuartosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuartosService {

    @Autowired
    private QuartosRepository quartosRepository;


    public void registrarQuarto(String nome, int numero) {

        Optional<Quartos> qr = quartosRepository.findByNumero(numero);
        Optional<Quartos> qrNome = Optional.ofNullable(quartosRepository.findByNomeQuarto(nome));

        if (qr.isEmpty() && qrNome.isEmpty()) {
            Quartos quartos = new Quartos();
            quartos.setNomeQuarto(nome);
            quartos.setNumero(numero);

            quartosRepository.save(quartos);

        } else if (qr.isPresent()){
            throw new RuntimeException("Um quarto com esse número já foi registrado");
        } else if (qrNome.isPresent()) {
            throw new RuntimeException("Um quarto com esse nome já foi registrado");
        }


    }

}
