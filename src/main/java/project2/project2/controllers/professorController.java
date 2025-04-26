package project2.project2.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

  private User user;

  @FXML
  private TextField courseNameField;

  @FXML
  private TextField editGradeField;

  @FXML
  private TextField assignmentNameField;

  private ObservableList<Course> coursesList =
    FXCollections.observableArrayList();
  private ObservableList<User> studentsList =
    FXCollections.observableArrayList();
  private ObservableList<Grade> gradesList =
    FXCollections.observableArrayList();

  private Course selectedCourse;
  private User selectedStudent;
  private Grade selectedGrade;

  public void setUser(User user) {
    this.user = user;
    initializeCoursesTable();
  }

  public void initializeCoursesTable() {
    welcomeLabel.setText(
      "Welcome Professor, " + user.getName() + " #" + user.getId()
    );

    courseIdColumn.setCellValueFactory(
      new PropertyValueFactory<Course, String>("id")
    );
    courseNameColumn.setCellValueFactory(
      new PropertyValueFactory<Course, String>("name")
    );
    professorNameColumn.setCellValueFactory(
      new PropertyValueFactory<Course, String>("professorId")
    );

    for (String[] course : user.getCourses()) {
      coursesList.add(
        new Course(
          Integer.parseInt(course[0]),
          course[1],
          Integer.parseInt(course[2])
        )
      );
    }

    coursesTable.setItems(coursesList);

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

  private void initializeStudentsTable(int courseId) {
    studentsList.clear();

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

    studentsTable.setItems(studentsList);

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

  public void initializeGradesTable(int courseId, int studentId) {
    gradesList.clear();

    gradesCourseIdColumn.setCellValueFactory(
      new PropertyValueFactory<Grade, String>("id")
    );
    gradesCourseNameColumn.setCellValueFactory(
      new PropertyValueFactory<Grade, String>("userId")
    );
    assignmentNameColumn.setCellValueFactory(
      new PropertyValueFactory<Grade, String>("name")
    );
    gradeColumn.setCellValueFactory(
      new PropertyValueFactory<Grade, String>("grade")
    );

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

    gradesTable.setItems(gradesList);

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

  @FXML
  void saveGradeChanges(ActionEvent event) {
    if (selectedGrade == null) {
      System.out.println("No grade selected.");
      return;
    }

    String newGradeValue = editGradeField.getText();
    if (newGradeValue.isEmpty()) {
      System.out.println("Grade value cannot be empty.");
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
      System.out.println("Grade updated successfully.");
    } catch (IOException e) {
      System.out.println("Error updating grade.");
      e.printStackTrace();
    }
  }

  @FXML
  void addCourse(ActionEvent event) {
    String courseName = courseNameField.getText();
    String professorId = String.valueOf(user.getId());

    // Ensure all required fields are provided
    if (courseName.isEmpty() || professorId.isEmpty()) {
      System.out.println("Course name or professor ID cannot be empty.");
      return;
    }

    String[] newCourse = { courseName, professorId };

    FileUtil.insert(newCourse, FileUtil.COURSES_TABLE);

    coursesList.add(new Course(0, courseName, Integer.parseInt(professorId)));
    coursesTable.refresh();
    courseNameField.clear();
  }

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

  @FXML
  void addGrade(ActionEvent event) {
    if (selectedStudent == null || selectedCourse == null) {
      System.out.println("No student or course selected.");
      return;
    }
    String assignmentName = assignmentNameField.getText(); // Replace with input from UI
    String[] newGrade = {
      String.valueOf(selectedCourse.getId()),
      String.valueOf(selectedStudent.getId()),
      assignmentName,
      "0",
    };

    FileUtil.insert(newGrade, FileUtil.ASSIGNMENTS_TABLE);

    initializeGradesTable(selectedCourse.getId(), selectedStudent.getId());
    System.out.println("Grade added successfully.");
  }

  @FXML
  void deleteGrade(ActionEvent event) {
    if (selectedGrade == null) {
      System.out.println("No grade selected.");
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
      System.out.println("Grade deleted successfully.");
    } catch (IOException e) {
      System.out.println("Error deleting grade.");
      e.printStackTrace();
    }
  }

  @FXML
  private void close(ActionEvent event) {
    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene()
      .getWindow();
    stage.close();
  }

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
}
