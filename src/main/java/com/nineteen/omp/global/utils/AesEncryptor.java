package com.nineteen.omp.global.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Qualifier("AES")
public class AesEncryptor implements Encryptor {

  private static final String AES_ALGORITHM = "AES";//암호화 알고리즘
  private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
  private final SecretKey secretKey;

  public AesEncryptor(
      @Value("${encryptor.aes.key}") String aesKey
  ) {
    this.secretKey =
        new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
    ;
  }

  @Override
  public String encrypt(String plainText) throws Exception {
    byte[] encryptedBytes;

    Cipher cipher = getCipherInstance();
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(encryptedBytes);
  }

  @Override
  public String decrypt(String cipherText) throws Exception {
    byte[] decryptedBytes;

    Cipher cipher = getCipherInstance();
    cipher.init(Cipher.DECRYPT_MODE, secretKey);

    byte[] encryptedBytes = Base64.getDecoder().decode(cipherText);
    decryptedBytes = cipher.doFinal(encryptedBytes);
    return new String(decryptedBytes, StandardCharsets.UTF_8);
  }

  private Cipher getCipherInstance() throws Exception {
    return Cipher.getInstance(AES_TRANSFORMATION);
  }
}
