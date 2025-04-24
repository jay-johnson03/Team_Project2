// package project2.project2.controllers;


import javafx.collections.FXCollections;
// import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project2.classes.Course;
import project2.classes.User;
import project2.project2.utils.FileUtil;
import project2.classes.Grade;

// public class professorController {

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
  private Button searchAvailable;
  @FXML
  TableView<Course> courseOverviewTable;
  @FXML
  

//   private User user;

//   private ObservableList<Course> coursesList = FXCollections.observableArrayList();

  // private ObservableList<Grade> gradesList = FXCollections.observableArrayList();

  public void setUser(User user) {
    this.user = user;
    welcomeLabel.setText("Welcome, " + user.getName() + " #" + user.getId());
    initializeCoursesTable();
  }

  public void initializeCoursesTable() {
    welcomeLabel.setText("Welcome, Professor" + user.getName() + " #" + user.getId());

//     courseIdColumn.setCellValueFactory(
//         new PropertyValueFactory<Course, String>("id"));
//     courseNameColumn.setCellValueFactory(
//         new PropertyValueFactory<Course, String>("name"));
//     professorNameColumn.setCellValueFactory(
//         new PropertyValueFactory<Course, String>("professorId"));

    for (String[] course : User.getCourses()) {
      coursesList.add(new Course(Integer.parseInt(course[0]), course[1], Integer.parseInt(course[2])));
    }

//     coursesTable.setItems(coursesList);

//     TableView.TableViewSelectionModel<Course> selectionModel = coursesTable.getSelectionModel();
//     ObservableList<Course> selectedItems = selectionModel.getSelectedItems();


    // comment out later to use for the grades 
   /* selectedItems.addListener(new ListChangeListener<Course>() {
      @Override
      public void onChanged(Change<? extends Course> course) {
        if (course.next()) {
          selectedCourse = course.getList().get(0);
          int selectedCourseId = selectedCourse.getId();
          initializeGradesTable(selectedCourseId);
        }
      }
    }); */
  }
  public void searchAvailableCourses(ActionEvent event) {
    Stage popupStage = new Stage();
    popupStage.setTitle("Available Courses");

    // make it *modal*
    popupStage.initModality(Modality.APPLICATION_MODAL);


    // adding all of the necessary table things 
    TableView<Course> availableCoursesTable = new TableView<>();
    TableColumn<Course, String> availableCourseIdColumn = new TableColumn<>("Course ID");
    courseIdColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("id"));

    TableColumn<Course, String> availableCourseNameColumn = new TableColumn<>("Course Name");
    courseNameColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("name"));

    TableColumn<Course, String> availableProfessorNameColumn = new TableColumn<>("Professor Name");
    professorNameColumn.setCellValueFactory(new PropertyValueFactory<Course, String>("professorName"));

    availableCoursesTable.getColumns().add(availableCourseIdColumn);
    availableCoursesTable.getColumns().add(availableCourseNameColumn);
    availableCoursesTable.getColumns().add(availableProfessorNameColumn);

    //putting the data into the table
    ObservableList<Course> availableCoursesList = FXCollections.observableArrayList();
    for (String[] courseData : FileUtil.getCourses()) {
      availableCoursesList.add(new Course(
        Integer.parseInt(courseData[0]),
        courseData[1],
        Integer.parseInt(courseData[2])));
    }
    availableCoursesTable.setItems(availableCoursesList);

    // my dandy enroll button 
    Button enrollButton = new Button("Enroll");
    enrollButton.setOnAction(e -> {
      Course selectedCourse = availableCoursesTable.getSelectionModel().getSelectedItem();
      if (selectedCourse != null) {
        // enroll the user in the selected course
        coursesList.add(selectedCourse);
        coursesTable.refresh();
        // removing the course from the available courses table
        availableCoursesList.remove(selectedCourse);

        System.out.println("Enrolled in course: " + selectedCourse.getName());
      } else {
        System.out.println("No course selected.");
      }
    });

    //creatung the vbox layout and adding the table and button to it
    VBox vbox = new VBox(10,availableCoursesTable, enrollButton);
    vbox.setStyle("-fx-padding: 10; -fx-background-color:rgb(244, 221, 180);");
  }
}

/*
 * TO DO 
 * make a second table that shows what courses the student is enrolled in after clicking the student 
 * make the course info button show the students grades and assignments for that course
 * link the search course button to the available courses table for that specific student
 * 
 * 
 * 
 */