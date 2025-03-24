package project2.project2.utils;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {

  private static final String AES = "AES";
  private static final String SECRET_KEY = "verysecretkeymhm";

  public static String encrypt(String string) {
    System.out.println("Encrypting...");
    try {
      SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), AES);
      Cipher cipher = Cipher.getInstance(AES);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] encryptedString = cipher.doFinal(string.getBytes());
      return Base64.getEncoder().encodeToString(encryptedString);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String decrypt(String string) {
    System.out.println("Decrypting...");
    try {
      SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), AES);
      Cipher cipher = Cipher.getInstance(AES);
      cipher.init(Cipher.DECRYPT_MODE, key);
      byte[] decodedString = Base64.getDecoder().decode(string);
      byte[] decryptedString = cipher.doFinal(decodedString);
      return new String(decryptedString);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
