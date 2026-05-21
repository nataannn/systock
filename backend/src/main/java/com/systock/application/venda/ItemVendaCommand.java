package com.systock.application.venda;

import java.util.UUID;

public record ItemVendaCommand(UUID produtoId, int quantidade) {
}
