-- V8__criar_tabela_movimentacao_estoque.sql
-- Rastreabilidade de movimentações de estoque.

CREATE TABLE movimentacao_estoque (
    id UUID PRIMARY KEY,

    produto_id UUID NOT NULL REFERENCES produto (id),

    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('ENTRADA', 'SAIDA')),

    quantidade INTEGER NOT NULL CHECK (quantidade > 0),

    referencia_tipo VARCHAR(20),
    referencia_id UUID,

    realizado_em TIMESTAMP WITH TIME ZONE NOT NULL,

    usuario_id UUID REFERENCES usuario (id)
);

CREATE INDEX idx_movimentacao_produto ON movimentacao_estoque (produto_id);
CREATE INDEX idx_movimentacao_referencia ON movimentacao_estoque (referencia_tipo, referencia_id);
