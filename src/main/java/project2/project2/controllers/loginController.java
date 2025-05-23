package project2.project2.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project2.classes.User;
import project2.project2.utils.FileUtil;
import project2.project2.utils.HashingUtil;

public class loginController {
  // fxml elements
  @FXML
  private TextField emailField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Button close;
  //

  // sign in button clicked -> get email and password -> encrypt email -> hash
  // password -> pass to checkFile
  // if file exists -> open log in view
  // else -> prompt sign up
  @FXML
  private void signIn(ActionEvent event) {
    System.out.println("Attempting login, checking file...");
    String email = this.emailField.getText();
    String password = this.passwordField.getText();

    // checks if file exists
    // if it does -> open log in view
    // else -> prompt sign up

    // this is an example of a lambda expression which is a way to pass a function
    // as a parameter. Syntax: (parameter/input) -> function logic

    // also an example of an anonymous function which is just a function without a
    // name, you don't need a name cause the function is being passed as a parameter
    FileUtil.logInCheck(email, password, this::openHomeView, () -> promptSignUp(null));
  }
  //

  // sign up button clicked -> open sign up view
  @FXML
  private void promptSignUp(ActionEvent event) {
    // System.out.println("Prompting sign up...");
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/project2/project2/signup-view.fxml"));
      Stage stage = new Stage();
      stage.setScene(new Scene(loader.load()));
      stage.setTitle("Sign Up");
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  //

  private void openHomeView(User user) {
    try {
      FXMLLoader loader;
      Stage stage = new Stage();

      
      if (user.getIsProfessor()) {
        System.out.println("Redirecting to Professor Dashboard...");
        loader = new FXMLLoader(getClass().getResource("/project2/project2/professor-view.fxml"));
        stage.setTitle("Professor Dashboard");
      } else {
        System.out.println("Redirecting to Student Dashboard...");
        loader = new FXMLLoader(getClass().getResource("/project2/project2/student-view.fxml"));
        stage.setTitle("Student Dashboard"); 
      }
      
      stage.setScene(new Scene(loader.load()));
      
      if (user.getIsProfessor()) {
        professorController professorController = loader.getController();
        professorController.setUser(user);
      } else {
        homeController homeController = loader.getController();
        homeController.setUser(user);
      }

      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @FXML
  private void close(ActionEvent event) {
    Stage stage = (Stage) this.close.getScene().getWindow();
    stage.close();
  }
}
