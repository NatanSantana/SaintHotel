# API SAINT HOTEL
## Sistema de Hotelaria

Abaixo há informações do meu projeto pessoal, uma API para gerenciamento de Hotel, feito no Spring Boot.

## Tecnologias Utilizadas

- Java 24
- Spring Boot
- Spring Data JPA
- Spring Security (JWT)
- Postgresql
- Maven

## Funcionalidades

- Cadastro e autenticação de usuários
- Cadastro de hotéis e quartos
- Reserva de quartos por período
- Alerta sobre quartos que estão com o checkout vencido
- Envio de email para usuário que não concluíram o checkout antes da data prevista.
- Verificação de disponibilidade
- Integração com gateway de pagamento
- Webhook para confirmação de pagamento

## Arquitetura

O projeto segue uma arquitetura em camadas:

- **Controller** → Recebe as requisições HTTP
- **Service** → Contém as regras de negócio
- **Repository** → Comunicação com o banco de dados
- **DTOs** → Transferência de dados
- **Entities** → Mapeamento JPA

  
## Como rodar o projeto

### Pré-requisitos
- Java 17+
- Maven
- PostgreSQL

### Passos

```bash
git clone https://github.com/NatanSantana/SaintHotel
cd api-hotel
mvn spring-boot:run
