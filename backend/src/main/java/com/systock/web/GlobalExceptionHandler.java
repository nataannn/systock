package com.systock.web;

import com.systock.domain.shared.RegraDeNegocioException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegraDeNegocioException.class)
    public RedirectView tratarRegraDeNegocio(RegraDeNegocioException ex,
                                              RedirectAttributes attrs) {
        attrs.addFlashAttribute("erro", ex.getMessage());
        String destino = ex.getRedirectPath() != null ? ex.getRedirectPath() : "/";
        return new RedirectView(destino);
    }
}