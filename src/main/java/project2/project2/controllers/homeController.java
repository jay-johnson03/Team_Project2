package project2.project2.controllers;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import project2.classes.User;
import project2.project2.utils.FileUtil;

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

    nameLabel.setText("Welcome, " + user.getName() + "!");

    List<String[]> grades = FileUtil.select(
      1,
      String.valueOf(user.getId()),
      FileUtil.GRADES_TABLE
    );

    for (String[] grade : grades) { // this is how to access multiple results
      System.out.println("Course: " + grade[0]);
    }

    String firstGrade = grades.get(0)[0];
    System.out.println("First grade: " + firstGrade); // this is how to access a single result
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
