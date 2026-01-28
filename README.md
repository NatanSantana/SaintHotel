# Saint Hotel

Sistema de Hotelaria

API REST desenvolvida em **Java com Spring Boot** para gerenciamento de hotéis, quartos, usuários, reservas e pagamentos.

---

## 🚀 Tecnologias Utilizadas

- Java 24
- Spring Boot
- Spring Data JPA
- Spring Security (JWT)
- PostgreSQL
- Maven

---

## 📌 Funcionalidades

- Cadastro e autenticação de usuários
- Cadastro de hotéis e quartos
- Reserva de quartos por período
- Verificação de disponibilidade
- Alerta de quartos com checkout vencido
- Envio de e-mail para usuários que não realizaram o checkout na data prevista
- Integração com gateway de pagamento
- Webhook para confirmação de pagamento

---

## 🧱 Arquitetura

O projeto segue uma arquitetura em camadas:

- **Controller** → Recebe as requisições HTTP
- **Service** → Contém as regras de negócio
- **Repository** → Comunicação com o banco de dados
- **DTOs** → Transferência de dados
- **Entities** → Mapeamento JPA

---

## ▶️ Como rodar o projeto

### 📋 Pré-requisitos

- Java 24
- Maven
- PostgreSQL

---

## ⚙️ Configuração do Banco de Dados

Configure o arquivo `application.properties` ou `application.yml`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/saint_hotel
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=seu_email@gmail.com
spring.mail.password=sua_senha_de_app
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

secret.key=key_para_gerar_token_jwt

mercadopago.access-token=token_de_acesso_mercadopago
```

## Como clonar o código

```bash
git clone https://github.com/NatanSantana/SaintHotel
cd SaintHotel
mvn spring-boot:run
```
