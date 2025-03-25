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
    // close the window, makes sure to close the window that the button is in by
    // getting the source of the event and getting the scene and window from that
    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene()
      .getWindow();
    stage.close();
  }
}
