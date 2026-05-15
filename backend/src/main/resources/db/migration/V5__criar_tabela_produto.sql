-- V5__criar_tabela_produto.sql
-- Tabela de produtos com relacionamentos para categoria e fornecedor.

CREATE TABLE produto (
    id UUID PRIMARY KEY,

    codigo VARCHAR(40) NOT NULL UNIQUE,
    nome VARCHAR(150) NOT NULL,
    descricao VARCHAR(500),

    preco_custo NUMERIC(12, 2) NOT NULL CHECK (preco_custo >= 0),
    preco_venda NUMERIC(12, 2) NOT NULL CHECK (preco_venda >= 0),

    estoque_atual INTEGER NOT NULL DEFAULT 0 CHECK (estoque_atual >= 0),
    estoque_minimo INTEGER NOT NULL DEFAULT 0 CHECK (estoque_minimo >= 0),

    categoria_id UUID NOT NULL REFERENCES categoria(id),
    fornecedor_id UUID NOT NULL REFERENCES fornecedor(id),

    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_produto_nome ON produto (LOWER(nome));
CREATE INDEX idx_produto_categoria ON produto (categoria_id);
CREATE INDEX idx_produto_fornecedor ON produto (fornecedor_id);
CREATE INDEX idx_produto_estoque_critico ON produto (id) WHERE estoque_atual <= estoque_minimo;