# Systock

Plataforma modular de gestão para microempresas de varejo de eletrônicos.

## Visão geral

Sistema web que centraliza cadastros, controle de estoque e infraestrutura
de autenticação para pequenas e médias empresas (PMEs) de varejo. Desenvolvido
como Trabalho de Conclusão de Curso, contempla módulos de:

- **Autenticação** por sessão com BCrypt e perfis (Administrador, Vendedor, Estoquista)
- **Categorias** de produtos
- **Fornecedores** com exclusão lógica
- **Produtos** com relacionamentos para Categoria e Fornecedor, controle
  de estoque (entrada/baixa), invariantes de domínio (estoque não-negativo,
  alerta de mínimo) e movimentações rastreáveis
- **Painel** com indicadores em tempo real

Os módulos de **Cliente** (com tratamento LGPD via criptografia AES-256
em repouso) e **Venda** (operação composta transacional com baixa
atômica de estoque) compõem os trabalhos futuros do projeto, já modelados
em diagrama de classes.

## Stack tecnológico

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 (LTS) |
| Framework | Spring Boot 3.x |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | PostgreSQL 16 |
| Migrations | Flyway |
| Segurança | Spring Security + BCrypt |
| Templates | Thymeleaf |
| Containerização | Docker + Docker Compose |
| Build | Maven |

## Arquitetura

Monolito modular em **Clean Architecture**, com separação em quatro camadas:

- `domain` — entidades e regras de negócio
- `application` — casos de uso (services)
- `infrastructure` — persistência, segurança, integrações
- `web` — controllers, DTOs, tratamento de exceções

## Como executar

Pré-requisitos: Docker e Docker Compose.

\`\`\`bash
git clone https://github.com/seu-usuario/systock.git
cd systock
docker compose up --build
\`\`\`

A aplicação fica disponível em `http://localhost:8080`.

Usuário inicial:
- E-mail: `admin@systock.local`
- Senha: `admin123`

## Estrutura do repositório

\`\`\`
systock/
├── backend/              # Aplicação Spring Boot
├── docs/                 # Documentação, diagramas, artigo
├── docker-compose.yml    # Orquestração
└── README.md
\`\`\`

## Autoria

Trabalho de Conclusão de Curso — FAM, 2026.
Autores: Natan Nascimento, Miguel Gracio, Kauã de Moura Contieri, Hugo Araújo de Andrade Souza, André Silva de Carvalho, João Victor Oliveira Santos.