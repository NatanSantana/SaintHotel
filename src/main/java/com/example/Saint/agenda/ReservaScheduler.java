package com.example.Saint.agenda;

import com.example.Saint.Entity.Quartos;
import com.example.Saint.Entity.QuartosOcupados;
import com.example.Saint.Entity.RedeSaintHotels;
import com.example.Saint.Entity.Usuarios;
import com.example.Saint.Repository.QuartosOcupadosRepository;
import com.example.Saint.Repository.QuartosRepository;
import com.example.Saint.Repository.RedeSaintHotelsRepository;
import com.example.Saint.Repository.UsuariosRepository;
import com.example.Saint.Service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ReservaScheduler {

    @Autowired
    private QuartosOcupadosRepository quartosOcupadosRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private QuartosRepository quartosRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedeSaintHotelsRepository redeSaintHotelsRepository;

    // O método faz a verificação a cada 10 horas
    @Scheduled(fixedDelay = 36_000_000)
    public void alertaReservasVencidas() {


        List<QuartosOcupados> qos = quartosOcupadosRepository.reservasVencidas(LocalDateTime.now());

        if (qos.isEmpty()) {
            System.out.println("Não há reservas vencidas");

        } else {
            System.out.println("ATENÇÃO: RESERVAS JÁ VENCIDAS ABAIXO: \n");
            qos.stream().forEach(System.out::println);
            quartosOcupadosRepository.marcarReservasComoExpirada(LocalDateTime.now());


            List<Long> idsUser = qos.stream()
                    .map(QuartosOcupados::getIdUsuario)
                    .distinct()
                    .toList();

            for(Long ids : idsUser) {
                Usuarios user = usuariosRepository.findById(ids).orElseThrow(() ->
                        new RuntimeException("Não foi possível encontrar o ID do usuário"));

                List<QuartosOcupados> reservasVencidas = quartosOcupadosRepository.reservasVencidasPorUsuario(ids);


                for (QuartosOcupados rv : reservasVencidas) {
                    Quartos qo = quartosRepository.findById(rv.getIdQuarto()).orElseThrow(() ->
                            new RuntimeException("Não foi possível encontrar as reservas vencidas"));

                    RedeSaintHotels redeSaintHotels = redeSaintHotelsRepository.findById(qo.getIdHotel()).orElseThrow(() ->
                            new RuntimeException("Não foi possível encontrar a qual Hotel o quarto: "+qo.getNomeQuarto()+" pertence"));

                    try {
                        emailService.sendEmail(user.getEmail(), "Reserva Vencida do quarto: " + qo.getNomeQuarto()+ " da unidade: "+redeSaintHotels.getBairro()+" da rede Saint Hotel",
                                "Sua reserva já está vencida, por favor, compareça a recepção para realizar o checkOut");
                        log.info("Email(s) enviado(s)");
                    } catch (MailSendException e) {
                        throw new RuntimeException("Email inválido: "+ e.getMessage());
                    }



                }
            }
        }
    }






}
