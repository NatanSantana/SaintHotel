package com.example.Saint.agenda;

import com.example.Saint.DTO.ReservaVencidaDTO;
import com.example.Saint.Entity.Quartos;
import com.example.Saint.Entity.QuartosOcupados;
import com.example.Saint.Entity.Usuarios;
import com.example.Saint.Repository.QuartosOcupadosRepository;
import com.example.Saint.Repository.QuartosRepository;
import com.example.Saint.Repository.UsuariosRepository;
import com.example.Saint.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class ReservaScheduler {

    @Autowired
    private QuartosOcupadosRepository quartosOcupadosRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private QuartosRepository quartosRepository;

    @Autowired
    private EmailService emailService;

    // O método faz a verificação a cada 10 horas
    @Scheduled(fixedDelay = 36_000_000)
    public void alertaReservasVencidas() {


        List<QuartosOcupados> qos = quartosOcupadosRepository.reservasVencidas(LocalDateTime.now());

        if (qos.isEmpty()) {

        } else {
            System.out.println("ATENÇÃO: RESERVAS JÁ VENCIDAS \n");
            qos.stream().forEach(System.out::println);
            quartosOcupadosRepository.marcarReservasComoExpirada(LocalDateTime.now());


            List<Long> idsUser = qos.stream().map(QuartosOcupados::getIdUsuario).toList();

            for(Long ids : idsUser) {
                Optional<Usuarios> user = usuariosRepository.findById(ids);

                List<QuartosOcupados> reservasVencidas = quartosOcupadosRepository.reservasVencidasPorUsuario(ids);

                for (QuartosOcupados rv : reservasVencidas) {
                    Optional<Quartos> qo = quartosRepository.findById(rv.getIdQuarto());

                    emailService.sendEmail(user.get().getEmail(), "Reserva Vencida do quarto: " + qo.get().getNomeQuarto(),
                            "Sua reserva já está vencida, por favor, compareça a recepção para realizar o checkOut");


                }
            }
        }
    }



}
