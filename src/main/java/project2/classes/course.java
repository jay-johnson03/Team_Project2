package project2.classes;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class course {

    private int id;
    private String name;
    private String professorId;
    private user selectedUser;

    @FXML
    private TableView<course> courseTableView;
    @FXML
    private TableColumn<course, Integer> courseIdColumn;
    @FXML
    private TableColumn<course, String> courseNameColumn;
    @FXML
    private TableColumn<course, String> courseProfessorIdColumn;
    @FXML
    private Button btnAddClass;
    @FXML
    private TableColumn<course, Void> actionColumn; // Column for the button
    

    public void initialize() {
        // Initialize the course table view and columns here

        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("course id"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("course name"));
        courseProfessorIdColumn.setCellValueFactory(new PropertyValueFactory<>("course professor id"));

        courseTableView.setEditable(true);

    addButtonToTable();
        }

        private void addButtonToTable() {
            Callback<TableColumn<course, Void>, TableCell<course, Void>> cellFactory = new Callback<>() {
                @Override
                public TableCell<course, Void> call(final TableColumn<course, Void> param) {
                    return new TableCell<>() {
                        private final Button btn = new Button("Action");
    
                        {
                            btn.setOnAction(event -> {
                                course selectedCourse = getTableView().getItems().get(getIndex());
                                System.out.println("Button clicked for course: " + selectedCourse.getName());
                                // Add your button action logic here
                            });
                        }
    
                        @Override
                        protected void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    public course(int id, String name, String professorId) {
        this.id = id;
        this.name = name;
        this.professorId = professorId;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getProfessorId() {
        return professorId;
    }

    // making a table in the database to hold the courses but also list off grades for each assignment
    // and the final grade for the course
    // toggle columns where it shows the students in the course and their grades for only professors
    // toggle columns where it shows the courses the student is enrolled in and their grades for only students
     



    /*
     * the goal of this project is to create a course management system that allows
     * students to enroll in courses, professors to manage their courses, and for grades to be posted
     * students can enroll and unenroll in courses (same with professors)
     * professors can add and remove students from their courses
     * students can view their grades and professors can post grades
     * students can view their courses and professors can view their students 
     * 
     * how to implement this:
     * make a table that shows only for professors all the students in the class information
     * make a table that shows only for students all the courses they are enrolled in
     * make a table of the available courses for students to enroll in
     * a button for students to unenroll in a course
     * a button for professors to add and remove students from their courses in the table
     */
}

