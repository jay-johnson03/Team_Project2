package project2.classes;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;

public class course {
    private int id;
    private String name;
    private String professorId;

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
    public int getProfessorId() {
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

