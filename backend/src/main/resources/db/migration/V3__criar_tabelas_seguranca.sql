-- V3__criar_tabelas_seguranca.sql
-- Tabelas de autenticação e autorização: perfil e usuario.
-- Seeds: três perfis e um usuário administrador inicial.

CREATE TABLE perfil (
    id UUID PRIMARY KEY,
    nome VARCHAR(32) NOT NULL UNIQUE,
    descricao VARCHAR(255)
);

CREATE TABLE usuario (
    id UUID PRIMARY KEY,
    email VARCHAR(120) NOT NULL UNIQUE,
    senha_hash VARCHAR(100) NOT NULL,
    nome VARCHAR(120) NOT NULL,
    perfil_id UUID NOT NULL REFERENCES perfil(id),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_usuario_email ON usuario (email);

-- Seeds dos três perfis previstos no modelo
INSERT INTO perfil (id, nome, descricao) VALUES
    (gen_random_uuid(), 'ADMIN',       'Acesso total ao sistema'),
    (gen_random_uuid(), 'VENDEDOR',    'Atendimento e registro de vendas'),
    (gen_random_uuid(), 'ESTOQUISTA',  'Gestão de estoque e produtos');

-- Usuário administrador inicial.
-- Senha em texto plano: admin123 (TROCAR NO PRIMEIRO LOGIN)
-- Hash BCrypt fator 10 — substitua pelo hash que você gerou via htpasswd.
INSERT INTO usuario (id, email, senha_hash, nome, perfil_id)
SELECT
    gen_random_uuid(),
    'admin@systock.local',
    '$2y$10$/AdgguH/xmUaqRyroI04v.Z2PkUzYmPqsz3s3zf6bPgyzekeB7X0C',
    'Administrador',
    p.id
FROM perfil p
WHERE p.nome = 'ADMIN';