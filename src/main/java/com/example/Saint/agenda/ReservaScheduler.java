package com.example.Saint.agenda;

import com.example.Saint.Entity.Quartos;
import com.example.Saint.Entity.QuartosOcupados;
import com.example.Saint.Entity.RedeSaintHotels;
import com.example.Saint.Entity.Usuarios;
import com.example.Saint.Exception.NullResultException;
import com.example.Saint.Repository.QuartosOcupadosRepository;
import com.example.Saint.Repository.QuartosRepository;
import com.example.Saint.Repository.RedeSaintHotelsRepository;
import com.example.Saint.Repository.UsuariosRepository;
import com.example.Saint.Service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
            log.info("Não há reservas vencidas");

        } else {
            log.info("ATENÇÃO: RESERVAS JÁ VENCIDAS ABAIXO: \n");
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

    @Scheduled(cron = "0 0 9 * * *")
    public void lembreteDeCheckInAmanha() {

        LocalDate anoMesDia = LocalDate.now();

        String anoMesDiaString = anoMesDia.toString();

        LocalDateTime diaDoCheckIn = LocalDateTime.parse(anoMesDiaString+"T09:00:00").plusDays(1);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        List<QuartosOcupados> qos = quartosOcupadosRepository.acharDiaCheckIn(diaDoCheckIn);

        if (qos.isEmpty()) {
            log.info("Não há CheckIns para ser realizados Amanhã");
        } else {

            System.out.println("CheckIns para o dia: "+diaDoCheckIn);
            qos.stream().forEach(System.out::println);

            for (QuartosOcupados qo : qos) {
                Usuarios user = usuariosRepository.findById(qo.getIdUsuario()).orElseThrow(() -> new NullResultException("Usuário não encontrado"));
                QuartosOcupados quartoUser = quartosOcupadosRepository.reservasPorUsuarioECheckIn(user.getIdUsuario(),
                        diaDoCheckIn);

                Quartos quarto = quartosRepository.findById(quartoUser.getIdQuarto())
                        .orElseThrow(() -> new NullResultException("Quarto não encontrado"));;

                emailService.sendEmail(user.getEmail(), "Lembre De Fazer Seu CheckIn",
                        "Você tem um CheckIn para fazer no Quarto "+quarto.getNomeQuarto()+" Numero "+quarto.getNumero()+".\nO checkIn deve ser realizado Amanhã "+diaDoCheckIn.format(formatter));
                log.info("Email enviado para "+user.getEmail());
            }

        }


    }

    @Scheduled(cron = "0 0 15 * * *")
    public void lembreteDeCheckIn() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        List<QuartosOcupados> qos = quartosOcupadosRepository.findAll();

        if (qos.isEmpty()) {
            log.info("Não há reservas");
        } else {
            for (QuartosOcupados qo : qos) {

                Usuarios user = usuariosRepository.findById(qo.getIdUsuario()).orElseThrow(() -> new NullResultException("Usuário não encontrado"));

                Quartos quarto = quartosRepository.findById(qo.getIdQuarto())
                        .orElseThrow(() -> new NullResultException("Quarto não encontrado"));;

                emailService.sendEmail(user.getEmail(), "Lembre De Fazer Seu CheckIn",
                        "Você tem um CheckIn para fazer no Quarto "+quarto.getNomeQuarto()+" Numero "+quarto.getNumero()+".\nO checkIn deve ser realizado "+qo.getDiaReservado().format(formatter));
                log.info("Email enviado para "+user.getEmail());
            }

        }


    }






}
