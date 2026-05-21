-- V7__criar_tabelas_venda.sql
-- Vendas compostas com itens e vínculo a cliente/usuário.

CREATE SEQUENCE seq_venda_numero START WITH 1 INCREMENT BY 1;

CREATE TABLE venda (
    id UUID PRIMARY KEY,

    numero BIGINT NOT NULL UNIQUE DEFAULT nextval('seq_venda_numero'),

    cliente_id UUID REFERENCES cliente (id),

    usuario_id UUID NOT NULL REFERENCES usuario (id),

    valor_total NUMERIC(12, 2) NOT NULL CHECK (valor_total >= 0),

    status VARCHAR(20) NOT NULL DEFAULT 'FINALIZADA'
        CHECK (status IN ('FINALIZADA', 'CANCELADA')),

    realizado_em TIMESTAMP WITH TIME ZONE NOT NULL,

    criado_em TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE item_venda (
    id UUID PRIMARY KEY,

    venda_id UUID NOT NULL REFERENCES venda (id) ON DELETE CASCADE,

    produto_id UUID NOT NULL REFERENCES produto (id),

    quantidade INTEGER NOT NULL CHECK (quantidade > 0),

    preco_unitario NUMERIC(12, 2) NOT NULL CHECK (preco_unitario >= 0),

    subtotal NUMERIC(12, 2) NOT NULL CHECK (subtotal >= 0)
);

CREATE INDEX idx_venda_cliente ON venda (cliente_id);
CREATE INDEX idx_venda_usuario ON venda (usuario_id);
CREATE INDEX idx_venda_realizado_em ON venda (realizado_em DESC);
CREATE INDEX idx_item_venda_venda ON item_venda (venda_id);
CREATE INDEX idx_item_venda_produto ON item_venda (produto_id);
