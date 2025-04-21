package project2.project2.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import project2.classes.User;

public class homeController {

  @FXML
  private Label nameLabel;
  @FXML
  private Label coursesLabel;
  @FXML
  private TableView<?> coursesTable;
  @FXML
  private TableColumn<?, ?> courseIdColumn;
  @FXML
  private TableColumn<?, ?> courseNameColumn;
  @FXML
  private TableColumn<?, ?> professorNameColumn;
  @FXML
  private Label gradesLabel;
  @FXML
  private TableView<?> gradesTable;
  @FXML
  private TableColumn<?, ?> gradesCourseIdColumn;
  @FXML
  private TableColumn<?, ?> gradesCourseNameColumn;
  @FXML
  private TableColumn<?, ?> assignmentNameColumn;
  @FXML
  private TableColumn<?, ?> gradeColumn;

  private User user;

  public void setUser(User user) {
    this.user = user;

    populate();
  }

  public void populate() {

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
