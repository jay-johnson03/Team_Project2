package project2.project2.utils;

import org.mindrot.jbcrypt.BCrypt;

public class HashingUtil {

  public static String hashPassword(String password) {
    System.out.println("Hashing...");
    String salt = BCrypt.gensalt();
    return BCrypt.hashpw(password, salt);
  }
}
