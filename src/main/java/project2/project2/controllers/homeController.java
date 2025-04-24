package project2.project2.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
  private TextField editGradeField;

  @FXML
  private Button saveGradeButton;

  @FXML
  private TextField assignmentNameField;

  @FXML
  private TextField courseIdField;

  @FXML
  private Button addGradeButton;

  private User user;

  // ObservableList to store and display enrolled courses/grades
  private ObservableList<Course> coursesList =
    FXCollections.observableArrayList();

  private ObservableList<Grade> gradesList =
    FXCollections.observableArrayList();

  private Course selectedCourse;
  private Grade selectedGrade;

  // sets logged in user and begins initialization
  public void setUser(User user) {
    this.user = user;
    initializeCoursesTable();
  }

  // initializes the courses table with user data
  public void initializeCoursesTable() {
    welcomeLabel.setText("Welcome, " + user.getName() + " #" + user.getId());

    // set columns
    courseIdColumn.setCellValueFactory(
      new PropertyValueFactory<Course, String>("id")
    );
    courseNameColumn.setCellValueFactory(
      new PropertyValueFactory<Course, String>("name")
    );
    professorNameColumn.setCellValueFactory(
      new PropertyValueFactory<Course, String>("professorId")
    );

    // add load data in classes
    for (String[] course : user.getCourses()) {
      coursesList.add(
        new Course(
          Integer.parseInt(course[0]),
          course[1],
          Integer.parseInt(course[2])
        )
      );
    }
    // load classes into table
    coursesTable.setItems(coursesList);

    //
    TableView.TableViewSelectionModel<Course> selectionModel =
      coursesTable.getSelectionModel();
    ObservableList<Course> selectedItems = selectionModel.getSelectedItems();

    selectedItems.addListener(
      new ListChangeListener<Course>() {
        @Override
        public void onChanged(Change<? extends Course> course) {
          if (course.next()) {
            selectedCourse = course.getList().get(0);
            int selectedCourseId = selectedCourse.getId();
            initializeGradesTable(selectedCourseId);
          }
        }
      }
    );
  }

  // initializes the grades table for a selected course
  public void initializeGradesTable(int courseId) {
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

      gradesTable.setItems(gradesList);
      TableView.TableViewSelectionModel<Grade> selectionModel =
        gradesTable.getSelectionModel();
      ObservableList<Grade> selectedItems = selectionModel.getSelectedItems();
      selectedItems.addListener(
        new ListChangeListener<Grade>() {
          @Override
          public void onChanged(Change<? extends Grade> grade) {
            if (grade.next()) {
              Grade selectedGrade = grade.getList().get(0);
              int selectedGradeId = selectedGrade.getId();
              System.out.println("Selected grade: " + selectedGradeId);
            }
          }
        }
      );
    }

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

  // saves changes made to a selected grade
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
    } catch (IOException e) {
      System.out.println("Error updating grade.");
      e.printStackTrace();
    }
  }

  // adds a new course to the user's enrolled courses
  @FXML
  void addCourse(ActionEvent event) {
    String courseId = courseIdField.getText();
    if (courseId.isEmpty()) {
      System.out.println("Course ID cannot be empty.");
      return;
    }

    for (Course course : coursesList) {
      if (course.getId() == Integer.parseInt(courseId)) {
        System.out.println("You are already enrolled in this course.");
        return;
      }
    }

    String[][] courseEntry = FileUtil.select(
      0,
      courseId,
      FileUtil.COURSES_TABLE
    );
    if (courseEntry.length == 0) {
      System.out.println("Course not found.");
      return;
    }

    try {
      String[] data = { courseId, String.valueOf(user.getId()) };
      FileUtil.insert(data, FileUtil.ENROLLMENTS_TABLE);

      coursesList.clear();
      initializeCoursesTable();

      System.out.println("Course added successfully.");
    } catch (Exception e) {
      System.out.println("Error adding course.");
      e.printStackTrace();
    }
  }

  // drops a selected course from the user's enrolled courses
  @FXML
  void drop(ActionEvent event) {
    if (selectedCourse == null) {
      System.out.println("No course selected.");
      return;
    }

    try {
      FileUtil.delete(
        String.valueOf(selectedCourse.getId()),
        FileUtil.ENROLLMENTS_TABLE
      );

      coursesList.remove(selectedCourse);
      coursesTable.refresh();

      gradesList.clear();
      gradesTable.refresh();

      System.out.println("Course dropped successfully.");
    } catch (IOException e) {
      System.out.println("Error dropping course.");
      e.printStackTrace();
    }
  }

  // adds a new grade for the selected course
  @FXML
  void addGrade(ActionEvent event) {
    if (selectedCourse == null) {
      System.out.println("No course selected.");
      return;
    }

    String assignmentName = assignmentNameField.getText();
    if (assignmentName.isEmpty()) {
      System.out.println("Assignment name cannot be empty.");
      return;
    }

    String[] newGrade = {
      String.valueOf(selectedCourse.getId()),
      String.valueOf(user.getId()),
      assignmentName,
      "0",
    };

    FileUtil.insert(newGrade, FileUtil.ASSIGNMENTS_TABLE);

    initializeGradesTable(selectedCourse.getId());

    assignmentNameField.clear();
  }

  // deletes a selected grade from the grades table
  @FXML
  void deleteGrade(ActionEvent event) {
    try {
      FileUtil.delete(
        String.valueOf(selectedGrade.getId()),
        FileUtil.ASSIGNMENTS_TABLE
      );
    } catch (IOException e) {
      e.printStackTrace();
    }
    gradesList.remove(selectedGrade);
    gradesTable.refresh();
    editGradeField.clear();
  }

  // closes the application window
  @FXML
  private void close(ActionEvent event) {
    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene()
      .getWindow();
    stage.close();
  }
  /*
   * Example usage of FileUtil for fetching grades:
   * List<String[]> grades = FileUtil.select(
   *   1,
   *   String.valueOf(user.getId()),
   *   FileUtil.GRADES_TABLE
   * );
   *
   * for (String[] grade : grades) { // this is how to access multiple results
   *   System.out.println("Course: " + grade[0]);
   * }
   *
   * String firstGrade = grades.get(0)[0];
   * System.out.println("First grade: " + firstGrade); // this is how to access a single result
   */
}
