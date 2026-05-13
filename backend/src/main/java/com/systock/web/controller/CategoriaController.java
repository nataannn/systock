package com.systock.web.controller;

import com.systock.application.categoria.CategoriaService;
import com.systock.web.dto.CategoriaForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", service.listarTodas());
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new CategoriaForm());
        }
        return "categoria/lista";
    }

    @PostMapping
    public String criar(@Valid CategoriaForm form,
                        BindingResult resultado,
                        RedirectAttributes attrs) {
        if (resultado.hasErrors()) {
            attrs.addFlashAttribute("form", form);
            attrs.addFlashAttribute(
                    "org.springframework.validation.BindingResult.form", resultado);
            return "redirect:/categorias";
        }
        service.criar(form.getNome());
        attrs.addFlashAttribute("sucesso", "Categoria criada com sucesso.");
        return "redirect:/categorias";
    }

    @GetMapping("/{id}/editar")
    public String formularioEdicao(@PathVariable UUID id, Model model) {
        var categoria = service.buscarPorId(id);
        CategoriaForm form = new CategoriaForm();
        form.setNome(categoria.getNome());
        model.addAttribute("categoria", categoria);
        model.addAttribute("form", form);
        return "categoria/edicao";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable UUID id,
                            @Valid CategoriaForm form,
                            BindingResult resultado,
                            Model model,
                            RedirectAttributes attrs) {
        if (resultado.hasErrors()) {
            model.addAttribute("categoria", service.buscarPorId(id));
            return "categoria/edicao";
        }
        service.alterarNome(id, form.getNome());
        attrs.addFlashAttribute("sucesso", "Categoria atualizada com sucesso.");
        return "redirect:/categorias";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable UUID id, RedirectAttributes attrs) {
        service.excluir(id);
        attrs.addFlashAttribute("sucesso", "Categoria excluída com sucesso.");
        return "redirect:/categorias";
    }
}