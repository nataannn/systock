-- V4__criar_tabela_fornecedor.sql
-- Tabela de fornecedores do Systock.

CREATE TABLE fornecedor (
    id UUID PRIMARY KEY,

    cnpj VARCHAR(14) NOT NULL UNIQUE,

    razao_social VARCHAR(150) NOT NULL,

    nome_fantasia VARCHAR(150),

    email VARCHAR(120),

    telefone VARCHAR(20),

    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    criado_em TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_fornecedor_razao_social
    ON fornecedor (LOWER(razao_social));

CREATE INDEX idx_fornecedor_ativo
    ON fornecedor (ativo);