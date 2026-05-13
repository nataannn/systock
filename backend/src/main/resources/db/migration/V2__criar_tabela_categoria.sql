-- V2__criar_tabela_categoria.sql
-- Primeira tabela de domínio do Systock: Categoria.

CREATE TABLE categoria (
    id UUID PRIMARY KEY,
    nome VARCHAR(80) NOT NULL UNIQUE
);

CREATE INDEX idx_categoria_nome ON categoria (LOWER(nome));