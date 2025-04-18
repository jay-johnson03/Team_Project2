package project2.project2.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import project2.classes.User;

public class homeController {

  @FXML
  private Label nameLabel;

  private User user;

  public void setUser(User user) {
    this.user = user;

    nameLabel.setText("Welcome, " + user.getName() + "!");
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
