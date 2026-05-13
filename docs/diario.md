## Diário de bordo - Systock 

### 2026-05-13 - Setup Inicial
Estrutura de pastas criada. WSL2 com Java 21 e Docker configurados.

### 2026-05-13 - Onda 1 concluída
docker compose up sobre Postgres 16 e Spring Boot 3 com Java 21. Flyway aplicou V1_init.sql. Endpoint /actuator/health responde UP. Stack base operacional.

### 2026-05-13 - Primeiro vertical slice: Categoria
Service, controller, DTO, GlobalExceptionHandler e templates Thymeleaf implementados. CRUD completo da entidade Categoria funcionando: criar, listar, editar e excluir, com validação e mensagens de erro/sucesso. Padrões Clean Architecture (camadas separadas) e Post/Redirect/Get aplicados.