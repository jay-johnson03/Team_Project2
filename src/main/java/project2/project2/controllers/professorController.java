// package project2.project2.controllers;

// import java.io.IOException;
// import java.util.List;
// import java.util.Observable;

// import javafx.collections.FXCollections;
// import javafx.collections.ListChangeListener;
// import javafx.collections.ObservableList;
// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.control.TableColumn;
// import javafx.scene.control.TableView;
// import javafx.scene.control.TextField;
// import javafx.scene.control.cell.PropertyValueFactory;
// import javafx.scene.input.MouseEvent;
// import javafx.stage.Stage;
// import project2.classes.Course;
// import project2.classes.User;
// import project2.project2.utils.FileUtil;
// import project2.classes.Grade;

// public class professorController {

//   @FXML
//   private Label coursesLabel;
//   @FXML
//   private TableView<Course> coursesTable;
//   @FXML
//   private TableColumn<Course, String> courseIdColumn;
//   @FXML
//   private TableColumn<Course, String> courseNameColumn;
//   @FXML
//   private TableColumn<Course, String> professorNameColumn;
//   @FXML
//   private Label gradesLabel;
//   @FXML
//   private TableView<Grade> gradesTable;
//   @FXML
//   private TableColumn<Grade, String> gradesCourseIdColumn;
//   @FXML
//   private TableColumn<Grade, String> gradesCourseNameColumn;
//   @FXML
//   private TableColumn<Grade, String> assignmentNameColumn;
//   @FXML
//   private TableColumn<Grade, String> gradeColumn;
//   @FXML
//   private Label welcomeLabel;
//   @FXML
//   private TextField editGradeField;
//   @FXML
//   private Button saveGradeButton;

//   private User user;

//   private ObservableList<Course> coursesList = FXCollections.observableArrayList();

//   private ObservableList<Grade> gradesList = FXCollections.observableArrayList();

//   private Course selectedCourse;
//   private Grade selectedGrade;

//   public void setUser(User user) {
//     this.user = user;
//     initializeCoursesTable();
//   }

//   public void initializeCoursesTable() {
//     welcomeLabel.setText("Welcome, " + user.getName() + " #" + user.getId());

//     courseIdColumn.setCellValueFactory(
//         new PropertyValueFactory<Course, String>("id"));
//     courseNameColumn.setCellValueFactory(
//         new PropertyValueFactory<Course, String>("name"));
//     professorNameColumn.setCellValueFactory(
//         new PropertyValueFactory<Course, String>("professorId"));

//     for (String[] course : user.getCourses()) {
//       coursesList.add(new Course(Integer.parseInt(course[0]), course[1], Integer.parseInt(course[2])));
//     }

//     coursesTable.setItems(coursesList);

//     TableView.TableViewSelectionModel<Course> selectionModel = coursesTable.getSelectionModel();
//     ObservableList<Course> selectedItems = selectionModel.getSelectedItems();

//     selectedItems.addListener(new ListChangeListener<Course>() {
//       @Override
//       public void onChanged(Change<? extends Course> course) {
//         if (course.next()) {
//           selectedCourse = course.getList().get(0);
//           int selectedCourseId = selectedCourse.getId();
//           initializeGradesTable(selectedCourseId);
//         }
//       }
//     });
//   }

//   public void initializeGradesTable(int courseId) {

//     gradesList.clear();

//     gradesCourseIdColumn.setCellValueFactory(
//         new PropertyValueFactory<Grade, String>("id"));
//     gradesCourseNameColumn.setCellValueFactory(
//         new PropertyValueFactory<Grade, String>("userId"));
//     assignmentNameColumn.setCellValueFactory(
//         new PropertyValueFactory<Grade, String>("name"));
//     gradeColumn.setCellValueFactory(
//         new PropertyValueFactory<Grade, String>("grade"));

//     for (String[] grade : user.getGrades()) {
//       if (Integer.parseInt(grade[1]) == courseId) {
//         gradesList.add(new Grade(
//             Integer.parseInt(grade[0]),
//             Integer.parseInt(grade[1]),
//             Integer.parseInt(grade[2]),
//             grade[3],
//             Double.parseDouble(grade[4])));
//       }

//       gradesTable.setItems(gradesList);
//       TableView.TableViewSelectionModel<Grade> selectionModel = gradesTable
//           .getSelectionModel();
//       ObservableList<Grade> selectedItems = selectionModel
//           .getSelectedItems();
//       selectedItems.addListener(new ListChangeListener<Grade>() {
//         @Override
//         public void onChanged(Change<? extends Grade> grade) {
//           if (grade.next()) {
//             Grade selectedGrade = grade.getList().get(0);
//             int selectedGradeId = selectedGrade.getId();
//             System.out.println("Selected grade: " + selectedGradeId);
//           }
//         }
//       });
//     }

//     gradesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//       if (newSelection != null) {
//         selectedGrade = newSelection;
//         editGradeField.setText(String.valueOf(selectedGrade.getGrade()));
//       }
//     });
//   }

//   @FXML
//   void saveGradeChanges(ActionEvent event) {
//     //
//   }

//   @FXML
//   void addCourse(ActionEvent event) {

//   }

//   @FXML
//   void drop(ActionEvent event) {

//     try {
//       FileUtil.delete(String.valueOf(selectedCourse.getId()), FileUtil.ENROLLMENTS_TABLE);
//     } catch (IOException e) {
//       e.printStackTrace();
//     }

//     coursesList.remove(selectedCourse);
//     coursesTable.refresh();

//     gradesList.clear();
//     gradesTable.refresh();
//     selectedCourse = null;

//   }

//   @FXML
//   void addGrade(ActionEvent event) {

//   }

//   @FXML
//   void deleteGrade(ActionEvent event) {
//     try {
//       FileUtil.delete(String.valueOf(selectedGrade.getId()), FileUtil.ASSIGNMENTS_TABLE);
//     } catch (IOException e) {
//       e.printStackTrace();
//     }
//     gradesList.remove(selectedGrade);
//     gradesTable.refresh();
//     editGradeField.clear();
//   }

//   @FXML
//   private void close(ActionEvent event) {
//     // close the window, makes sure to close the window that the button is in by
//     // getting the source of the event and getting the scene and window from that
//     Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene()
//         .getWindow();
//     stage.close();
//   }
// }

// /*
//  * List<String[]> grades = FileUtil.select(
//  * 1,
//  * String.valueOf(user.getId()),
//  * FileUtil.GRADES_TABLE
//  * );
//  * 
//  * for (String[] grade : grades) { // this is how to access multiple results
//  * System.out.println("Course: " + grade[0]);
//  * }
//  * 
//  * String firstGrade = grades.get(0)[0];
//  * System.out.println("First grade: " + firstGrade); // this is how to access a
//  * single result
//  */