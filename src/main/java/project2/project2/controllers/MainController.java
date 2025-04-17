package project2.project2.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project2.classes.course;

public class MainController {


    @FXML
    private TableView<course> yourCoursesView;
    @FXML
    private TableColumn<course, Integer> courseIdColumn;
    @FXML
    private TableColumn<course, String> courseNameColumn;
    @FXML
    private TableColumn<course, String> courseProfessorIdColumn;
    /*
     * here's what i need to do: 
     * 
     * 
     * 1. Create a course class that has the following attributes: id, name, professorId
     * 2. Create a course management system that allows the user to add and drop courses
     * 3. Create a course management system that allows the user to view their courses and available courses
     * 4. Create a course management system that allows the user to view their grades and professors to post grades
     * 
     * how to implement this:
     * make a table showing the students current courses 
     * have a pop up that shows the available courses to enroll in when you click the available courses button 
     * in the pop up next to the course name will be a course id
     * 
     * how to make a pop up in java:
     * the pop up will just be something that shows all of the available courses in a table view and their course id
     * the pop up will have a button that says "enroll in course" and when you click it, it will add the course to your courses table view
     * the pop up will also have a button that says "unenroll in course" and when you click it, it will remove the course from your courses table view
     */

    // adding all of the import statements for the classes that i will be using in this class

    public void showAvailableCoursesPopup() {
        // this is creating a new stage 
        Stage popupStage = new Stage();
        popupStage.setTitle("Available Courses");

        // this makes the popup modal, meaning you have to close it before you can go back to the main window
        popupStage.initModality(Modality.APPLICATION_MODAL); 

        //making a table view for the available courses
        TableView<course> availableCoursesTable = new TableView<>();

        // DEFINING THE CLOMUNS!! (ik its spelled wrong...)
        TableColumn<course, Integer> courseIdColumn = new TableColumn<>("Course ID");
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<course, String> courseNameColumn = new TableColumn<>("Course Name");
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<course, String> courseProfessorIdColumn = new TableColumn<>("Professor ID");
        courseProfessorIdColumn.setCellValueFactory(new PropertyValueFactory<>("professorId"));

        // adding the columns to the table view
        availableCoursesTable.getColumns().addAll(courseIdColumn, courseNameColumn, courseProfessorIdColumn);

        // adding the data to the table view
        /* availableCoursesTable.setItems(availableCourses());

        //creating an enroll button to go on the window 
        Button enrollButton = new Button("Enroll in Course");
        enrollButton.setOnAction(event -> {
            course selectedCourse = availableCoursesTable.getSelectionModel().getSelectedItem();
            if (selectedCourse != null && !yourCourses.contains(selectedCourse)) {
                yourCourses.add(selectedCourse);
                availableCoursesTable.getItems().remove(selectedCourse);
                System.out.println("Enrolled in course: " + selectedCourse.getName());
            } else {
                System.out.println("Course already added or not selected.");
            }
        }); 
        
        // this is the layout and components of the popup window
        VBox layout = new VBox(10, availableCoursesTable, enrollButton);
        layout.setStyle("-fx-padding: 10; -fx-alignment: center; -fx-background-color:rgb(232, 213, 180);"); 

        
        //setting the scene and showing the stage
        Scene scene = new Scene(layout, 400, 300);
        popupStage.setScene(scene);
        // this will make the popup modal, meaning you have to close it before you can go back to the main window
        popupStage.showAndWait(); 
        */

    } 


}
