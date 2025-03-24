package project2.project2.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LogInController {

  @FXML
  private Label nameLabel;

  public void setName(String name) {
    System.out.println("Setting name label");
    this.nameLabel.setText(name);
  }

  @FXML
  private void close(ActionEvent event) {
    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene()
      .getWindow();
    stage.close();
  }
}
