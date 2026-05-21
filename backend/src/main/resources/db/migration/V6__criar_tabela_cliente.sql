-- V6__criar_tabela_cliente.sql
-- Cadastro de clientes com dados sensíveis criptografados (LGPD).

CREATE TABLE cliente (
    id UUID PRIMARY KEY,

    nome VARCHAR(150) NOT NULL,

    cpf_criptografado TEXT NOT NULL,
    cpf_hash VARCHAR(64) NOT NULL,

    email_criptografado TEXT,
    email_hash VARCHAR(64),

    telefone VARCHAR(20),

    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    criado_em TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE UNIQUE INDEX uk_cliente_cpf_hash ON cliente (cpf_hash);
CREATE UNIQUE INDEX uk_cliente_email_hash ON cliente (email_hash) WHERE email_hash IS NOT NULL;

CREATE INDEX idx_cliente_nome ON cliente (LOWER(nome));
CREATE INDEX idx_cliente_ativo ON cliente (ativo);
