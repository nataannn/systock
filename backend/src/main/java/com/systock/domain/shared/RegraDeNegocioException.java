package com.systock.domain.shared;

public class RegraDeNegocioException extends RuntimeException {

    private final String redirectPath;

    public RegraDeNegocioException(String mensagem) {
        this(mensagem, null);
    }

    public RegraDeNegocioException(String mensagem, String redirectPath) {
        super(mensagem);
        this.redirectPath = redirectPath;
    }

    public String getRedirectPath() {
        return redirectPath;
    }
}
