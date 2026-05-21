package com.systock.web.controller;

import com.systock.application.cliente.ClienteService;
import com.systock.web.dto.ClienteForm;
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
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(@RequestParam(name = "incluirInativos", required = false) Boolean incluirInativos,
                         Model model) {
        boolean mostrarTodos = Boolean.TRUE.equals(incluirInativos);
        model.addAttribute("clientes",
                mostrarTodos ? service.listarTodos() : service.listarAtivos());
        model.addAttribute("incluirInativos", mostrarTodos);
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new ClienteForm());
        }
        return "cliente/lista";
    }

    @PostMapping
    public String criar(@Valid ClienteForm form,
                        BindingResult resultado,
                        RedirectAttributes attrs) {
        if (resultado.hasErrors()) {
            attrs.addFlashAttribute("form", form);
            attrs.addFlashAttribute(
                    "org.springframework.validation.BindingResult.form", resultado);
            return "redirect:/clientes";
        }
        service.criar(form.getCpf(), form.getNome(), form.getEmail(), form.getTelefone());
        attrs.addFlashAttribute("sucesso", "Cliente cadastrado com sucesso.");
        return "redirect:/clientes";
    }

    @GetMapping("/{id}/editar")
    public String formularioEdicao(@PathVariable UUID id, Model model) {
        var cliente = service.buscarPorId(id);
        ClienteForm form = new ClienteForm();
        form.setCpf(cliente.getCpf());
        form.setNome(cliente.getNome());
        form.setEmail(cliente.getEmail());
        form.setTelefone(cliente.getTelefone());
        model.addAttribute("cliente", cliente);
        model.addAttribute("form", form);
        return "cliente/edicao";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable UUID id,
                            @Valid ClienteForm form,
                            BindingResult resultado,
                            Model model,
                            RedirectAttributes attrs) {
        if (resultado.hasErrors()) {
            model.addAttribute("cliente", service.buscarPorId(id));
            return "cliente/edicao";
        }
        service.atualizar(id, form.getCpf(), form.getNome(), form.getEmail(), form.getTelefone());
        attrs.addFlashAttribute("sucesso", "Cliente atualizado com sucesso.");
        return "redirect:/clientes";
    }

    @PostMapping("/{id}/desativar")
    public String desativar(@PathVariable UUID id, RedirectAttributes attrs) {
        service.desativar(id);
        attrs.addFlashAttribute("sucesso", "Cliente desativado com sucesso.");
        return "redirect:/clientes";
    }

    @PostMapping("/{id}/ativar")
    public String ativar(@PathVariable UUID id, RedirectAttributes attrs) {
        service.ativar(id);
        attrs.addFlashAttribute("sucesso", "Cliente reativado com sucesso.");
        return "redirect:/clientes";
    }
}
