package project2.classes;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class course {

    private int id;
    private String name;
    private String professorId;

    // allllll the fxml variables for the course management system
    @FXML
    private TableView<course> yourCoursesView;
    @FXML
    private TableColumn<course, Integer> courseIdColumn;
    @FXML
    private TableColumn<course, String> courseNameColumn;
    @FXML
    private TableColumn<course, String> courseProfessorIdColumn;
    @FXML
    private Button btnAddClass;
    @FXML
    private Button btnDropClass;
    @FXML
    private TableView<course> availableCoursesView; // Available courses table view
    @FXML 
    private TableColumn<course, Integer> availableCourseIdColumn; // Column for course ID
    @FXML
    private TableColumn<course, String> availableCourseNameColumn; // Column for course name
    @FXML
    private TableColumn<course, String> availableCourseProfessorIdColumn; // Column for professor ID
    @FXML
    private TableColumn<course, Void> actionColumn; // Column for the button
    

    private ObservableList<course> availableCourses = FXCollections.observableArrayList();
    private ObservableList<course> yourCourses = FXCollections.observableArrayList();

    public void initialize() {
        // Initialize the avaible courses table view

        availableCourseIdColumn.setCellValueFactory(new PropertyValueFactory<>("course id"));
        availableCourseNameColumn.setCellValueFactory(new PropertyValueFactory<>("course name"));
        availableCourseProfessorIdColumn.setCellValueFactory(new PropertyValueFactory<>("course professor id"));
        availableCoursesView.setEditable(true);

        // Initialize the your courses table view
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("course id"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("course name"));
        courseProfessorIdColumn.setCellValueFactory(new PropertyValueFactory<>("course professor id"));
        yourCoursesView.setEditable(true);


        // random sample data to test the table view
        availableCourses.addAll(null, 
            new course(101, "CS101", "Prof. Smith"),
            new course(202, "CS102", "Prof. Johnson"),
            new course(303, "CS103", "Prof. Brown"));

        btnAddClass.setOnAction(event -> addSelectedCourses());

        }

    private void addSelectedCourses() {
        // Get the course you selected from the available courses table view
        course selectedCourse = availableCoursesView.getSelectionModel().getSelectedItem();
        if (selectedCourse != null && !yourCourses.contains(selectedCourse)) {
            // Add the selected course to your courses list
            yourCourses.add(selectedCourse);
            // Remove the selected course from the available courses list
            availableCourses.remove(selectedCourse);
        } else {
            System.out.println("Course already added or not selected.");
        }
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

