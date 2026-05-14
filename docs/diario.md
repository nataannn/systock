## Diário de bordo - Systock 

### 2026-05-13 - Setup Inicial
Estrutura de pastas criada. WSL2 com Java 21 e Docker configurados.

### 2026-05-13 - Onda 1 concluída
docker compose up sobre Postgres 16 e Spring Boot 3 com Java 21. Flyway aplicou V1_init.sql. Endpoint /actuator/health responde UP. Stack base operacional.

### 2026-05-13 - Primeiro vertical slice: Categoria
Service, controller, DTO, GlobalExceptionHandler e templates Thymeleaf implementados. CRUD completo da entidade Categoria funcionando: criar, listar, editar e excluir, com validação e mensagens de erro/sucesso. Padrões Clean Architecture (camadas separadas) e Post/Redirect/Get aplicados.
### 2026-05-13 - Bloco 2D: autenticação implementada
Entidades Perfil e Usuario, UserDetailsService, SecurityConfig com BCrypt fator 10, tela de login Thymeleaf, navbar com usuário logado e logout. CSRF desabilitado provisoriamente — pendência para Onda 3.

### 2026-05-14 - Segundo vertical slice: Fornecedor
Service, DTO, controller, templates de listagem e edição. CRUD completo com exclusão lógica (desativar/reativar) em vez de DELETE físico. Normalização de CNPJ no service. Padrão Post/Redirect/Get mantido.

### 2026-05-14 - Quiz Fornecedor
Cinco perguntas tipo banca. Acertei readOnly (Pergunta 2) e POST vs GET (Pergunta 5). Errei dirty checking (Pergunta 1) e travei em defesa em profundidade (Pergunta 3) e PRG (Pergunta 4). Conceitos a internalizar antes de Produto.
