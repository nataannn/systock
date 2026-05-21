package com.systock.web;

import com.systock.domain.shared.RegraDeNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegraDeNegocioException.class)
    public RedirectView tratarRegraDeNegocio(RegraDeNegocioException ex,
                                              RedirectAttributes attrs,
                                              HttpServletRequest request) {
        attrs.addFlashAttribute("erro", ex.getMessage());
        String destino = ex.getRedirectPath();
        if (destino == null) {
            destino = extrairPathDoReferer(request);
        }
        if (destino == null) {
            destino = "/";
        }
        return new RedirectView(destino);
    }

    /**
     * Quando o service não informa redirectPath, volta para a página de origem (ex.: /fornecedores).
     */
    private String extrairPathDoReferer(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isBlank()) {
            return null;
        }
        try {
            URI uri = URI.create(referer);
            String path = uri.getPath();
            if (path != null && path.startsWith("/") && !path.startsWith("//")) {
                return path;
            }
        } catch (IllegalArgumentException ignored) {
            // Referer inválido — ignora
        }
        return null;
    }
}
