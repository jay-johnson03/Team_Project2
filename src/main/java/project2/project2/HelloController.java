package project2.project2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.mindrot.jbcrypt.BCrypt;

public class HelloController {

  @FXML
  private TextField emailField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private TextField nameField;

  private static final String AES = "AES"; // encryption algorithm
  private static final String SECRET_KEY = "verysecretkeymhm"; // secret key

  private String database = "superSecureDb.csv"; // very secure

  @FXML
  private void signIn(ActionEvent event) {
    String email = this.emailField.getText();
    String password = this.passwordField.getText();
    String encryptedEmail = encrypt(email); // update this
    String salt = BCrypt.gensalt();
    String hashedPassword = BCrypt.hashpw(password, salt);
    String decryptedEmail = decrypt(encryptedEmail);

    // debug
    // System.out.println(email);
    // System.out.println(encryptedEmail);
    // System.out.println(decryptedEmail);
    System.out.println(password);
    System.out.println(hashedPassword);
    // System.out.println(salt);
    boolean passwordMatches = BCrypt.checkpw(password, hashedPassword);
    System.out.println(passwordMatches);
    /*
     * 121212
     * 232323
     * $2a$10$5t1hFMY5JxN7pzUBCtukgO07wrnoTgNHlEow51yrgLvUYS/WkIV8K
     * $2a$10$5t1hFMY5JxN7pzUBCtukgO
     * true
     *
     * $2a$ is the version of the algorithm
     * 10$ is the cost factor, eg. 2^10 iterations
     * 5t1hFMY5JxN7pzUBCtukgO is the salt
     */

    checkFile(encryptedEmail, password); // check if account exists
  }

  ///////////////////////////////////// HANDLE SIGN IN INPUT /////////////////////////////////////

  private void checkFile(String email, String password) {
    System.out.println("Checking file");
    try (
      BufferedReader reader = new BufferedReader(new FileReader(this.database))
    ) { // read file
      String line;
      reader.readLine(); // skip header
      Boolean accountFound = false;
      while ((line = reader.readLine()) != null) { // read line by line
        String[] data = line.split(","); // split by comma
        // dencrpyt and unhash then check
        System.out.println("Password: " + BCrypt.checkpw(password, data[1]));
        System.out.println(
          "Email: " + (decrypt(data[0]).equals(decrypt(email)))
        );
        if (
          BCrypt.checkpw(password, data[1]) &&
          (decrypt(data[0]).equals(decrypt(email)))
        ) { // check if password matches
          System.out.println("Account found: " + data.toString());
          accountFound = true;
          // open login view
          openLogInView(null, data[2]);
          break;
        } else { // account not found
          System.out.println("Account does not match");
        }
      }
      if (!accountFound) { // account not found, create account
        promptSignUp(null);
      }
    } catch (Exception e) {
      e.printStackTrace();
      // error pop up
    }
  }

  private void createFile(String email, String password, String name) { // create file
    System.out.println("Creating file");
    try {
      FileWriter writer = new FileWriter(this.database, true);
      writer.write("Email" + "," + "Password" + "," + "Name" + "\n"); // write header
      writer.write(email + "," + password + "," + encrypt(name)  + "\n"); // write data
      writer.close();
      System.out.println(
        "File created with: " + email + "," + password + "," + name
      );
    } catch (Exception e) {
      e.printStackTrace();
      // error pop up
    }
  }

  private void createAccount(String email, String password, String name) {
    // this is also where we will need to create the file
    System.out.println("Creating account");
    if (Files.exists(Paths.get(this.database))) { // check if file exists
      System.out.println("File exists");
      try (FileWriter writer = new FileWriter(this.database, true)) { // write to file
        writer.write(email + "," + password + "," + encrypt(name) + "\n");
        System.out.println(
          "Account created with: " + email + "," + password + "," + name
        );
      } catch (Exception e) {
        e.printStackTrace();
        // error pop up
      }
    } else { // file does not exist
      System.out.println("File does not exist");
      createFile(email, password, name);
      // createFile(email, password, name);
    }
  }

  ///////////////////////////////////// HANDLE SIGN IN INPUT /////////////////////////////////////

  ///////////////////////////////////// ENCRYPTION /////////////////////////////////////

  private String encrypt(String string) {
    // System.out.println("Encrypting: " + string);
    try {
      SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), AES); // create secret key with secret key and
      // algorithm
      Cipher cipher = Cipher.getInstance(AES); // create cipher with algorithm
      cipher.init(Cipher.ENCRYPT_MODE, key); // initialize cipher with secret key
      byte[] encryptedString = cipher.doFinal(string.getBytes()); // encrypt string
      // System.out.println("Encrypted: " +
      // Base64.getEncoder().encodeToString(encryptedString));
      return Base64.getEncoder().encodeToString(encryptedString); // return encrypted string, translated to string
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private String decrypt(String string) {
    // System.out.println("Decrypting: " + string);
    try {
      SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), AES); // create secret key with secret key and
      // algorithm
      Cipher cipher = Cipher.getInstance(AES); // create cipher with algorithm
      cipher.init(Cipher.DECRYPT_MODE, key); // initialize cipher with secret key
      byte[] decodedString = Base64.getDecoder().decode(string); // decode string
      byte[] decryptedString = cipher.doFinal(decodedString); // decrypt string
      // System.out.println("Decrypted: " + new String(decryptedString));
      return new String(decryptedString);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  ///////////////////////////////////// ENCRYPTION /////////////////////////////////////

  ///////////////////////////////////// SIGN UP /////////////////////////////////////

  @FXML
  private void promptSignUp(ActionEvent event) { // prompt user to sign up with sigh up view
    System.out.println("Prompting Sign Up");
    try {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("signUp-view.fxml")
      );
      Stage stage = new Stage();
      stage.setScene(new Scene(loader.load()));
      stage.setTitle("Sign Up");
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void signUp(ActionEvent event) { // handle sign up from sign up view
    String email = this.emailField.getText();
    String password = this.passwordField.getText();
    String name = this.nameField.getText();
    String encryptedEmail = encrypt(email);
    String salt = BCrypt.gensalt();
    String hashedPassword = BCrypt.hashpw(password, salt);

    createAccount(encryptedEmail, hashedPassword, name);

    Stage stage = (Stage) this.emailField.getScene().getWindow();
    stage.close(); // close sign up view when done
  }
  ///////////////////////////////////// SIGN UP /////////////////////////////////////
  ///////////////////////////////////// LOG IN /////////////////////////////////////

  private void openLogInView(ActionEvent event, String name) { // open login view
    System.out.println("Opening Log In View");
    System.out.println("Name: " + name);
    try {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("logIn-view.fxml")
      );
      Stage stage = new Stage();
      stage.setScene(new Scene(loader.load()));
      stage.setTitle("Log In");
      Label nameLabel = (Label) loader.getNamespace().get("nameLabel");
      nameLabel.setText(decrypt(name));
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  ///////////////////////////////////// LOG IN /////////////////////////////////////

}
