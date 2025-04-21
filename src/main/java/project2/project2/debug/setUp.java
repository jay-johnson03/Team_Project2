package project2.project2.debug;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import project2.project2.utils.FileUtil;



public class setUp {
    public static void main(String[] args) {
        setup();
    }

    public static void setUpDatabase() {
    // create a database for the users

    // create the tables
    try {
        FileWriter writer = new FileWriter(FileUtil.USERS_TABLE);
        writer.write("id, name, email, password, isProfessor\n");
        writer.close();

        writer = new FileWriter(FileUtil.COURSES_TABLE);
        writer.write("id, name, professorId\n");
        writer.close();

        writer = new FileWriter(FileUtil.ASSIGNMENTS_TABLE);
        writer.write("id, courseId, userId, name, grade\n");
        writer.close();

    } catch (IOException e) {
        System.out.println("Error creating tables");
        e.printStackTrace();
    }
}

public static void setup() {
    setUpDatabase();

    String[][] students = {
            { "student1", "email", "password", "false" },
            { "student2", "email", "password", "false" },
            { "student3", "email", "password", "false" } };
    String[][] professors = {
            { "professor1", "email", "password", "true" },
            { "professor2", "email", "password", "true" } };
    String[][] courses = {
            { "course1", "4" },
            { "course2", "4" },
            { "course3", "5" },
            { "course4", "5" } };
    String[][] assignments = {
            { "1", "1", "assignment1", "100" },
            { "1", "1", "assignment2", "90" },
            { "2", "2", "assignment3", "80" },
            { "2", "2", "assignment4", "70" },
            { "2", "3", "assignment5", "60" } };

    for (String[] student : students) {
        FileUtil.insert(student, FileUtil.USERS_TABLE);
    }
    for (String[] professor : professors) {
        FileUtil.insert(professor, FileUtil.USERS_TABLE);
    }
    for (String[] course : courses) {
        FileUtil.insert(course, FileUtil.COURSES_TABLE);
    }
    for (String[] assignment : assignments) {
        FileUtil.insert(assignment, FileUtil.ASSIGNMENTS_TABLE);
    }
}
}
