package com.systock.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String erro,
                        @RequestParam(required = false) String saiu,
                        Model model) {
        if (erro != null) {
            model.addAttribute("mensagemErro", "E-mail ou senha inválidos.");
        }
        if (saiu != null) {
            model.addAttribute("mensagemSucesso", "Você saiu com segurança.");
        }
        return "login";
    }
}