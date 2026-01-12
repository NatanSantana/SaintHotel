package com.example.Saint.Service;

import com.example.Saint.Entity.Quartos;
import com.example.Saint.Repository.QuartosRepository;
import com.example.Saint.Repository.RedeSaintHotelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class QuartosService {

    @Autowired
    private QuartosRepository quartosRepository;

    @Autowired
    private RedeSaintHotelsRepository redeSaintHotelsRepository;


    public void registrarQuarto(String nome, int numero, BigDecimal valorQuarto, Long idHotel) {

        Optional<Quartos> qr = quartosRepository.findByNumero(numero, idHotel);

        Optional<Quartos> qrNome = Optional.ofNullable(quartosRepository.findByNomeQuarto(nome, idHotel));

        if (qr.isEmpty() && qrNome.isEmpty() && redeSaintHotelsRepository.existsById(idHotel)) {
            Quartos quartos = new Quartos();
            quartos.setNomeQuarto(nome);
            quartos.setNumero(numero);
            quartos.setValorDoQuarto(valorQuarto);
            quartos.setIdHotel(idHotel);

            quartosRepository.save(quartos);

        } else if (qr.isPresent()){
            throw new RuntimeException("Um quarto com esse número já foi registrado");
        } else if (qrNome.isPresent()) {
            throw new RuntimeException("Um quarto com esse nome já foi registrado");
        } else if (!redeSaintHotelsRepository.existsById(idHotel)) {
            throw new RuntimeException("Não existe nenhuma filial com esse ID");
        }
    }

    public void deletarQuartoRegistrado(String nomeQuarto) {
        try {

            if (!nomeQuarto.isEmpty()) {
                quartosRepository.deleteByNomeQuarto(nomeQuarto);
            } else {
                throw new RuntimeException("Preencha o parâmetro com o nome do quarto");
            }
        } catch (NumberFormatException e) {
            System.out.println("No lugar do nome do quarto foi inserido um tipo numérico");
        }

    }

}
