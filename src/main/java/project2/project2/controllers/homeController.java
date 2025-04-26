package project2.project2.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import project2.classes.Course;
import project2.classes.Grade;
import project2.classes.User;
import project2.project2.utils.FileUtil;

public class homeController {

  @FXML
  private Label coursesLabel;

  @FXML
  private TableView<Course> coursesTable;

  @FXML
  private TableColumn<Course, String> courseIdColumn;

  @FXML
  private TableColumn<Course, String> courseNameColumn;

  @FXML
  private TableColumn<Course, String> professorNameColumn;

  @FXML
  private Label gradesLabel;

  @FXML
  private TableView<Grade> gradesTable;

  @FXML
  private TableColumn<Grade, String> gradesCourseIdColumn;

  @FXML
  private TableColumn<Grade, String> gradesCourseNameColumn;

  @FXML
  private TableColumn<Grade, String> assignmentNameColumn;

  @FXML
  private TableColumn<Grade, String> gradeColumn;

  @FXML
  private Label welcomeLabel;

  @FXML
  private TextField courseIdField;

  private User user;

  private ObservableList<Course> coursesList =
    FXCollections.observableArrayList();
  private ObservableList<Grade> gradesList =
    FXCollections.observableArrayList();

  private Course selectedCourse;
  private Grade selectedGrade;

  /**
   * Sets the logged-in user and initializes the courses table.
   */
  public void setUser(User user) {
    this.user = user;
    initializeCoursesTable();
  }

  /**
   * Initializes the courses table with user data.
   */
  public void initializeCoursesTable() {
    welcomeLabel.setText(
      "Welcome Student, " + user.getName() + " #" + user.getId()
    );

    // Set up table columns
    courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    professorNameColumn.setCellValueFactory(
      new PropertyValueFactory<>("professorId")
    );

    // Populate courses list
    for (String[] course : user.getCourses()) {
      coursesList.add(
        new Course(
          Integer.parseInt(course[0]),
          course[1],
          Integer.parseInt(course[2])
        )
      );
    }

    // Load courses into the table
    coursesTable.setItems(coursesList);

    // Add listener for course selection
    coursesTable
      .getSelectionModel()
      .getSelectedItems()
      .addListener(
        (ListChangeListener<Course>) change -> {
          if (change.next() && !change.getList().isEmpty()) {
            selectedCourse = change.getList().get(0);
            initializeGradesTable(selectedCourse.getId());
          }
        }
      );
  }

  /**
   * Initializes the grades table for a selected course.
   */
  public void initializeGradesTable(int courseId) {
    gradesList.clear();

    // Set up table columns
    gradesCourseIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    gradesCourseNameColumn.setCellValueFactory(
      new PropertyValueFactory<>("userId")
    );
    assignmentNameColumn.setCellValueFactory(
      new PropertyValueFactory<>("name")
    );
    gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

    // Populate grades list
    for (String[] grade : user.getGrades()) {
      if (Integer.parseInt(grade[1]) == courseId) {
        gradesList.add(
          new Grade(
            Integer.parseInt(grade[0]),
            Integer.parseInt(grade[1]),
            Integer.parseInt(grade[2]),
            grade[3],
            Double.parseDouble(grade[4])
          )
        );
      }
    }

    // Load grades into the table
    gradesTable.setItems(gradesList);

    // Add listener for grade selection
    gradesTable
      .getSelectionModel()
      .selectedItemProperty()
      .addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
          selectedGrade = newSelection;
        }
      });
  }

  /**
   * Adds a new course to the user's enrolled courses.
   */
  @FXML
  void addCourse(ActionEvent event) {
    String courseId = courseIdField.getText();
    if (courseId.isEmpty()) {
      showAlert("Course ID cannot be empty.", Alert.AlertType.WARNING);
      return;
    }

    for (Course course : coursesList) {
      if (course.getId() == Integer.parseInt(courseId)) {
        showAlert(
          "You are already enrolled in this course.",
          Alert.AlertType.WARNING
        );
        return;
      }
    }

    String[][] courseEntry = FileUtil.select(
      0,
      courseId,
      FileUtil.COURSES_TABLE
    );
    if (courseEntry.length == 0) {
      showAlert("Course not found.", Alert.AlertType.WARNING);
      return;
    }

    try {
      String[] data = { courseId, String.valueOf(user.getId()) };
      FileUtil.insert(data, FileUtil.ENROLLMENTS_TABLE);

      coursesList.add(
        new Course(
          Integer.parseInt(courseEntry[0][0]),
          courseEntry[0][1],
          Integer.parseInt(courseEntry[0][2])
        )
      );
    } catch (Exception e) {
      showAlert(
        "Error adding course: " + e.getMessage(),
        Alert.AlertType.ERROR
      );
    }
  }

  /**
   * Drops the selected course from the user's enrolled courses.
   */
  @FXML
  void drop(ActionEvent event) {
    if (selectedCourse == null) {
      showAlert("No course selected.", Alert.AlertType.WARNING);
      return;
    }

    String temp = "temp.csv";
    try (
      BufferedReader reader = new BufferedReader(
        new FileReader(FileUtil.ENROLLMENTS_TABLE)
      );
      FileWriter writer = new FileWriter(temp)
    ) {
      String header = reader.readLine();
      if (header != null) {
        writer.write(header + "\n");
      }

      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        if (
          data[1].equals(String.valueOf(selectedCourse.getId())) &&
          data[2].equals(String.valueOf(user.getId()))
        ) {
          continue;
        }
        writer.write(line + "\n");
      }
    } catch (IOException e) {
      showAlert(
        "Error reading or writing the file: " + e.getMessage(),
        Alert.AlertType.ERROR
      );
      return;
    }

    try {
      Files.delete(Paths.get(FileUtil.ENROLLMENTS_TABLE));
      Files.move(Paths.get(temp), Paths.get(FileUtil.ENROLLMENTS_TABLE));

      coursesList.remove(selectedCourse);
      coursesTable.refresh();
      gradesList.clear();
      gradesTable.refresh();
    } catch (IOException e) {
      showAlert(
        "Error replacing the enrollments file: " + e.getMessage(),
        Alert.AlertType.ERROR
      );
    }
  }

  /**
   * Closes the application window.
   */
  @FXML
  private void close(ActionEvent event) {
    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene()
      .getWindow();
    stage.close();
  }

  /**
   * Displays an alert dialog with the specified message and type.
   * @param message The message to display.
   * @param alertType The type of alert.
   */
  private void showAlert(String message, Alert.AlertType alertType) {
    Alert alert = new Alert(alertType);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
