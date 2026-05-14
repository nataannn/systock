package com.systock.web.controller;

import com.systock.application.fornecedor.FornecedorService;
import com.systock.web.dto.FornecedorForm;
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

import java.util.UUID;

@Controller
@RequestMapping("/fornecedores")
public class FornecedorController {

    private final FornecedorService service;

    public FornecedorController(FornecedorService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(@RequestParam(name = "incluirInativos", required = false) Boolean incluirInativos,
                         Model model) {
        boolean mostrarTodos = Boolean.TRUE.equals(incluirInativos);
        model.addAttribute("fornecedores",
                mostrarTodos ? service.listarTodos() : service.listarAtivos());
        model.addAttribute("incluirInativos", mostrarTodos);
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new FornecedorForm());
        }
        return "fornecedor/lista";
    }

    @PostMapping
    public String criar(@Valid FornecedorForm form,
                        BindingResult resultado,
                        RedirectAttributes attrs) {
        if (resultado.hasErrors()) {
            attrs.addFlashAttribute("form", form);
            attrs.addFlashAttribute(
                    "org.springframework.validation.BindingResult.form", resultado);
            return "redirect:/fornecedores";
        }
        service.criar(form.getCnpj(), form.getRazaoSocial(),
                form.getNomeFantasia(), form.getEmail(), form.getTelefone());
        attrs.addFlashAttribute("sucesso", "Fornecedor cadastrado com sucesso.");
        return "redirect:/fornecedores";
    }

    @GetMapping("/{id}/editar")
    public String formularioEdicao(@PathVariable UUID id, Model model) {
        var fornecedor = service.buscarPorId(id);
        FornecedorForm form = new FornecedorForm();
        form.setCnpj(fornecedor.getCnpj());
        form.setRazaoSocial(fornecedor.getRazaoSocial());
        form.setNomeFantasia(fornecedor.getNomeFantasia());
        form.setEmail(fornecedor.getEmail());
        form.setTelefone(fornecedor.getTelefone());
        model.addAttribute("fornecedor", fornecedor);
        model.addAttribute("form", form);
        return "fornecedor/edicao";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable UUID id,
                            @Valid FornecedorForm form,
                            BindingResult resultado,
                            Model model,
                            RedirectAttributes attrs) {
        if (resultado.hasErrors()) {
            model.addAttribute("fornecedor", service.buscarPorId(id));
            return "fornecedor/edicao";
        }
        service.atualizar(id, form.getRazaoSocial(), form.getNomeFantasia(),
                form.getEmail(), form.getTelefone());
        attrs.addFlashAttribute("sucesso", "Fornecedor atualizado com sucesso.");
        return "redirect:/fornecedores";
    }

    @PostMapping("/{id}/desativar")
    public String desativar(@PathVariable UUID id, RedirectAttributes attrs) {
        service.desativar(id);
        attrs.addFlashAttribute("sucesso", "Fornecedor desativado com sucesso.");
        return "redirect:/fornecedores";
    }

    @PostMapping("/{id}/ativar")
    public String ativar(@PathVariable UUID id, RedirectAttributes attrs) {
        service.ativar(id);
        attrs.addFlashAttribute("sucesso", "Fornecedor reativado com sucesso.");
        return "redirect:/fornecedores";
    }
}