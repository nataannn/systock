package com.systock.infrastructure.crypto;

import com.systock.domain.shared.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class DadosSensiveisCryptoService {

    private static final String ALGORITMO = "AES/GCM/NoPadding";
    private static final int IV_BYTES = 12;
    private static final int TAG_BITS = 128;

    private final SecretKey chave;
    private final SecureRandom secureRandom = new SecureRandom();

    public DadosSensiveisCryptoService(
            @Value("${systock.crypto.secret-key}") String secretKey) {
        byte[] bytes = secretKey.getBytes(StandardCharsets.UTF_8);
        if (bytes.length != 32) {
            throw new IllegalStateException(
                    "systock.crypto.secret-key deve ter exatamente 32 bytes (AES-256)");
        }
        this.chave = new SecretKeySpec(bytes, "AES");
    }

    public String criptografar(String textoPlano) {
        if (textoPlano == null || textoPlano.isBlank()) {
            return null;
        }
        try {
            byte[] iv = new byte[IV_BYTES];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, chave, new GCMParameterSpec(TAG_BITS, iv));

            byte[] cipherText = cipher.doFinal(textoPlano.getBytes(StandardCharsets.UTF_8));

            ByteBuffer buffer = ByteBuffer.allocate(iv.length + cipherText.length);
            buffer.put(iv);
            buffer.put(cipherText);

            return Base64.getEncoder().encodeToString(buffer.array());
        } catch (Exception e) {
            throw new RegraDeNegocioException("Falha ao proteger dado sensível");
        }
    }

    public String descriptografar(String textoCriptografado) {
        if (textoCriptografado == null || textoCriptografado.isBlank()) {
            return null;
        }
        try {
            byte[] payload = Base64.getDecoder().decode(textoCriptografado);
            ByteBuffer buffer = ByteBuffer.wrap(payload);

            byte[] iv = new byte[IV_BYTES];
            buffer.get(iv);

            byte[] cipherText = new byte[buffer.remaining()];
            buffer.get(cipherText);

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, chave, new GCMParameterSpec(TAG_BITS, iv));

            byte[] plain = cipher.doFinal(cipherText);
            return new String(plain, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RegraDeNegocioException("Falha ao recuperar dado sensível");
        }
    }
}
