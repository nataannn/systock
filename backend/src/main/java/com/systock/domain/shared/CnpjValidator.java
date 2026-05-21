package com.systock.domain.shared;

public class CnpjValidator {

    private CnpjValidator() {
    }

    /**
     * Valida CNPJ pelo cálculo dos dígitos verificadores (módulo 11).
     * Espera CNPJ com 14 dígitos, sem máscara.
     */
    public static boolean isValido(String cnpj) {
        if (cnpj == null || cnpj.length() != 14 || !cnpj.matches("\\d{14}")) {
            return false;
        }
        // Rejeita CNPJs com todos os dígitos iguais (00000000000000, 11111111111111, etc.)
        if (cnpj.chars().distinct().count() == 1) {
            return false;
        }
        try {
            int[] digitos = new int[14];
            for (int i = 0; i < 14; i++) {
                digitos[i] = Character.getNumericValue(cnpj.charAt(i));
            }

            // Cálculo do primeiro dígito verificador
            int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += digitos[i] * pesos1[i];
            }
            int resto = soma % 11;
            int dv1 = (resto < 2) ? 0 : 11 - resto;
            if (digitos[12] != dv1) {
                return false;
            }

            // Cálculo do segundo dígito verificador
            int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            soma = 0;
            for (int i = 0; i < 13; i++) {
                soma += digitos[i] * pesos2[i];
            }
            resto = soma % 11;
            int dv2 = (resto < 2) ? 0 : 11 - resto;
            return digitos[13] == dv2;

        } catch (Exception e) {
            return false;
        }
    }
}