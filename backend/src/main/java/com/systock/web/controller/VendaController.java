package com.systock.web.controller;

import com.systock.application.cliente.ClienteService;
import com.systock.application.produto.ProdutoService;
import com.systock.application.venda.ItemVendaCommand;
import com.systock.application.venda.VendaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    public VendaController(VendaService vendaService,
                           ClienteService clienteService,
                           ProdutoService produtoService) {
        this.vendaService = vendaService;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vendas", vendaService.listarTodas());
        return "venda/lista";
    }

    @GetMapping("/nova")
    public String formularioNova(Model model) {
        carregarOpcoes(model);
        return "venda/nova";
    }

    @PostMapping
    public String registrar(@RequestParam(required = false) UUID clienteId,
                            @RequestParam(required = false) List<UUID> produtoIds,
                            @RequestParam(required = false) List<Integer> quantidades,
                            RedirectAttributes attrs) {
        List<ItemVendaCommand> itens = montarItens(produtoIds, quantidades);
        var venda = vendaService.registrar(clienteId, itens);
        attrs.addFlashAttribute("sucesso",
                "Venda #" + venda.getNumero() + " registrada com sucesso.");
        return "redirect:/vendas/" + venda.getId();
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable UUID id, Model model) {
        model.addAttribute("venda", vendaService.buscarPorId(id));
        return "venda/detalhe";
    }

    private List<ItemVendaCommand> montarItens(List<UUID> produtoIds, List<Integer> quantidades) {
        List<ItemVendaCommand> itens = new ArrayList<>();
        if (produtoIds == null || quantidades == null) {
            return itens;
        }
        int tamanho = Math.min(produtoIds.size(), quantidades.size());
        for (int i = 0; i < tamanho; i++) {
            UUID produtoId = produtoIds.get(i);
            Integer quantidade = quantidades.get(i);
            if (produtoId != null && quantidade != null && quantidade > 0) {
                itens.add(new ItemVendaCommand(produtoId, quantidade));
            }
        }
        return itens;
    }

    private void carregarOpcoes(Model model) {
        model.addAttribute("clientesDisponiveis", clienteService.listarAtivos());
        model.addAttribute("produtosDisponiveis", produtoService.listarAtivos());
    }
}
