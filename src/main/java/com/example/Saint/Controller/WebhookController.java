package com.example.Saint.Controller;

import com.example.Saint.Entity.Usuarios;
import com.example.Saint.Repository.UsuariosRepository;
import com.example.Saint.Service.QuartosOcupadosService;
import com.mercadopago.client.merchantorder.MerchantOrderClient;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.merchantorder.MerchantOrder;
import com.mercadopago.resources.merchantorder.MerchantOrderItem;
import com.mercadopago.resources.merchantorder.MerchantOrderPayment;
import com.mercadopago.resources.payment.Payment;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Transactional
@RestController
@RequestMapping("/saintHotel/mercadopago")
public class WebhookController {

    @Autowired
    private QuartosOcupadosService quartosOcupadosService;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @PostMapping("/webhook")
    public ResponseEntity<Void> receberWebHook(@RequestBody Map<String, Object> payload) {

        System.out.println("Webhook recebido:");
        System.out.println(payload);
        String cpf = null;
        String titulo = null;



        try {

            // 1. Verifica se existe "resource"
            if (!payload.containsKey("resource")) {
                System.out.println("Webhook ignorado: sem resource");
                return ResponseEntity.ok().build();
            }

            String resourceUrl = (String) payload.get("resource");

            if (resourceUrl == null) {
                System.out.println("Resource nulo — ignorando");
                return ResponseEntity.ok().build();
            }


            // 2. Extrair ID da ordem
            Long merchantOrderId = Long.parseLong(resourceUrl.replaceAll("\\D", ""));


            MerchantOrderClient client = new MerchantOrderClient();


            MerchantOrder order = client.get(merchantOrderId);


            LocalDateTime dia = LocalDateTime.parse(order.getAdditionalInfo());
            cpf = order.getExternalReference();
            Long id = null;
            int dias = 0;






            for (MerchantOrderItem item : order.getItems()) {
                System.out.println("---- ITEM ----");
                System.out.println("Título: " + item.getTitle());
                System.out.println("Descrição: " + item.getDescription());
                System.out.println("Descrição: " + item.getCategoryId());
                System.out.println("Quantidade: " + item.getQuantity());
                System.out.println("Preço unitário: " + item.getUnitPrice());
                System.out.println("ID do produto enviado: " + item.getId());

                titulo = item.getTitle();
                id = Long.valueOf(item.getId());
                dias = item.getQuantity();

            }


            System.out.println("MerchantOrder ID = " + merchantOrderId);

            PaymentClient paymentClient = new PaymentClient();


            boolean isApproved = false;

            // 3. Ler payments com segurança
            for (MerchantOrderPayment mpPayment : order.getPayments()) {

                if (mpPayment.getStatus().equals("approved") && !isApproved) {
                    isApproved = true;

                    Optional<Usuarios> usuarios = usuariosRepository.findByCpf(cpf);

                    quartosOcupadosService.reservarQuarto(id, dia, dias, usuarios.get().getIdUsuario());
                }

                try {
                    Payment payment = paymentClient.get(mpPayment.getId());


                } catch (Exception e) {
                    System.out.println("Erro ao consultar pagamento " + mpPayment.getId());
                    e.printStackTrace();
                }
            }

            // 4. Só processa se aprovado
            if (!isApproved) {
                System.out.println("Pagamento ainda não aprovado.");
                return ResponseEntity.ok().build();
            }

            System.out.println("Pagamento APROVADO!");


            System.out.println("Compra registrada com sucesso.");



        } catch (Exception e) {
            System.out.println("❌ ERRO NO WEBHOOK:");
            return ResponseEntity.status(200).build(); // nunca retornar 500
        }

        return ResponseEntity.ok().build();
    }
}
