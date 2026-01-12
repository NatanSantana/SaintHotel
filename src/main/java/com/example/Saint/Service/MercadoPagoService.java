package com.example.Saint.Service;

import com.example.Saint.DTO.ReservaRequest;
import com.example.Saint.Entity.Quartos;
import com.example.Saint.Entity.Usuarios;
import com.example.Saint.Repository.QuartosOcupadosRepository;
import com.example.Saint.Repository.QuartosRepository;
import com.example.Saint.Repository.UsuariosRepository;
import com.example.Saint.Service.QuartosOcupadosService;
import com.example.Saint.Service.QuartosService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class MercadoPagoService {

    @Autowired
    private QuartosService quartosService;

    @Autowired
    private QuartosRepository quartosRepository;

    @Autowired
    private QuartosOcupadosService quartosOcupadosService;

    @Autowired
    private QuartosOcupadosRepository quartosOcupadosRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    public MercadoPagoService(@Value("${mercadopago.access.token}") String token) {

        MercadoPagoConfig.setAccessToken(token);
    }

    public Preference criarCheckoutPro(ReservaRequest request)
            throws MPException, MPApiException {


        Quartos qo = quartosRepository.findByNomeQuarto(request.getNomeQuarto(), request.getIdHotel());

        Optional<Usuarios> user = usuariosRepository.findByCpf(request.getCpf());

        List<PreferenceItemRequest> items = new ArrayList<>();
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("id_hotel", String.valueOf(request.getIdHotel()));


        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .id(String.valueOf(qo.getIdQuarto()))
                .title("Quarto: " +qo.getNomeQuarto()+ " Número: " + qo.getNumero())
                .description("Saint Hotel: Reserva do Quarto " + qo.getNomeQuarto())
                .quantity(request.getDiasNoHotel())
                .currencyId("BRL")
                .unitPrice(new BigDecimal(String.valueOf(qo.getValorDoQuarto())))
                .build();

        items.add(item);



        // -----------------------------
        // BACK URLS
        // -----------------------------
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("https://venially-hexagonal-hayley.ngrok-free.dev/success")
                .pending("https://venially-hexagonal-hayley.ngrok-free.dev/pending")
                .failure("https://venially-hexagonal-hayley.ngrok-free.dev/failure")
                .build();

        // -----------------------------
        // MÉTODOS DE PAGAMENTO
        // -----------------------------
        List<PreferencePaymentTypeRequest> excludedPaymentTypes = new ArrayList<>();
        excludedPaymentTypes.add(
                PreferencePaymentTypeRequest.builder().id("ticket").build()
        );
        excludedPaymentTypes.add(
                PreferencePaymentTypeRequest.builder().id("atm").build()
        );

        PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                .excludedPaymentTypes(excludedPaymentTypes)
                .installments(1) // Ex.: Número de parcelamentos disponíveis
                .build();

        // -----------------------------
        // PREFERÊNCIA FINAL
        // -----------------------------



        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .externalReference(request.getCpf())
                .additionalInfo(String.valueOf(request.getDia()))
                .metadata(metadata)
                .backUrls(backUrls)
                .payer(
                        PreferencePayerRequest.builder()
                                .name(user.get().getNome())
                                .email("")
                                .identification(IdentificationRequest.builder().type("CPF").number(user.get().getCpf()).build()
                                )
                                .build()
                )
                .paymentMethods(paymentMethods)
                .autoReturn("approved")
                .notificationUrl("https://venially-hexagonal-hayley.ngrok-free.dev/saintHotel/mercadopago/webhook")
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        return preference;
    }
}
