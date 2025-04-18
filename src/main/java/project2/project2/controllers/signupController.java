package project2.project2.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project2.project2.utils.EncryptionUtil;
import project2.project2.utils.FileUtil;
import project2.project2.utils.HashingUtil;

public class signupController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField nameField;

    @FXML
    private CheckBox isProfessorField;

    // sign up button clicked -> get email, password, and name -> encrypt email ->
    // hash
    // password -> create account
    @FXML
    private void signUp(ActionEvent event) {
        System.out.println("Signing up");
        String email = this.emailField.getText();
        String password = this.passwordField.getText();
        String name = this.nameField.getText();
        Boolean isProfessor = this.isProfessorField.isSelected();

        FileUtil.createAccount(name, email, password, isProfessor);

        Stage stage = (Stage) this.emailField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene()
                .getWindow();
        stage.close();
    }
}
