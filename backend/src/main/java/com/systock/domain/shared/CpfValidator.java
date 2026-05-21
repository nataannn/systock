package com.systock.domain.shared;

public final class CpfValidator {

    private CpfValidator() {
    }

    public static boolean isValido(String cpf) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            return false;
        }
        if (cpf.chars().distinct().count() == 1) {
            return false;
        }
        try {
            int[] digitos = new int[11];
            for (int i = 0; i < 11; i++) {
                digitos[i] = Character.getNumericValue(cpf.charAt(i));
            }

            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += digitos[i] * (10 - i);
            }
            int resto = soma % 11;
            int dv1 = (resto < 2) ? 0 : 11 - resto;
            if (digitos[9] != dv1) {
                return false;
            }

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += digitos[i] * (11 - i);
            }
            resto = soma % 11;
            int dv2 = (resto < 2) ? 0 : 11 - resto;
            return digitos[10] == dv2;

        } catch (Exception e) {
            return false;
        }
    }
}
