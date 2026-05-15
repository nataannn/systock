package com.systock.web.controller;

import com.systock.application.categoria.CategoriaService;
import com.systock.application.fornecedor.FornecedorService;
import com.systock.application.produto.ProdutoService;
import com.systock.web.dto.ProdutoForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.UUID;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;
    private final FornecedorService fornecedorService;

    public ProdutoController(ProdutoService produtoService,
                              CategoriaService categoriaService,
                              FornecedorService fornecedorService) {
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
        this.fornecedorService = fornecedorService;
    }

    @GetMapping
    public String listar(@RequestParam(name = "incluirInativos", required = false) Boolean incluirInativos,
                         Model model) {
        boolean mostrarTodos = Boolean.TRUE.equals(incluirInativos);
        model.addAttribute("produtos",
                mostrarTodos ? produtoService.listarTodos() : produtoService.listarAtivos());
        model.addAttribute("incluirInativos", mostrarTodos);
        return "produto/lista";
    }

    @GetMapping("/novo")
    public String formularioNovo(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new ProdutoForm());
        }
        carregarOpcoes(model);
        return "produto/cadastro";
    }

    @PostMapping
    public String criar(@Valid @ModelAttribute("form") ProdutoForm form,
                        BindingResult resultado,
                        Model model,
                        RedirectAttributes attrs) {
        if (resultado.hasErrors()) {
            carregarOpcoes(model);
            return "produto/cadastro";
        }
        produtoService.criar(
                form.getCodigo(), form.getNome(), form.getDescricao(),
                form.getPrecoCusto(), form.getPrecoVenda(),
                form.getEstoqueAtual(), form.getEstoqueMinimo(),
                form.getCategoriaId(), form.getFornecedorId());
        attrs.addFlashAttribute("sucesso", "Produto cadastrado com sucesso.");
        return "redirect:/produtos";
    }

    @GetMapping("/{id}/editar")
    public String formularioEdicao(@PathVariable UUID id, Model model) {
        var produto = produtoService.buscarPorId(id);

        ProdutoForm form = new ProdutoForm();
        form.setCodigo(produto.getCodigo());
        form.setNome(produto.getNome());
        form.setDescricao(produto.getDescricao());
        form.setPrecoCusto(produto.getPrecoCusto());
        form.setPrecoVenda(produto.getPrecoVenda());
        form.setEstoqueAtual(produto.getEstoqueAtual());
        form.setEstoqueMinimo(produto.getEstoqueMinimo());
        form.setCategoriaId(produto.getCategoria().getId());
        form.setFornecedorId(produto.getFornecedor().getId());

        model.addAttribute("produto", produto);
        model.addAttribute("form", form);
        carregarOpcoes(model);
        return "produto/edicao";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable UUID id,
                            @Valid @ModelAttribute("form") ProdutoForm form,
                            BindingResult resultado,
                            Model model,
                            RedirectAttributes attrs) {
        if (resultado.hasErrors()) {
            model.addAttribute("produto", produtoService.buscarPorId(id));
            carregarOpcoes(model);
            return "produto/edicao";
        }
        produtoService.atualizar(
                id, form.getNome(), form.getDescricao(),
                form.getPrecoCusto(), form.getPrecoVenda(),
                form.getEstoqueMinimo(),
                form.getCategoriaId(), form.getFornecedorId());
        attrs.addFlashAttribute("sucesso", "Produto atualizado com sucesso.");
        return "redirect:/produtos";
    }

    @PostMapping("/{id}/desativar")
    public String desativar(@PathVariable UUID id, RedirectAttributes attrs) {
        produtoService.desativar(id);
        attrs.addFlashAttribute("sucesso", "Produto desativado.");
        return "redirect:/produtos";
    }

    @PostMapping("/{id}/ativar")
    public String ativar(@PathVariable UUID id, RedirectAttributes attrs) {
        produtoService.ativar(id);
        attrs.addFlashAttribute("sucesso", "Produto reativado.");
        return "redirect:/produtos";
    }

    @PostMapping("/{id}/entrada-estoque")
    public String entradaEstoque(@PathVariable UUID id,
                                  @RequestParam int quantidade,
                                  RedirectAttributes attrs) {
        produtoService.darEntradaEstoque(id, quantidade);
        attrs.addFlashAttribute("sucesso", "Entrada de " + quantidade + " unidade(s) registrada.");
        return "redirect:/produtos";
    }

    private void carregarOpcoes(Model model) {
        model.addAttribute("categoriasDisponiveis", categoriaService.listarTodas());
        model.addAttribute("fornecedoresDisponiveis", fornecedorService.listarAtivos());
    }
}