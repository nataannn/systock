-- V1__init.sql
-- Primeira migration: cria estrutura inicial.
-- Tabelas de domínio entram em V2 (Onda 2).

CREATE TABLE IF NOT EXISTS app_meta (
    chave VARCHAR(64) PRIMARY KEY,
    valor VARCHAR(255) NOT NULL,
    atualizado_em TIMESTAMP NOT NULL DEFAULT NOW()
);

INSERT INTO app_meta (chave, valor) VALUES
    ('schema_version', 'v1'),
    ('initialized_at', NOW()::text)
ON CONFLICT (chave) DO NOTHING;
