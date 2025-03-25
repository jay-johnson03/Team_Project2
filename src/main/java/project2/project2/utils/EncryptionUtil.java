package project2.project2.utils;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {

  // AES is a symmetric encryption algorithm
  // in the real world, you would not hardcode the key
  // you would store it in a secure location
  // and use a key management system
  private static final String AES = "AES";
  private static final String SECRET_KEY = "verysecretkeymhm"; // secret key length must be 16, 24, or 32 bytes eg. 16, 24, or 32 characters

// create a key with the secret key -> create a cipher with the AES algorithm -> initialize the cipher with the key and the mode -> 
// encrypt the string -> encode the encrypted string to base64 string
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

  // create a key with the secret key -> create a cipher with the AES algorithm -> initialize the cipher with the key and the mode ->
  // decode the string from base64 -> decrypt the string
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
