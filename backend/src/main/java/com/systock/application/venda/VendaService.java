package com.systock.application.venda;

import com.systock.domain.cliente.Cliente;
import com.systock.domain.estoque.MovimentacaoEstoque;
import com.systock.domain.produto.Produto;
import com.systock.domain.shared.RegraDeNegocioException;
import com.systock.domain.usuario.Usuario;
import com.systock.domain.venda.Venda;
import com.systock.infrastructure.persistence.ClienteRepository;
import com.systock.infrastructure.persistence.MovimentacaoEstoqueRepository;
import com.systock.infrastructure.persistence.ProdutoRepository;
import com.systock.infrastructure.persistence.UsuarioRepository;
import com.systock.infrastructure.persistence.VendaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    public VendaService(VendaRepository vendaRepository,
                        ProdutoRepository produtoRepository,
                        ClienteRepository clienteRepository,
                        UsuarioRepository usuarioRepository,
                        MovimentacaoEstoqueRepository movimentacaoRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Transactional(readOnly = true)
    public List<Venda> listarTodas() {
        return vendaRepository.listarTodasComClienteEUsuario();
    }

    @Transactional(readOnly = true)
    public Venda buscarPorId(UUID id) {
        return vendaRepository.buscarDetalhadaPorId(id)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Venda não encontrada: " + id, "/vendas"));
    }

    @Transactional
    public Venda registrar(UUID clienteId, List<ItemVendaCommand> itensComando) {
        List<ItemVendaCommand> itensValidos = filtrarItensValidos(itensComando);
        if (itensValidos.isEmpty()) {
            throw new RegraDeNegocioException(
                    "Informe ao menos um item com quantidade positiva", "/vendas/nova");
        }

        Usuario usuario = obterUsuarioLogado();
        Cliente cliente = resolverCliente(clienteId);

        Venda venda = new Venda(usuario, cliente);
        Map<UUID, Produto> produtosCarregados = new HashMap<>();

        for (ItemVendaCommand comando : itensValidos) {
            Produto produto = produtosCarregados.computeIfAbsent(
                    comando.produtoId(),
                    id -> produtoRepository.findById(id)
                            .orElseThrow(() -> new RegraDeNegocioException(
                                    "Produto não encontrado: " + id, "/vendas/nova")));
            venda.adicionarItem(produto, comando.quantidade());
        }

        venda.finalizar();
        Venda salva = vendaRepository.save(venda);

        for (var item : salva.getItens()) {
            movimentacaoRepository.save(new MovimentacaoEstoque(
                    item.getProduto(),
                    MovimentacaoEstoque.TipoMovimentacao.SAIDA,
                    item.getQuantidade(),
                    MovimentacaoEstoque.ReferenciaTipo.VENDA,
                    salva.getId(),
                    usuario));
        }

        return vendaRepository.buscarDetalhadaPorId(salva.getId()).orElse(salva);
    }

    private List<ItemVendaCommand> filtrarItensValidos(List<ItemVendaCommand> itensComando) {
        if (itensComando == null) {
            return List.of();
        }
        List<ItemVendaCommand> validos = new ArrayList<>();
        for (ItemVendaCommand item : itensComando) {
            if (item != null && item.produtoId() != null && item.quantidade() > 0) {
                validos.add(item);
            }
        }
        return validos;
    }

    private Cliente resolverCliente(UUID clienteId) {
        if (clienteId == null) {
            return null;
        }
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Cliente selecionado não existe", "/vendas/nova"));
        if (!cliente.isAtivo()) {
            throw new RegraDeNegocioException(
                    "Cliente selecionado está inativo", "/vendas/nova");
        }
        return cliente;
    }

    private Usuario obterUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RegraDeNegocioException(
                        "Usuário da sessão não encontrado", "/vendas/nova"));
    }
}
