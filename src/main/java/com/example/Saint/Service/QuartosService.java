package com.example.Saint.Service;

import com.example.Saint.DTO.AtualizarNomeQuartoDTO;
import com.example.Saint.DTO.AtualizarNomeQuartoResponse;
import com.example.Saint.DTO.AtualizarPrecoQuartoDTO;
import com.example.Saint.DTO.AtualizarPrecoQuartoResponse;
import com.example.Saint.Entity.Quartos;
import com.example.Saint.Exception.NullResultException;
import com.example.Saint.Repository.QuartosRepository;
import com.example.Saint.Repository.RedeSaintHotelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
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

    public List<Quartos> listarQuartos() {
        return quartosRepository.findAllQuartos();
    }

    public AtualizarNomeQuartoResponse atualizarNomeQuarto(AtualizarNomeQuartoDTO dto) {

        String nomeAntigo = quartosRepository.findById(dto.idQuarto()).get().getNomeQuarto();

        if (!nomeAntigo.isEmpty()) {
            AtualizarNomeQuartoResponse atualizar = new AtualizarNomeQuartoResponse(nomeAntigo, dto.idQuarto(), dto.nomeQuarto());
            quartosRepository.atualizarNomeQuarto(dto.idQuarto(), dto.nomeQuarto());
            return atualizar;
        } else {
            throw new NullResultException("Não Existe Quarto Com esse ID");
        }
    }

    public AtualizarPrecoQuartoResponse atualizarPreco(AtualizarPrecoQuartoDTO dto) {
        Quartos quartos = quartosRepository.findById(dto.idQuarto()).orElseThrow(() -> new NullResultException("Não Existe Quarto Com esse ID"));
        quartosRepository.atualizarValor(dto.idQuarto(), dto.valor());
        return new AtualizarPrecoQuartoResponse(dto.valor(), quartos.getValorDoQuarto(), quartos.getNomeQuarto());
    }


    public List<Quartos> listarQuartos(Long idHotel) {
        redeSaintHotelsRepository.findById(idHotel).orElseThrow(() -> new NullResultException("Este idHotel não existe"));
        List<Quartos> quartos = quartosRepository.findAllQuartosByIdHotel(idHotel);

        return quartos;
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
