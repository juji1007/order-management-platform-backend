package com.nineteen.omp.global.utils;

public interface Encryptor {

  String encrypt(String plainText) throws Exception;

  String decrypt(String cipherText) throws Exception;
}
