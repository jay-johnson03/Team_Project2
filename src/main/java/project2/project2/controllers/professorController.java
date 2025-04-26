package project2.project2.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.collections.FXCollections;
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

public class professorController {

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
  private TextField studentNameField;

  @FXML
  private TextField studentEmailField;

  @FXML
  private Button saveGradeButton;

  @FXML
  private TableView<User> studentsTable;

  @FXML
  private TableColumn<User, String> studentIdColumn;

  @FXML
  private TableColumn<User, String> studentNameColumn;

  @FXML
  private TableColumn<User, String> studentEmailColumn;

  @FXML
  private TextField courseNameField;

  @FXML
  private TextField editGradeField;

  @FXML
  private TextField assignmentNameField;

  @FXML
  private Button addGradeButton;

  private ObservableList<Course> coursesList =
    FXCollections.observableArrayList();
  private ObservableList<User> studentsList =
    FXCollections.observableArrayList();
  private ObservableList<Grade> gradesList =
    FXCollections.observableArrayList();

  private User user;
  private Course selectedCourse;
  private User selectedStudent;
  private Grade selectedGrade;

  /**
   * Sets the logged-in user and initializes the courses table.
   */
  public void setUser(User user) {
    this.user = user;
    initializeCoursesTable();
  }

  /**
   * Initializes the courses table with data for the logged-in professor.
   */
  public void initializeCoursesTable() {
    welcomeLabel.setText(
      "Welcome Professor, " + user.getName() + " #" + user.getId()
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
      .selectedItemProperty()
      .addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
          selectedCourse = newSelection;
          initializeStudentsTable(selectedCourse.getId());
        }
      });
  }

  /**
   * Initializes the students table for the selected course.
   */
  private void initializeStudentsTable(int courseId) {
    studentsList.clear();

    // Set up table columns
    studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    studentEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

    // Load students enrolled in the selected course
    String[][] enrolledStudents = FileUtil.select(
      1,
      String.valueOf(courseId),
      FileUtil.ENROLLMENTS_TABLE
    );
    for (String[] studentData : enrolledStudents) {
      int studentId = Integer.parseInt(studentData[2]);
      String[][] studentInfo = FileUtil.select(
        0,
        String.valueOf(studentId),
        FileUtil.USERS_TABLE
      );
      if (studentInfo.length > 0) {
        studentsList.add(new User(Integer.parseInt(studentInfo[0][0])));
      }
    }

    // Load students into the table
    studentsTable.setItems(studentsList);

    // Add listener for student selection
    studentsTable
      .getSelectionModel()
      .selectedItemProperty()
      .addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
          selectedStudent = newSelection;
          initializeGradesTable(
            selectedCourse.getId(),
            selectedStudent.getId()
          );
        }
      });
  }

  /**
   * Initializes the grades table for the selected course and student.
   */
  public void initializeGradesTable(int courseId, int studentId) {
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

    // Load grades for the selected course and student
    String[][] gradesData = FileUtil.select(
      1,
      String.valueOf(courseId),
      FileUtil.ASSIGNMENTS_TABLE
    );
    for (String[] grade : gradesData) {
      if (Integer.parseInt(grade[2]) == studentId) {
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
          editGradeField.setText(String.valueOf(selectedGrade.getGrade()));
        }
      });
  }

  /**
   * Saves changes made to the selected grade.
   */
  @FXML
  void saveGradeChanges(ActionEvent event) {
    if (selectedGrade == null) {
      showAlert("No grade selected.", Alert.AlertType.WARNING);
      return;
    }

    String newGradeValue = editGradeField.getText();
    if (newGradeValue.isEmpty()) {
      showAlert("Grade value cannot be empty.", Alert.AlertType.WARNING);
      return;
    }

    try {
      FileUtil.update(
        String.valueOf(selectedGrade.getId()),
        4,
        newGradeValue,
        FileUtil.ASSIGNMENTS_TABLE
      );
      selectedGrade.setGrade(Double.parseDouble(newGradeValue));
      gradesTable.refresh();
      editGradeField.clear();
      // showAlert("Grade updated successfully.", Alert.AlertType.INFORMATION);
    } catch (IOException e) {
      showAlert(
        "Error updating grade: " + e.getMessage(),
        Alert.AlertType.ERROR
      );
    }
  }

  /**
   * Adds a new course to the professor's courses.
   */
  @FXML
  void addCourse(ActionEvent event) {
    String courseName = courseNameField.getText();
    String professorId = String.valueOf(user.getId());

    // Ensure all required fields are provided
    if (courseName.isEmpty() || professorId.isEmpty()) {
      showAlert(
        "Course name or professor ID cannot be empty.",
        Alert.AlertType.WARNING
      );
      return;
    }

    try {
      // Get the next available ID for the course
      int nextCourseId = FileUtil.getNextId(FileUtil.COURSES_TABLE);

      // Create the new course entry
      String[] newCourse = {
        String.valueOf(nextCourseId),
        courseName,
        professorId,
      };

      // Insert the new course into the table
      FileUtil.insert(newCourse, FileUtil.COURSES_TABLE);

      // Add the new course to the ObservableList
      coursesList.add(
        new Course(nextCourseId, courseName, Integer.parseInt(professorId))
      );

      // Refresh the table and clear the input field
      coursesTable.refresh();
      courseNameField.clear();
      // showAlert("Course added successfully.", Alert.AlertType.INFORMATION);
    } catch (Exception e) {
      showAlert(
        "Error adding course: " + e.getMessage(),
        Alert.AlertType.ERROR
      );
    }
  }

  /**
   * Retrieves the next available ID for a given table.
   */
  public static int getNextId(String table) {
    int id = 0;
    try (BufferedReader reader = new BufferedReader(new FileReader(table))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] row = line.split(",");
        try {
          id = Math.max(id, Integer.parseInt(row[0]));
        } catch (NumberFormatException ignored) {}
      }
    } catch (IOException e) {
      System.err.println("Error retrieving next ID from table: " + table);
      e.printStackTrace();
    }
    return id + 1;
  }

  /**
   * Drops the selected course from the professor's courses.
   */
  @FXML
  void drop(ActionEvent event) {
    if (selectedCourse == null) {
      System.out.println("No course selected.");
      return;
    }

    String temp = "temp.csv";
    try (
      BufferedReader reader = new BufferedReader(
        new FileReader(FileUtil.ENROLLMENTS_TABLE)
      );
      FileWriter writer = new FileWriter(temp)
    ) {
      String header = reader.readLine(); // Read the header
      if (header != null) {
        writer.write(header + "\n"); // Write the header to the temp file
      }

      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        // Skip rows where the Course_Id matches the selected course
        if (data[1].equals(String.valueOf(selectedCourse.getId()))) {
          continue;
        }
        writer.write(line + "\n");
      }
    } catch (IOException e) {
      System.out.println("Error reading or writing the enrollments file.");
      e.printStackTrace();
      return;
    }

    try {
      Files.delete(Paths.get(FileUtil.ENROLLMENTS_TABLE));
      Files.move(Paths.get(temp), Paths.get(FileUtil.ENROLLMENTS_TABLE));
    } catch (IOException e) {
      System.out.println("Error replacing the ENROLLMENTS_TABLE file.");
      e.printStackTrace();
      return;
    }

    // Handle assignments table similarly
    try (
      BufferedReader reader = new BufferedReader(
        new FileReader(FileUtil.ASSIGNMENTS_TABLE)
      );
      FileWriter writer = new FileWriter(temp)
    ) {
      String header = reader.readLine(); // Read the header
      if (header != null) {
        writer.write(header + "\n"); // Write the header to the temp file
      }

      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        // Skip rows where the Course_Id matches the selected course
        if (data[1].equals(String.valueOf(selectedCourse.getId()))) {
          continue;
        }
        writer.write(line + "\n");
      }
    } catch (IOException e) {
      System.out.println("Error reading or writing the assignments file.");
      e.printStackTrace();
      return;
    }

    try {
      Files.delete(Paths.get(FileUtil.ASSIGNMENTS_TABLE));
      Files.move(Paths.get(temp), Paths.get(FileUtil.ASSIGNMENTS_TABLE));
    } catch (IOException e) {
      System.out.println("Error replacing the ASSIGNMENTS_TABLE file.");
      e.printStackTrace();
      return;
    }

    // Update the ObservableLists and UI
    coursesList.remove(selectedCourse);
    coursesTable.refresh();

    studentsList.clear();
    studentsTable.refresh();

    gradesList.clear();
    gradesTable.refresh();

    selectedCourse = null;
    System.out.println("Course dropped successfully.");
  }

  /**
   * Updates the selected student's information.
   */
  @FXML
  public void updateStudent(ActionEvent event) {
    if (selectedStudent == null) {
      System.out.println("No student selected.");
      return;
    }

    String newName = studentNameField.getText();
    String newEmail = studentEmailField.getText();

    try {
      FileUtil.update(
        String.valueOf(selectedStudent.getId()),
        1,
        newName,
        FileUtil.USERS_TABLE
      );
      FileUtil.update(
        String.valueOf(selectedStudent.getId()),
        2,
        newEmail,
        FileUtil.USERS_TABLE
      );

      selectedStudent.setName(newName);
      selectedStudent.setEmail(newEmail);
      studentsTable.refresh();

      System.out.println("Student updated successfully.");
    } catch (IOException e) {
      System.out.println("Error updating student.");
      e.printStackTrace();
    }
  }

  /**
   * Adds a new grade for the selected student and course.
   */
  @FXML
  void addGrade(ActionEvent event) {
    if (selectedStudent == null || selectedCourse == null) {
      showAlert("No student or course selected.", Alert.AlertType.WARNING);
      return;
    }

    String assignmentName = assignmentNameField.getText();
    if (assignmentName.isEmpty()) {
      showAlert("Assignment name cannot be empty.", Alert.AlertType.WARNING);
      return;
    }

    String[] newGrade = {
      String.valueOf(selectedCourse.getId()),
      String.valueOf(selectedStudent.getId()),
      assignmentName,
      "0",
    };

    try {
      FileUtil.insert(newGrade, FileUtil.ASSIGNMENTS_TABLE);
      initializeGradesTable(selectedCourse.getId(), selectedStudent.getId());
      assignmentNameField.clear();
      // showAlert("Grade added successfully.", Alert.AlertType.INFORMATION);
    } catch (Exception e) {
      showAlert("Error adding grade: " + e.getMessage(), Alert.AlertType.ERROR);
    }
  }

  /**
   * Deletes the selected grade.
   */
  @FXML
  void deleteGrade(ActionEvent event) {
    if (selectedGrade == null) {
      showAlert("No grade selected.", Alert.AlertType.WARNING);
      return;
    }

    try {
      FileUtil.delete(
        String.valueOf(selectedGrade.getId()),
        FileUtil.ASSIGNMENTS_TABLE
      );
      gradesList.remove(selectedGrade);
      gradesTable.refresh();
      editGradeField.clear();
      // showAlert("Grade deleted successfully.", Alert.AlertType.INFORMATION);
    } catch (IOException e) {
      showAlert(
        "Error deleting grade: " + e.getMessage(),
        Alert.AlertType.ERROR
      );
    }
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

  /**
   * Closes the application window.
   */
  @FXML
  private void close(ActionEvent event) {
    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene()
      .getWindow();
    stage.close();
  }
}
