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

            writer = new FileWriter(FileUtil.ENROLLMENTS_TABLE);
            writer.write("enrollmentId, courseId, userId\n");
            writer.close();

        } catch (IOException e) {
            //System.out.println("Error creating tables");
            //e.printStackTrace();
        }
    }

    public static void setup() {
        setUpDatabase();

        String[][] users = {
                { "s", "s", "$2a$10$MXDm1iJFxQGXqfZWQTs4xuYIHsBDVvbIxA4.a.Wrf0KR.u2/zmSxu", "false" },
                { "p", "p", "$2a$10$grjimYR3NxHXulE5Fg7EI.LIR4vQbaJ/GxLulqalgCPsVXE0//QKu", "true" }
        };

        String[][] courses = {
                { "course1", "2" },
                { "course2", "2" },
                { "course3", "2" },
                { "course4", "2" }
        };
        String[][] assignments = {
                { "1", "1", "assignment1", "100" },
                { "1", "1", "assignment2", "90" },
                { "2", "1", "assignment3", "80" },
                { "2", "1", "assignment4", "70" },
                { "2", "1", "assignment5", "60" },
                { "2", "1", "assignment6", "50" },
                { "3", "1", "assignment7", "40" },
                { "4", "1", "assignment8", "30" }
        };
        String[][] enrollments = {
                { "1", "1" },
                { "2", "1" },
                { "3", "1" },
                { "4", "1" },
        
        };

        for (String[] user : users) {
            FileUtil.insert(user, FileUtil.USERS_TABLE);
        }
        for (String[] course : courses) {
            FileUtil.insert(course, FileUtil.COURSES_TABLE);
        }
        for (String[] assignment : assignments) {
            FileUtil.insert(assignment, FileUtil.ASSIGNMENTS_TABLE);
        }
        for (String[] enrollment : enrollments) {
            FileUtil.insert(enrollment, FileUtil.ENROLLMENTS_TABLE);
        }
    }
}